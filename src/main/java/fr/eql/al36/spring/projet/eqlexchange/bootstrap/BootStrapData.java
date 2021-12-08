package fr.eql.al36.spring.projet.eqlexchange.bootstrap;

import fr.eql.al36.spring.projet.eqlexchange.domain.*;
import fr.eql.al36.spring.projet.eqlexchange.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class BootStrapData implements CommandLineRunner {

    private final AssetRepository assetRepository;

    private final CurrencyRepository currencyRepository;

    private final CurrencyPriceRepository currencyPriceRepository;

    private final CurrencyTypeRepository currencyTypeRepository;

    private final TradeOrderRepository tradeOrderRepository;

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;


    public BootStrapData(AssetRepository assetRepository, CurrencyRepository currencyRepository,
                         CurrencyPriceRepository currencyPriceRepository, CurrencyTypeRepository currencyTypeRepository,
                         TradeOrderRepository tradeOrderRepository, PaymentRepository paymentRepository,
                         TransactionRepository transactionRepository, UserRepository userRepository,
                         AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {

        this.assetRepository = assetRepository;
        this.currencyRepository = currencyRepository;
        this.currencyPriceRepository = currencyPriceRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.tradeOrderRepository = tradeOrderRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        initData();
    }


    private void initData() {

        ////////////////////////////////
        // CURRENCY TYPES
        ////////////////////////////////

        CurrencyType fiat = currencyTypeRepository.save(CurrencyType.builder().name("Fiat").build());

        CurrencyType cryptocurrency = currencyTypeRepository.save(
                CurrencyType.builder().name("Cryptocurrency").build());

        ////////////////////////////////
        // CURRENCIES
        ////////////////////////////////

        Currency bitcoin = currencyRepository.save(Currency.builder().name("Bitcoin").ticker("BTC").currencyType(
                        cryptocurrency).contractAddress("0x321162Cd933E2Be498Cd2267a90534A804051b11").maximumSupply(21000000)
                                                           .circulatingSupply("188890987").build());

        Currency ethereum = currencyRepository.save(Currency.builder().name("Ethereum").ticker("ETH").currencyType(
                        cryptocurrency).contractAddress("0xAb5801a7D398351b8bE11C439e05C5B3259aeC9B")
                                                            .circulatingSupply("118602065").build());

        Currency binanceCoin = currencyRepository.save(Currency.builder().name("Binance Coin").ticker("BNB")
                                                               .currencyType(cryptocurrency).contractAddress(
                        "0xb8c77482e45f1f44de1745f52c74426c631bdd52").circulatingSupply("166801148").build());

        Currency tether = currencyRepository.save(Currency.builder().name("Tether").ticker("USDT").currencyType(
                        cryptocurrency).contractAddress("0xdac17f958d2ee523a2206206994597c13d831ec7")
                                                          .circulatingSupply("74157654134").build());

        Currency eqlcoin = currencyRepository.save(Currency.builder().name("EQL Coin").ticker("XQL").currencyType(
                        cryptocurrency).contractAddress("0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae").circulatingSupply("1000")
                                                           .build());

        Currency dollar = currencyRepository.save(Currency.builder().name("US Dollar").ticker("USD").currencyType(fiat)
                                                          .circulatingSupply("2100000000000").build());

        Currency euro = currencyRepository.save(Currency.builder().name("Euro").ticker("EUR").currencyType(fiat)
                                                        .circulatingSupply("1100000000000").build());

        Currency pound = currencyRepository.save(Currency.builder().name("Pound Sterling").ticker("GBP").currencyType(
                fiat).circulatingSupply("80000000000").build());

        ////////////////////////////////
        // CURRENCIES PRICES
        ////////////////////////////////

        CurrencyPrice bitcoinPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(bitcoin).price(
                47128.33).dateTime(LocalDateTime.now()).build());
        CurrencyPrice ethereumPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(ethereum).price(
                3922.09).dateTime(LocalDateTime.now()).build());
        CurrencyPrice binanceCoinPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(binanceCoin)
                                                                              .price(544.18)
                                                                              .dateTime(LocalDateTime.now()).build());
        CurrencyPrice tetherPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(tether).price(1.0)
                                                                         .dateTime(LocalDateTime.now()).build());
        CurrencyPrice eqlcoinPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(eqlcoin).price(.5)
                                                                          .dateTime(LocalDateTime.now()).build());
        CurrencyPrice dollarPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(dollar).price(1.0)
                                                                         .dateTime(LocalDateTime.now()).build());
        CurrencyPrice euroPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(euro).price(1.13)
                                                                       .dateTime(LocalDateTime.now()).build());
        CurrencyPrice poundPrice = currencyPriceRepository.save(CurrencyPrice.builder().currency(pound).price(1.32)
                                                                        .dateTime(LocalDateTime.now()).build());

        ////////////////////////////////
        // USERS ROLES
        ////////////////////////////////

        Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
        Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());

        Set<Authority> adminSet = new HashSet<>();
        adminSet.add(adminRole);
        Set<Authority> userSet = new HashSet<>();
        userSet.add(userRole);

        ////////////////////////////////
        // USERS
        ////////////////////////////////

        User owner = userRepository.save(User.builder().firstName("EQL").lastName("Exchange").username("EQLExchange")
                                                 .dateOfBirth(LocalDate.of(1969, 6, 1)).email("bigbux@eqlexchange.io")
                                                 .password(passwordEncoder.encode("admin")).walletAddress(
                        "EQL_0F887AC6986B00BBDE4AA91A0B82430A782E93603973AE81A6EC1041789EDA94").authorities(adminSet)
                                                 .build());

        User alain = userRepository.save(User.builder().firstName("Alain").lastName("Musque").username("imsocool420")
                                                 .dateOfBirth(LocalDate.of(1969, 6, 1)).email("alain.musque@yahoo.fr")
                                                 .password(passwordEncoder.encode("toto")).walletAddress(
                        "EQL_DBF8B58DEC30242DD3E1A64331B9DACDB58CFA0F7742AA47E0984CF4098997AB").authorities(userSet)
                                                 .build());

        User annesophie = userRepository.save(User.builder().firstName("Anne-Sophie").lastName("Ladouille").username(
                "douilledu13").dateOfBirth(LocalDate.of(1994, 4, 1)).email("douilledu13@yopmail.com").password(
                passwordEncoder.encode("douilledouilledouille")).walletAddress(
                "EQL_DEBD5C88C70C54820665D03373F1DB3EFE45551F5D3856EDD6A9EAC7920435D7").authorities(userSet).build());

        User robert = userRepository.save(User.builder().firstName("Robert").lastName("Pushard").username(
                "pusher_bobby").dateOfBirth(LocalDate.of(1956, 11, 8)).email("pouchard11@numericable.fr").password(
                passwordEncoder.encode("jesusmarietf1")).walletAddress(
                "EQL_DEBD5C88C70C54820665D03373F1DB3EFE45551F5D3856EDD6A9EAC7920435D7").authorities(userSet).build());

        ////////////////////////////////
        // ASSETS
        ////////////////////////////////

        // OWNER ASSET

        Asset ownerDollar = assetRepository.save(Asset.builder().user(owner).currency(dollar).balance(10000).build());

        Asset ownerEuro = assetRepository.save(Asset.builder().user(owner).currency(euro).balance(5000).build());

        Asset ownerPound = assetRepository.save(Asset.builder().user(owner).currency(pound).balance(1000).build());

        Asset ownerBitcoin = assetRepository.save(Asset.builder().user(owner).currency(bitcoin).balance(5).build());

        Asset ownerEthereum = assetRepository.save(Asset.builder().user(owner).currency(ethereum).balance(10).build());

        Asset ownerBinanceCoin = assetRepository.save(Asset.builder().user(owner).currency(binanceCoin).balance(20)
                                                              .build());

        Asset ownerTether = assetRepository.save(Asset.builder().user(owner).currency(tether).balance(10000).build());

        Asset ownerEQLCoin = assetRepository.save(Asset.builder().user(owner).currency(eqlcoin).balance(1000).build());

        // ALAIN ASSET
        Asset alainDollar = assetRepository.save(Asset.builder().user(alain).currency(dollar).balance(20).build());

        Asset alainEuro = assetRepository.save(Asset.builder().user(alain).currency(euro).balance(0).build());

        Asset alainPound = assetRepository.save(Asset.builder().user(alain).currency(pound).balance(0).build());

        Asset alainBitcoin = assetRepository.save(Asset.builder().user(alain).currency(bitcoin).balance(.02239446)
                                                          .build());

        Asset alainEthereum = assetRepository.save(Asset.builder().user(alain).currency(ethereum).balance(.0551802)
                                                           .build());

        Asset alainBinanceCoin = assetRepository.save(Asset.builder().user(alain).currency(binanceCoin).balance(.65766)
                                                              .build());

        Asset alainTether = assetRepository.save(Asset.builder().user(alain).currency(tether).balance(20.015223)
                                                         .build());

        Asset alainEQLCoin = assetRepository.save(Asset.builder().user(alain).currency(eqlcoin).balance(0).build());

        // ANNESOPHIE ASSET

        Asset annesophieDollar = assetRepository.save(Asset.builder().user(annesophie).currency(dollar).balance(0)
                                                              .build());

        Asset annesophieEuro = assetRepository.save(Asset.builder().user(annesophie).currency(euro).balance(35)
                                                            .build());

        Asset annesophiePound = assetRepository.save(Asset.builder().user(annesophie).currency(pound).balance(0)
                                                             .build());

        Asset annesophieBitcoin = assetRepository.save(Asset.builder().user(annesophie).currency(bitcoin)
                                                               .balance(.06445).build());

        Asset annesophieEthereum = assetRepository.save(Asset.builder().user(annesophie).currency(ethereum)
                                                                .balance(.031458).build());

        Asset annesophieBinanceCoin = assetRepository.save(Asset.builder().user(annesophie).currency(binanceCoin)
                                                                   .balance(.8451).build());

        Asset annesophieTether = assetRepository.save(Asset.builder().user(annesophie).currency(tether)
                                                              .balance(31.84565).build());

        Asset annesophieEQLCoin = assetRepository.save(Asset.builder().user(annesophie).currency(eqlcoin).balance(0)
                                                               .build());

        // ROBERT ASSET

        Asset robertDollar = assetRepository.save(Asset.builder().user(robert).currency(dollar).balance(0).build());

        Asset robertEuro = assetRepository.save(Asset.builder().user(robert).currency(euro).balance(0).build());

        Asset robertPound = assetRepository.save(Asset.builder().user(robert).currency(pound).balance(30).build());

        Asset robertBitcoin = assetRepository.save(Asset.builder().user(robert).currency(bitcoin).balance(.13568)
                                                           .build());

        Asset robertEthereum = assetRepository.save(Asset.builder().user(robert).currency(ethereum).balance(1.045)
                                                            .build());

        Asset robertBinanceCoin = assetRepository.save(Asset.builder().user(robert).currency(binanceCoin)
                                                               .balance(.68426).build());

        Asset robertTether = assetRepository.save(Asset.builder().user(robert).currency(tether).balance(32.852)
                                                          .build());

        Asset robertEQLCoin = assetRepository.save(Asset.builder().user(robert).currency(eqlcoin).balance(0).build());


        ////////////////////////////////
        // TRADE ORDERS
        ////////////////////////////////


        // ALAIN TRADE ORDERS

        // Alain wants to sell .0015 BTC for BNB
        TradeOrder to1 = tradeOrderRepository.save(TradeOrder.builder().asset(alainBitcoin).currency(binanceCoin)
                                                           .amount(.0015)
                                                           .creationDate(LocalDateTime.of(2021, 6, 2, 18, 6, 52))
                                                           .build());

        // Alain wants to sell .027 ETH for BNB
        TradeOrder to2 = tradeOrderRepository.save(TradeOrder.builder().asset(alainEthereum).currency(binanceCoin)
                                                           .amount(.027)
                                                           .creationDate(LocalDateTime.of(2021, 6, 2, 18, 27, 04))
                                                           .build());

        // Alain wants to sell .0012 BTC for EQL
        TradeOrder to3 = tradeOrderRepository.save(TradeOrder.builder().asset(alainBitcoin).currency(eqlcoin).amount(
                0.0012).creationDate(LocalDateTime.of(2021, 6, 2, 19, 1, 44)).build());


        // ANNE-SOPHIE TRADE ORDERS

        // Anne-Sophie wants to sell .002 BTC for EQL
        TradeOrder to4 = tradeOrderRepository.save(TradeOrder.builder().asset(annesophieBitcoin).amount(0.002).currency(
                eqlcoin).creationDate(LocalDateTime.of(2021, 6, 3, 9, 26, 11)).build());

        // Anne-Sophie wants to sell .0194 BNB for ETH
        TradeOrder to5 = tradeOrderRepository.save(TradeOrder.builder().asset(annesophieBinanceCoin).amount(0.194)
                                                           .currency(ethereum)
                                                           .creationDate(LocalDateTime.of(2021, 6, 3, 10, 01, 57))
                                                           .build());


        // ROBERT TRADE ORDERS

        // Robert wants to sell 200 EQL for BTC
        TradeOrder to6 = tradeOrderRepository.save(TradeOrder.builder().asset(robertEQLCoin).amount(200).currency(
                bitcoin).creationDate(LocalDateTime.of(2021, 6, 3, 11, 04, 3)).build());

        // Robert wants to sell .021 BNB for EQL
        TradeOrder to7 = tradeOrderRepository.save(TradeOrder.builder().asset(robertBinanceCoin).amount(0.021).currency(
                eqlcoin).creationDate(LocalDateTime.of(2021, 6, 3, 11, 07, 21)).build());


        ////////////////////////////////
        // TRANSACTIONS
        ////////////////////////////////


        // TRANSACTION 1

        // Involves orders 2 and 5

        // Alain (2) gets 0.194 BNB (order 5's amount) and Anne-Sophie (5) gets 0.027 ETH (order 2's amount)

        // 1. Alain's ETH asset is debited with order 3's amount
        Asset alainWantedCurrencyAsset = assetRepository.getAssetByUserAndCurrency(to2.getAsset().getUser(),
                                                                                   to2.getCurrency());
        alainWantedCurrencyAsset.setBalance(alainWantedCurrencyAsset.getBalance() - to5.getAmount());

        // 2. Anne-Sophie's BNB asset is debited with order 3's amount
        Asset annesophieWantedCurrencyAsset = assetRepository.getAssetByUserAndCurrency(to5.getAsset().getUser(),
                                                                                        to5.getCurrency());
        alainWantedCurrencyAsset.setBalance(alainWantedCurrencyAsset.getBalance() - to2.getAmount());


        // 3. Alain's BNB asset is credited with his order's amount
        to2.getAsset().setBalance(to2.getAsset().getBalance() + to2.getAmount());

        // 3. Anne-Sophie's ETH asset is credited with her order's amount
        to5.getAsset().setBalance(to5.getAsset().getBalance() + to5.getAmount());

        // Both orders are completely satisfied and are updated with completion date

        Transaction transaction1 = transactionRepository.save(Transaction.builder().date(
                        LocalDateTime.of(2021, 6, 3, 10, 2, 1)).remainingAmount(0).txId(
                                "tx_c4ca4238a0b923820dcc509a6f75849b")
                                                                      .tradeOrder1(to2).tradeOrder2(to5).build());


        // TRANSACTION 2

        // Involves orders 3 and 6
        // Alain (3) gets 118.1 EQL (/!\ only a part of order 6's amount)
        // Robert (6) gets 0.0012 BTC (order 3's amount)

        to6.setAmount(to6.getAmount() + to3.getAmount());

        Transaction transaction2 = transactionRepository.save(Transaction.builder().date(
                LocalDateTime.of(2021, 6, 3, 11, 4, 5)).remainingAmount(40.9).txId(
                "tx_c81e728d9d4c2f636f067f89cc14862c").tradeOrder1(to3).tradeOrder2(to6).build());

        // Order 3 is satisfied, but not order 6
        // A new order is created with Order 6 remaining amount at the end transaction completion

        TradeOrder to8 = tradeOrderRepository.save(TradeOrder.builder().asset(robertEQLCoin).amount(
                        transaction2.getRemainingAmount()).currency(bitcoin)
                                                           .creationDate(LocalDateTime.of(2021, 6, 3, 11, 4, 7))
                                                           .build());

    }

}
