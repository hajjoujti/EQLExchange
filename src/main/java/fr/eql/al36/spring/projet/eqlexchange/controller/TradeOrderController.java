package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.service.TradeOrderService;
import fr.eql.al36.spring.projet.eqlexchange.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@SessionAttributes("sessionUser")
public class TradeOrderController {

    private final TradeOrderRepository tradeOrderRepository;
    private final CurrencyRepository currencyRepository;
    private final TradeOrderService tradeOrderService;
    private final TransactionService transactionService;
    private final AssetRepository assetRepository;

    public TradeOrderController(TradeOrderRepository tradeOrderRepository,
                                CurrencyRepository currencyRepository,
                                TradeOrderService tradeOrderService, TransactionService transactionService, AssetRepository assetRepository) {

        this.tradeOrderRepository = tradeOrderRepository;
        this.currencyRepository = currencyRepository;
        this.tradeOrderService = tradeOrderService;
        this.transactionService = transactionService;
        this.assetRepository = assetRepository;
    }

    @GetMapping("trade/{id1}/{id2}")
    public String trade(Model model, @PathVariable String id1, @PathVariable String id2, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        Currency currencyToBuy = currencyRepository.findById(Integer.parseInt(id1)).get();
        Currency currencyToSell = currencyRepository.findById(Integer.parseInt(id2)).get();
        model.addAttribute("targetAsset",assetRepository.getAssetByUserAndCurrency(connectedUser, currencyToBuy));
        model.addAttribute("sourceAsset",assetRepository.getAssetByUserAndCurrency(connectedUser, currencyToSell));
        model.addAttribute("currencyToBuy",currencyRepository.findById(Integer.parseInt(id1)).get());
        model.addAttribute("currencyToSell",currencyRepository.findById(Integer.parseInt(id2)).get());
        model.addAttribute("tradeOrder", new TradeOrder());
        return "transaction/trade";
    }

    @PostMapping("trade/place")
    public String placeTradeOrder(@ModelAttribute TradeOrder tradeOrder, HttpSession session) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        tradeOrderService.place(tradeOrder, connectedUser);
        List<TradeOrder> matchingTradeOrders = tradeOrderService.match(tradeOrder);
        TradeOrder selectedTradeOrder = tradeOrderService.selectBestAmong(tradeOrder, matchingTradeOrders);
        transactionService.execute(tradeOrder, selectedTradeOrder);
        return "redirect:/transaction/trade/" + tradeOrder.getAsset().getCurrency().getTicker() + "/" + tradeOrder.getCurrency().getTicker();
    }
}
