package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeOrderService {

    private TradeOrderRepository tradeOrderRepository;
    private TransactionRepository transactionRepository;
    private final double MAX_SLIPPAGE_RATE = .97;

    // Returns Trade Order value in reference currency (dollar by design)
    public double getReferenceValue(TradeOrder tradeOrder) {
        return tradeOrder.getAmount() * tradeOrder.getAsset().getCurrency().getValue();
    }

    public boolean haveSameReferenceValue(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        double tradeOrder1Value = getReferenceValue(tradeOrder1);
        double tradeOrder2Value = getReferenceValue(tradeOrder2);

        if (tradeOrder1Value == tradeOrder2Value) return true;
        if(tradeOrder1Value / tradeOrder2Value >= MAX_SLIPPAGE_RATE
                && 1 / (tradeOrder2Value / tradeOrder1Value) <= 1 / MAX_SLIPPAGE_RATE) {
            return true;
        }
        return false;
    }

    public List<TradeOrder> getSortedByValue(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        double tradeOrder1Value = getReferenceValue(tradeOrder1);
        double tradeOrder2Value = getReferenceValue(tradeOrder2);

        List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();

        // HORRIBLE !!! NO TIME TO OFFER BETTER !!!
        if (tradeOrder1Value > tradeOrder2Value) {
            tradeOrders.add(tradeOrder1);
            tradeOrders.add(tradeOrder2);
        }
        else {
            tradeOrders.add(tradeOrder2);
            tradeOrders.add(tradeOrder1);
        }
        return tradeOrders;
    }

    public void send(TradeOrder tradeOrder) {
        tradeOrderRepository.save(tradeOrder);
    }

    public List<TradeOrder> match(TradeOrder referenceTradeOrder) {
        return tradeOrderRepository.findAllMatchingTradeOrders(referenceTradeOrder.getAsset().getCurrency(), referenceTradeOrder.getCurrency());
    }

    public TradeOrder selectBestAmong(TradeOrder referenceTradeOrder, List<TradeOrder> candidateTradeOrders) {

        double referenceValue = referenceTradeOrder.getAmount() * referenceTradeOrder.getAsset().getCurrency().getValue();
        List<TradeOrder> topTradeOrders = new ArrayList<>();
        List<TradeOrder> middleTradeOrders = new ArrayList<>();
        List<TradeOrder> bottomTradeOrders = new ArrayList<>();

        for (TradeOrder candidateTradeOrder : candidateTradeOrders) {
            double candidateValue = candidateTradeOrder.getAmount() * candidateTradeOrder.getAsset().getCurrency().getValue();
            if(referenceValue / candidateValue >= MAX_SLIPPAGE_RATE && 1 / (candidateValue / referenceValue) <= 1 / MAX_SLIPPAGE_RATE) {
                return candidateTradeOrder;
            }
            else if(referenceValue / candidateValue >= .5 && referenceValue / candidateValue <= 2) {
                topTradeOrders.add(candidateTradeOrder);
            }
            else if(referenceValue / candidateValue >= .25 && referenceValue / candidateValue <= 4) {
                middleTradeOrders.add(candidateTradeOrder);
            }
            else {
                bottomTradeOrders.add(candidateTradeOrder);
            }
        }
        if (topTradeOrders.size() > 0) {
            return topTradeOrders.get(0);
        }
        else if (middleTradeOrders.size() > 0) {
            return middleTradeOrders.get(0);
        }
        else if (bottomTradeOrders.size() > 0) {
            return bottomTradeOrders.get(0);
        }
        else {
            return null;
        }
    }

    public TradeOrder createFromUnsatisfiedTransaction(Transaction transaction) {

        List<TradeOrder> sortedTradeOrders = getSortedByValue(transaction.getTradeOrder1(), transaction.getTradeOrder2());
        TradeOrder unsatisfiedTradeOrder = sortedTradeOrders.get(0);

        TradeOrder newTradeOrder = tradeOrderRepository.save(TradeOrder.builder()
                .asset(unsatisfiedTradeOrder.getAsset())
                .amount(transaction.getRemainingAmount())
                .currency(unsatisfiedTradeOrder.getCurrency())
                .creationDate(LocalDateTime.now())
                .build());

        return newTradeOrder;
    }
}
