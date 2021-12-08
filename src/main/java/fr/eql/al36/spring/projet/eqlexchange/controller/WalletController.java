package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TransactionRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.UserRepository;
import fr.eql.al36.spring.projet.eqlexchange.service.AssetService;
import fr.eql.al36.spring.projet.eqlexchange.service.CurrencyPriceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class WalletController {

    private final AssetService assetService;
    private final TradeOrderRepository tradeOrderRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CurrencyPriceService currencyPriceService;

    public WalletController(AssetService assetService, TradeOrderRepository tradeOrderRepository, TransactionRepository transactionRepository, UserRepository userRepository, CurrencyPriceService currencyPriceService) {
        this.assetService = assetService;
        this.tradeOrderRepository = tradeOrderRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.currencyPriceService = currencyPriceService;
    }

    @GetMapping("/")
    public String getIndexPage(Model model) {
        return "index";
    }

    @GetMapping("wallet")
    public String displayWallet(Model model, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("assets", assetService.getUserWallet(connectedUser));
        return "wallet/show";
    }

    @GetMapping("wallet/{id}")
    public String displayAsset(Model model, HttpSession session, @PathVariable String id) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("asset", assetService.getById(Integer.parseInt(id)));
        return "wallet/details";
    }
}
