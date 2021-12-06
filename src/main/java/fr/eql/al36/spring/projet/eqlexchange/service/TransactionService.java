package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AssetService assetService;
    private final TradeOrderService tradeOrderService;
    private final TransactionRepository transactionRepository;
    private final TradeOrderRepository tradeOrderRepository;
    private final AssetRepository assetRepository;


    public TransactionService(AssetService assetService,
                              TradeOrderService tradeOrderService,
                              TransactionRepository transactionRepository,
                              TradeOrderRepository tradeOrderRepository,
                              AssetRepository assetRepository) {

        this.assetService = assetService;
        this.tradeOrderService = tradeOrderService;
        this.transactionRepository = transactionRepository;
        this.tradeOrderRepository = tradeOrderRepository;
        this.assetRepository = assetRepository;
    }

    public List<Transaction> getTransactionsDoneByUser(User user) {
        List<Asset> assets = assetRepository.getAllByUser(user);
        List<TradeOrder> tradeOrders = tradeOrderRepository.getAllByAssetIn(assets);
        return transactionRepository.findAllByTradeOrders(tradeOrders);
    }


    public List<Transaction> getTransactionsDoneOnCurrency(Currency currency) {
        List<Asset> assets = assetRepository.getAllByCurrency(currency);
        List<TradeOrder> tradeOrders = tradeOrderRepository.getAllByAssetIn(assets);
        return transactionRepository.findAllByTradeOrders(tradeOrders);
    }

    public void execute(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        if(tradeOrder1.getAsset().getCurrency() != tradeOrder2.getCurrency() || tradeOrder2.getAsset().getCurrency() != tradeOrder1.getCurrency()) return;

        double remainingAmount = 0;

        List<TradeOrder> sortedTradeOrders = tradeOrderService.getSortedByValue(tradeOrder1, tradeOrder2);
        TradeOrder biggerTradeOrder = sortedTradeOrders.get(0);
        TradeOrder smallerTradeOrder = sortedTradeOrders.get(1);
        double transactionValue = tradeOrderService.getReferenceValue(smallerTradeOrder);

        assetService.creditFromSourceAsset(
                smallerTradeOrder.getAsset(),
                assetRepository.getAssetByUserAndCurrency(
                        biggerTradeOrder.getAsset().getUser(),
                        biggerTradeOrder.getCurrency()),
                transactionValue / biggerTradeOrder.getCurrency().getValue());

        assetService.creditFromSourceAsset(
                biggerTradeOrder.getAsset(),
                assetRepository.getAssetByUserAndCurrency(
                        smallerTradeOrder.getAsset().getUser(),
                        smallerTradeOrder.getCurrency()),
                transactionValue / smallerTradeOrder.getCurrency().getValue());

        if (tradeOrderService.haveSameReferenceValue(tradeOrder1,tradeOrder2)) {
            remainingAmount = 0;
        } else {
            remainingAmount = (tradeOrderService.getReferenceValue(biggerTradeOrder) - transactionValue) / biggerTradeOrder.getAsset().getCurrency().getValue();
        }

        Transaction transaction = transactionRepository.save(Transaction.builder()
                .tradeOrder1(smallerTradeOrder)
                .tradeOrder2(biggerTradeOrder)
                .remainingAmount(remainingAmount)
                .date(LocalDateTime.now())
                .build());
    }
}
