package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyPriceRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final AssetService assetService;
    private final TradeOrderService tradeOrderService;
    private final TransactionRepository transactionRepository;
    private final TradeOrderRepository tradeOrderRepository;
    private final AssetRepository assetRepository;
    private final CurrencyPriceRepository currencyPriceRepository;
    private final CurrencyPriceService currencyPriceService;


    public TransactionService(AssetService assetService, TradeOrderService tradeOrderService,
                              TransactionRepository transactionRepository, TradeOrderRepository tradeOrderRepository,
                              AssetRepository assetRepository, CurrencyPriceRepository currencyPriceRepository, CurrencyPriceService currencyPriceService) {

        this.assetService = assetService;
        this.tradeOrderService = tradeOrderService;
        this.transactionRepository = transactionRepository;
        this.tradeOrderRepository = tradeOrderRepository;
        this.assetRepository = assetRepository;
        this.currencyPriceRepository = currencyPriceRepository;
        this.currencyPriceService = currencyPriceService;
    }

    public List<Transaction> getTransactionsDoneByUser(User user) {
        List<Asset> assets = assetRepository.getAllByUser(user);
        List<TradeOrder> tradeOrders = tradeOrderRepository.getAllByAssetIn(assets);
        return transactionRepository.findAllByTradeOrders(tradeOrders);
    }

    public List<Transaction> getTransactionsByAsset(Asset asset) {
        List<Asset> assets = new ArrayList<>();
        assets.add(asset);
        List<TradeOrder> tradeOrders = tradeOrderRepository.getAllByAssetIn(assets);
        return transactionRepository.findAllByTradeOrders(tradeOrders);
    }


    public List<Transaction> getTransactionsDoneOnCurrency(Currency currency) {
        List<Asset> assets = assetRepository.getAllByCurrency(currency);
        List<TradeOrder> tradeOrders = tradeOrderRepository.getAllByAssetIn(assets);
        return transactionRepository.findAllByTradeOrders(tradeOrders);
    }

    public void execute(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        System.out.println("entered execute");
        System.out.println("execute: tradeOrder1 asset user:" + tradeOrder1.getAsset().getUser().getUsername());
        System.out.println("execute: tradeOrder2 asset user:" + tradeOrder2.getAsset().getUser().getUsername());
        if(tradeOrder1.getAsset().getCurrency() != tradeOrder2.getCurrency() || tradeOrder2.getAsset().getCurrency() != tradeOrder1.getCurrency()) return;
        System.out.println("execute: orders currencies match");
        double remainingAmount;

        List<TradeOrder> sortedTradeOrders = tradeOrderService.getSortedByValue(tradeOrder1, tradeOrder2);
        System.out.println("execute: sortedTradeOrders.size: " + sortedTradeOrders.size());
        TradeOrder biggerTradeOrder = sortedTradeOrders.get(0);
        TradeOrder smallerTradeOrder = sortedTradeOrders.get(1);
        double transactionValue = tradeOrderService.getReferenceValue(smallerTradeOrder);

        System.out.println("execute: about to credit smaller order asset");
        System.out.println("execute: smallerTradeOrder.getAsset(): " + smallerTradeOrder.getAsset());
        System.out.println("execute: : biggerTradeOrder.getAsset().getUser()" + biggerTradeOrder.getAsset().getUser());
        System.out.println("execute: : biggerTradeOrder.getCurrency()" + biggerTradeOrder.getCurrency());
        System.out.println("execute: : transactionValue" + transactionValue);
        System.out.println("execute: biggerTradeOrder.getCurrency().getCurrencyPrices().size(): " + biggerTradeOrder.getCurrency().getCurrencyPrices().size());
        System.out.println("execute: biggerTradeOrder.getCurrency().getCurrencyPrices().get(biggerTradeOrder.getCurrency().getCurrencyPrices().size() - 1).getPrice(): " + biggerTradeOrder.getCurrency().getCurrencyPrices().get(biggerTradeOrder.getCurrency().getCurrencyPrices().size() - 1).getPrice());
        assetService.creditFromSourceAsset(
                smallerTradeOrder.getAsset(),
                assetRepository.getAssetByUserAndCurrency(
                        biggerTradeOrder.getAsset().getUser(),
                        biggerTradeOrder.getCurrency()),
                //transactionValue / currencyPriceService.getLatestPriceOFCurrency(biggerTradeOrder.getCurrency()).getPrice());
                transactionValue / biggerTradeOrder.getCurrency().getCurrencyPrices().get(biggerTradeOrder.getCurrency().getCurrencyPrices().size() - 1).getPrice());

        System.out.println("execute: about to credit bigger order asset");
        System.out.println("execute: biggerTradeOrder.getAsset(): " + biggerTradeOrder.getAsset());
        System.out.println("execute: biggerTradeOrder.getAsset().getId(): " + biggerTradeOrder.getAsset().getId());
        System.out.println("execute: smallerTradeOrder.getAsset().getUser(): " + smallerTradeOrder.getAsset().getUser());
        System.out.println("execute: smallerTradeOrder.getCurrency(): " + smallerTradeOrder.getCurrency());
        System.out.println("execute: transactionValue: " + transactionValue);
        assetService.creditFromSourceAsset(
                biggerTradeOrder.getAsset(),
                assetRepository.getAssetByUserAndCurrency(
                        smallerTradeOrder.getAsset().getUser(),
                        smallerTradeOrder.getCurrency()),
                //transactionValue / currencyPriceRepository.findTopByCurrencyOrderByIdDesc(smallerTradeOrder.getCurrency()).getPrice());
                transactionValue / smallerTradeOrder.getCurrency().getCurrencyPrices().get(smallerTradeOrder.getCurrency().getCurrencyPrices().size() - 1).getPrice());

        System.out.println("execute: about to process remaining amount");
        if (tradeOrderService.haveSameReferenceValue(tradeOrder1,tradeOrder2)) {
            System.out.println("execute: both orders have same reference value, no remaining amount");
            remainingAmount = 0;
        } else {
            remainingAmount = (tradeOrderService.getReferenceValue(biggerTradeOrder) - transactionValue) / currencyPriceRepository.findTopByCurrencyOrderByIdDesc(biggerTradeOrder.getCurrency()).getPrice();
            System.out.println("execute: order 1 is smaller than order 2, remaining amount: " + remainingAmount);
        }

        System.out.println("execute: about to save transaction");
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .tradeOrder1(smallerTradeOrder)
                .tradeOrder2(biggerTradeOrder)
                .remainingAmount(remainingAmount)
                .date(LocalDateTime.now())
                .build());
    }
}
