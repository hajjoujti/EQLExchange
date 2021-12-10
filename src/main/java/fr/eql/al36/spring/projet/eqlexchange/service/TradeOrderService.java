package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyPriceRepository;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TradeOrderService {

    private final double MAX_SLIPPAGE_RATE = .97;

    private final TradeOrderRepository tradeOrderRepository;
    private final CurrencyPriceRepository currencyPriceRepository;
    private final AssetRepository assetRepository;

    public TradeOrderService(TradeOrderRepository tradeOrderRepository, CurrencyPriceRepository currencyPriceRepository,
                             AssetRepository assetRepository) {
        this.tradeOrderRepository = tradeOrderRepository;
        this.currencyPriceRepository = currencyPriceRepository;
        this.assetRepository = assetRepository;
    }


    // Returns Trade Order value in reference currency (dollar by design)
    public double getReferenceValue(TradeOrder tradeOrder) {
        return tradeOrder.getAmountToBuy() * currencyPriceRepository.findTopByCurrencyOrderByIdDesc(tradeOrder.getCurrencyToBuy()).getPrice();
    }

    public boolean haveSameReferenceValue(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        double tradeOrder1Value = getReferenceValue(tradeOrder1);
        double tradeOrder2Value = getReferenceValue(tradeOrder2);

        if (tradeOrder1Value == tradeOrder2Value) return true;
        return tradeOrder1Value / tradeOrder2Value >= MAX_SLIPPAGE_RATE &&
               1 / (tradeOrder2Value / tradeOrder1Value) <= 1 / MAX_SLIPPAGE_RATE;
    }

    public List<TradeOrder> getSortedByValue(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        double tradeOrder1Value = getReferenceValue(tradeOrder1);
        double tradeOrder2Value = getReferenceValue(tradeOrder2);

        List<TradeOrder> tradeOrders = new ArrayList<>();

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
        System.out.println("match(): referenceTradeOrder.getCurrencyToBuy : " + referenceTradeOrder.getCurrencyToBuy().getTicker());
        System.out.println("match(): referenceTradeOrder.getAmountToBuy : " + referenceTradeOrder.getAmountToBuy());
        System.out.println("match(): referenceTradeOrder.getCurrencyToSell : " + referenceTradeOrder.getCurrencyToSell().getTicker());
        System.out.println("match(): referenceTradeOrder.getAmountToSell : " + referenceTradeOrder.getAmountToSell());
        //return tradeOrderRepository.findAllMatchingTradeOrders(referenceTradeOrder.getUser(), referenceTradeOrder.getCurrencyToBuy(), referenceTradeOrder.getCurrencyToSell());
        List<TradeOrder> allTradeOrders = (List) tradeOrderRepository.findAll();
        List<TradeOrder> matchingTradeOrders = new ArrayList<>();
        for (TradeOrder tradeOrder : allTradeOrders) {
            System.out.println("match(): treating trade order " + tradeOrder.getId());
            System.out.println("match(): trade order " + tradeOrder.getId() + " user: " + tradeOrder.getUser().getId() + " " + tradeOrder.getUser().getUsername());
            System.out.println("match(): trade order " + tradeOrder.getId() + " currency to buy: " + tradeOrder.getCurrencyToBuy().getTicker());
            System.out.println("match(): trade order " + tradeOrder.getId() + " amount to buy: " + tradeOrder.getAmountToBuy());
            System.out.println("match(): trade order " + tradeOrder.getId() + " currency to sell: " + tradeOrder.getCurrencyToSell().getTicker());
            System.out.println("match(): trade order " + tradeOrder.getId() + " amount to sell: " + tradeOrder.getAmountToSell());
            System.out.println("match(): trade order " + tradeOrder.getId() + " completion date: " + tradeOrder.getCompletionDate());
            System.out.println("match(): trade order " + tradeOrder.getId() + " cancellation date: " + tradeOrder.getCancellationDate());
            if (tradeOrder.getCompletionDate() == null
                    && tradeOrder.getCancellationDate() == null
                    && !Objects.equals(tradeOrder.getUser().getId(), referenceTradeOrder.getUser().getId())
                    && Objects.equals(tradeOrder.getCurrencyToBuy().getId(), referenceTradeOrder.getCurrencyToSell().getId())
                    && Objects.equals(tradeOrder.getCurrencyToSell().getId(), referenceTradeOrder.getCurrencyToBuy().getId())) {
                System.out.println("match(): ************************************** trade order " + tradeOrder.getId() + " is a match !!!!!!!!!!!");
                matchingTradeOrders.add(tradeOrder);
                System.out.println("match: trade order with id " + tradeOrder.getId());
            }
        }
        System.out.println("match(): total matches: " + matchingTradeOrders.size());
        return matchingTradeOrders;
    }

    public TradeOrder selectBestAmong(TradeOrder referenceTradeOrder, List<TradeOrder> candidateTradeOrders) {

        double referenceValue = getReferenceValue(referenceTradeOrder);
        List<TradeOrder> topTradeOrders = new ArrayList<>();
        List<TradeOrder> middleTradeOrders = new ArrayList<>();
        List<TradeOrder> bottomTradeOrders = new ArrayList<>();

        for (TradeOrder candidateTradeOrder : candidateTradeOrders) {
            double candidateValue = getReferenceValue(candidateTradeOrder);
            if(candidateValue / referenceValue >= MAX_SLIPPAGE_RATE) {
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

    public TradeOrder place(TradeOrder tradeOrder) {
        System.out.println("place: tradeOrder.getUser()" + tradeOrder.getUser().getUsername());
        System.out.println("place: tradeOrder.getCurrencyToBuy()" + tradeOrder.getCurrencyToSell().getTicker());
        System.out.println("place: assetRepository.getAssetByUserAndCurrency(tradeOrder.getUser(),tradeOrder.getCurrencyToBuy()).getBalance()" + assetRepository.getAssetByUserAndCurrency(tradeOrder.getUser(),tradeOrder.getCurrencyToSell()).getBalance());
        System.out.println("place: tradeOrder.getAmountToBuy()" + tradeOrder.getAmountToSell());
        if (assetRepository.getAssetByUserAndCurrency(tradeOrder.getUser(),tradeOrder.getCurrencyToSell()).getBalance() >= tradeOrder.getAmountToSell()) {
            tradeOrder = tradeOrderRepository.save(tradeOrder);
            return tradeOrder;
        }
        return null;
    }
}
