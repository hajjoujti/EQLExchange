package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.service.AssetService;
import fr.eql.al36.spring.projet.eqlexchange.service.CurrencyPriceService;
import fr.eql.al36.spring.projet.eqlexchange.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class WalletController {

    private final AssetService assetService;
    private final CurrencyPriceService currencyPriceService;
    private final TransactionService transactionService;

    public WalletController(AssetService assetService, CurrencyPriceService currencyPriceService, TransactionService transactionService) {
        this.assetService = assetService;
        this.currencyPriceService = currencyPriceService;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String getIndexPage(Model model) {
        return "index";
    }

    @GetMapping("wallet")
    public String displayWallet(Model model, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        model.addAttribute("sessionUser", connectedUser);
        model.addAttribute("assets", assetService.getUserWallet(connectedUser));
        return "wallet/show";
    }

    @GetMapping("wallet/{id}")
    public String displayAsset(Model model, HttpSession session, @PathVariable String id) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        Currency currency = assetService.getById(Integer.parseInt(id)).getCurrency();
        Double marketCap = assetService.calculMarketCap(currency);
        Asset asset = assetService.getById(Integer.parseInt(id));
        model.addAttribute("asset", asset);
        model.addAttribute("sessionUser", connectedUser);
        model.addAttribute("marketcap", marketCap);
        model.addAttribute("currencyPricesJSON", currencyPriceService.getCurrencyPricesJSON(currency));
        model.addAttribute("transactions",transactionService.getTransactionsByAsset(asset));
        return "wallet/details";
    }
}
