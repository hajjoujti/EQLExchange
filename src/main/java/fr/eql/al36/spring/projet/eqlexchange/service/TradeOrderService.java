package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class TradeOrderService {

    private TradeOrderRepository tradeOrderRepository;
    private TransactionRepository transactionRepository;

    public void send(TradeOrder tradeOrder) {
        tradeOrderRepository.save(tradeOrder);
    }

    public List<TradeOrder> match(TradeOrder referenceTradeOrder) {
        return tradeOrderRepository.findAllMatchingTradeOrders(referenceTradeOrder.getAsset().getCurrency(), referenceTradeOrder.getCurrency());
    }

    public TradeOrder chooseAmong(TradeOrder referenceTradeOrder, List<TradeOrder> candidateTradeOrders) {

        double referenceValue = referenceTradeOrder.getAmount() * referenceTradeOrder.getAsset().getCurrency().getValue();
        List<TradeOrder> topTradeOrders = new ArrayList<>();
        List<TradeOrder> middleTradeOrders = new ArrayList<>();
        List<TradeOrder> bottomTradeOrders = new ArrayList<>();

        for (TradeOrder candidateTradeOrder : candidateTradeOrders) {
            double candidateValue = candidateTradeOrder.getAmount() * candidateTradeOrder.getAsset().getCurrency().getValue();
            if(referenceValue / candidateValue >= .99 && referenceValue / candidateValue <= 1.01) {
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
}
