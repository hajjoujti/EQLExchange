package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Payment;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.service.AssetService;
import fr.eql.al36.spring.projet.eqlexchange.service.CurrencyService;
import fr.eql.al36.spring.projet.eqlexchange.service.PaymentService;
import fr.eql.al36.spring.projet.eqlexchange.service.TransactionService;
import fr.eql.al36.spring.projet.eqlexchange.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    private final CurrencyService currencyService;

    private final PaymentService paymentService;

    private final AssetService assetService;

    private final UserService userService;


    public TransactionController(TransactionService transactionService, CurrencyService currencyService,
                                 PaymentService paymentService, AssetService assetService,
                                 UserService userService) {
        this.transactionService = transactionService;
        this.currencyService = currencyService;
        this.paymentService = paymentService;
        this.assetService = assetService;
        this.userService = userService;
    }


    @GetMapping("wallet/history")
    public String displayWallet(Model model, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("sessionUser", connectedUser);
        //model.addAttribute("transactions", transactionService.getTransactionsDoneByUser(connectedUser));
        return "wallet/history";
    }


    @GetMapping("wallet/refill")
    public String refillWallet(Model model, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("sessionUser", connectedUser);
        model.addAttribute("fiatCurrencies", currencyService.getFiatCurrencies());
        model.addAttribute("payment", new Payment());
        return "transaction/fillup";
    }


    @PostMapping("/wallet/refill/pay")
    public String payRefill(Model model, @ModelAttribute Payment payment, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        Asset targetAsset = assetService.getByUserAndCurrency(connectedUser, payment.getCurrency());
        payment.setAsset(targetAsset);
        paymentService.execute(payment);

        return "redirect:/wallet/";
    }


    @GetMapping("wallet/{id}/transfer")
    public String makeTransfer(Model model, HttpSession session, @PathVariable String id) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("sessionUser", connectedUser);
        model.addAttribute("sourceAsset", assetService.getById(Integer.parseInt(id)));
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("targetUser", new User());
        return "transaction/transfer";
    }


    @PostMapping("wallet/save-transaction")
    public String saveTransaction(Model model, @ModelAttribute User targetUser, @ModelAttribute Transaction transaction,
                                  @ModelAttribute Asset sourceAsset, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("sessionUser", connectedUser);
        String walletAddress = targetUser.getWalletAddress();

        targetUser = userService.findUserWalletAddress(walletAddress);
        if(targetUser == null) {
            model.addAttribute("walletAddress", walletAddress);
            return "transaction/address-non-existant";
        }

        sourceAsset = assetService.getById(sourceAsset.getId());
        if(sourceAsset.getBalance() < transaction.getAmount()) {
            model.addAttribute("asset", sourceAsset);
            return "transaction/not-enough-funds";
        }

        transactionService.transferMoney(targetUser, sourceAsset, transaction);
        return "redirect:/wallet/" + sourceAsset.getId();
    }

}
