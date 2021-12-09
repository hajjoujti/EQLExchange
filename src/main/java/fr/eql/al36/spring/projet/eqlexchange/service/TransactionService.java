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
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final double MAX_SLIPPAGE_RATE = .97;

    private final AssetService assetService;

    private final TradeOrderService tradeOrderService;

    private final TransactionRepository transactionRepository;

    private final AssetRepository assetRepository;


    public TransactionService(AssetService assetService, TradeOrderService tradeOrderService,
                              TransactionRepository transactionRepository,
                              AssetRepository assetRepository) {

        this.assetService = assetService;
        this.tradeOrderService = tradeOrderService;
        this.transactionRepository = transactionRepository;
        this.assetRepository = assetRepository;
    }


    public List<Transaction> getTransactionsDoneByUser(User user) {
        List<Asset> assets = assetRepository.getAllByUser(user);
        return transactionRepository.findAllByAssets(assets);
    }


    public List<Transaction> getTransactionsByAsset(Asset asset) {
        return transactionRepository.findAllByAsset(asset);
    }


    public List<Transaction> getTransactionsDoneOnCurrency(Currency currency) {
        List<Asset> assets = assetRepository.getAllByCurrency(currency);
        return transactionRepository.findAllByAssets(assets);
    }


    public void execute(TradeOrder tradeOrder1, TradeOrder tradeOrder2) {

        System.out.println("entered execute");
        System.out.println("----------");
        System.out.println("execute: tradeOrder1 id:" + tradeOrder1.getId());
        System.out.println("execute: tradeOrder1 user:" + tradeOrder1.getUser().getUsername());
        System.out.println("execute: tradeOrder1 currency to buy:" + tradeOrder1.getCurrencyToBuy().getTicker());
        System.out.println("execute: tradeOrder1 amount to buy:" + tradeOrder1.getAmountToBuy());
        System.out.println("execute: tradeOrder1 currency to sell:" + tradeOrder1.getCurrencyToSell().getTicker());
        System.out.println("execute: tradeOrder1 amount to sell:" + tradeOrder1.getAmountToSell());
        System.out.println("-----------------------------------------------------");
        System.out.println("execute: tradeOrder2 id:" + tradeOrder2.getId());
        System.out.println("execute: tradeOrder2 user:" + tradeOrder2.getUser().getUsername());
        System.out.println("execute: tradeOrder2 currency to buy:" + tradeOrder2.getCurrencyToBuy().getTicker());
        System.out.println("execute: tradeOrder2 amount to buy:" + tradeOrder2.getAmountToBuy());
        System.out.println("execute: tradeOrder2 currency to sell:" + tradeOrder2.getCurrencyToSell().getTicker());
        System.out.println("execute: tradeOrder2 amount to sell:" + tradeOrder2.getAmountToSell());
        System.out.println("-----------------------------------------------------");
        if(tradeOrder1.getCurrencyToBuy().getId() != tradeOrder2.getCurrencyToSell().getId()
           || tradeOrder1.getCurrencyToSell().getId() != tradeOrder2.getCurrencyToBuy().getId()) {
            System.out.println("couille dans le paté");
            System.out.println("tradeOrder1.getCurrencyToBuy().getId() : " + tradeOrder1.getCurrencyToBuy().getId());
            System.out.println("tradeOrder2.getCurrencyToSell().getId() : " + tradeOrder2.getCurrencyToSell().getId());
            System.out.println("tradeOrder1.getCurrencyToSell().getId() : " + tradeOrder1.getCurrencyToSell().getId());
            System.out.println("tradeOrder2.getCurrencyToBuy().getId() : " + tradeOrder2.getCurrencyToBuy().getId());
            return;
        }
        System.out.println("execute: orders currencies match");

        boolean isEven = false;
        if(tradeOrder1.getAmountToBuy() / tradeOrder2.getAmountToSell() >= MAX_SLIPPAGE_RATE
           && tradeOrder2.getAmountToBuy() / tradeOrder1.getAmountToSell() >= MAX_SLIPPAGE_RATE) {
            isEven = true;
        }

        double amount;
        if(isEven) {
            amount = tradeOrder1.getAmountToSell();
        } else {
            amount = (Math.min(tradeOrder1.getAmountToBuy(), tradeOrder2.getAmountToSell()));
        }

        User tradeOrder1User = tradeOrder1.getUser();
        User tradeOrder2User = tradeOrder2.getUser();

        Currency currency = tradeOrder1.getCurrencyToSell();
        Asset sourceAsset = assetService.getByUserAndCurrency(tradeOrder1User, currency);
        Asset targetAsset = assetService.getByUserAndCurrency(tradeOrder2User, currency);
        assetService.creditFromSourceAsset(targetAsset, sourceAsset, amount);
        Transaction transaction = Transaction.builder()
                .sourceAsset(sourceAsset)
                .targetAsset(targetAsset)
                .amount(amount)
                .date(LocalDateTime.now())
                .build();
        transaction.setTxId("tx_" + transaction.hashCode());
        transactionRepository.save(transaction);

        if(isEven) {
            amount = tradeOrder2.getAmountToSell();
        } else {
            amount = (Math.min(tradeOrder2.getAmountToBuy(), tradeOrder1.getAmountToSell()));
        }
        currency = tradeOrder2.getCurrencyToSell();
        sourceAsset = assetService.getByUserAndCurrency(tradeOrder2User, currency);
        targetAsset = assetService.getByUserAndCurrency(tradeOrder1User, currency);
        assetService.creditFromSourceAsset(targetAsset, sourceAsset, tradeOrder2.getAmountToSell());
        transaction = Transaction.builder()
                .sourceAsset(sourceAsset)
                .targetAsset(targetAsset)
                .amount(amount)
                .date(LocalDateTime.now())
                .build();
        transaction.setTxId("tx_" + transaction.hashCode());
        transactionRepository.save(transaction);

        if(!isEven) {
            TradeOrder biggerTradeOrder;
            TradeOrder smallerTradeOrder;
            if(tradeOrder1.getAmountToBuy() > tradeOrder2.getAmountToSell()) {
                biggerTradeOrder = tradeOrder1;
                smallerTradeOrder = tradeOrder2;
            } else {
                biggerTradeOrder = tradeOrder2;
                smallerTradeOrder = tradeOrder1;
            }

            tradeOrderService.place(TradeOrder.builder()
                                            .user(biggerTradeOrder.getUser())
                                            .currencyToBuy(biggerTradeOrder.getCurrencyToBuy())
                                            .currencyToSell(biggerTradeOrder.getCurrencyToSell())
                                            .amountToBuy(biggerTradeOrder.getAmountToBuy() -
                                                         smallerTradeOrder.getAmountToSell())
                                            .amountToSell(biggerTradeOrder.getAmountToSell() -
                                                          smallerTradeOrder.getAmountToBuy())
                                            .creationDate(LocalDateTime.now())
                                            .build());
        }
    }

}