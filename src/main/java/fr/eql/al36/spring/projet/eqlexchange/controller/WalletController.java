package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TransactionRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WalletController {

    private final AssetRepository assetRepository;
    private final TradeOrderRepository tradeOrderRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public WalletController(AssetRepository assetRepository, TradeOrderRepository tradeOrderRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.tradeOrderRepository = tradeOrderRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String getIndexPage(Model model) {
        return "index";
    }

    @GetMapping("wallet/show")
    public String displayWallet(Model model) {
        model.addAttribute("assets", assetRepository.getAssetsByUserOrderByBalanceDesc(userRepository.findById(2).get()));
        return "wallet/show";
    }

    @GetMapping("wallet/{id}")
    public String displayAsset(Model model, @PathVariable String id) {
        model.addAttribute("asset", assetRepository.findById(Integer.parseInt(id)).get());
        model.addAttribute(transactionRepository.findById(id).get());
        return "wallet/details";
    }
}
