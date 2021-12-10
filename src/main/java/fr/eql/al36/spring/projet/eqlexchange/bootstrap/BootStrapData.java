package fr.eql.al36.spring.projet.eqlexchange.bootstrap;

import fr.eql.al36.spring.projet.eqlexchange.domain.*;
import fr.eql.al36.spring.projet.eqlexchange.repository.*;
import fr.eql.al36.spring.projet.eqlexchange.service.CurrencyPriceService;
import fr.eql.al36.spring.projet.eqlexchange.service.CurrencyService;
import fr.eql.al36.spring.projet.eqlexchange.service.TradeOrderService;
import fr.eql.al36.spring.projet.eqlexchange.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BootStrapData implements CommandLineRunner {

    private final AssetRepository assetRepository;

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;

    private final CurrencyPriceService currencyPriceService;

    private final CurrencyPriceRepository currencyPriceRepository;

    private final CurrencyTypeRepository currencyTypeRepository;

    private final TradeOrderService tradeOrderService;

    private final TradeOrderRepository tradeOrderRepository;

    private final PaymentRepository paymentRepository;

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;


    public BootStrapData(AssetRepository assetRepository, CurrencyService currencyService, CurrencyRepository currencyRepository,
                         CurrencyPriceService currencyPriceService, CurrencyPriceRepository currencyPriceRepository, CurrencyTypeRepository currencyTypeRepository,
                         TradeOrderService tradeOrderService, TradeOrderRepository tradeOrderRepository, PaymentRepository paymentRepository,
                         TransactionService transactionService, TransactionRepository transactionRepository, UserRepository userRepository,
                         AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {

        this.assetRepository = assetRepository;
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
        this.currencyPriceService = currencyPriceService;
        this.currencyPriceRepository = currencyPriceRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.tradeOrderService = tradeOrderService;
        this.tradeOrderRepository = tradeOrderRepository;
        this.paymentRepository = paymentRepository;
        this.transactionService = transactionService;
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

        Currency eqlcoin = currencyRepository.save(Currency.builder().name("EQL Coin").ticker("EQL").currencyType(
                        cryptocurrency).contractAddress("0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae").circulatingSupply("1000")
                                                           .build());

        Currency dollar = currencyRepository.save(Currency.builder().name("US Dollar").ticker("USD").currencyType(fiat)
                                                          .circulatingSupply("2100000000000").build());

        Currency euro = currencyRepository.save(Currency.builder().name("Euro").ticker("EUR").currencyType(fiat)
                                                        .circulatingSupply("1100000000000").build());

        Currency pound = currencyRepository.save(Currency.builder().name("Pound Sterling").ticker("GBP").currencyType(
                fiat).circulatingSupply("80000000000").build());


        ////////////////////////////////
        // CURRENCY PRICES
        ////////////////////////////////

        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateRandomCurrencyPrices(bitcoin, LocalDateTime.now().minusMinutes(5),30,47128.33));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateRandomCurrencyPrices(ethereum, LocalDateTime.now().minusMinutes(5),30,3922.09));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateRandomCurrencyPrices(binanceCoin, LocalDateTime.now().minusMinutes(5),30,544.18));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateRandomCurrencyPrices(eqlcoin, LocalDateTime.now().minusMinutes(5),30,.5));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateLinearCurrencyPrices(dollar, LocalDateTime.now().minusMinutes(5),30));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateLinearCurrencyPrices(euro, LocalDateTime.now().minusMinutes(5),30));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateLinearCurrencyPrices(pound, LocalDateTime.now().minusMinutes(5),30));
        currencyPriceService.saveCurrencyPrices(currencyPriceService.generateLinearCurrencyPrices(tether, LocalDateTime.now().minusMinutes(5),30));


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
                "EQL_DEBD5C88C70C54820665D03373F1DB3EFE45551F5D3856EDD6A9EAC7920435D9").authorities(userSet).build());


        ////////////////////////////////
        // ASSETS
        ////////////////////////////////

        // OWNER ASSET

        Asset ownerDollar = assetRepository.save(Asset.builder().user(owner).currency(dollar).balance(1000000).build());

        Asset ownerEuro = assetRepository.save(Asset.builder().user(owner).currency(euro).balance(500000).build());

        Asset ownerPound = assetRepository.save(Asset.builder().user(owner).currency(pound).balance(100000).build());

        Asset ownerBitcoin = assetRepository.save(Asset.builder().user(owner).currency(bitcoin).balance(1000).build());

        Asset ownerEthereum = assetRepository.save(Asset.builder().user(owner).currency(ethereum).balance(10000).build());

        Asset ownerBinanceCoin = assetRepository.save(Asset.builder().user(owner).currency(binanceCoin).balance(50000).build());

        Asset ownerTether = assetRepository.save(Asset.builder().user(owner).currency(tether).balance(1000000).build());

        Asset ownerEQLCoin = assetRepository.save(Asset.builder().user(owner).currency(eqlcoin).balance(100000).build());


        //AUTOMATED ASSET GENERATION
        for (User user : userRepository.findAll()) {
            if (user.getId() > 1) {
                System.out.println("Creating assets and trade orders for " + user.getUsername());
                for (Currency currency : currencyRepository.findAll()) {
                    Asset asset = new Asset();
                    double randomAmount = Math.random() * 10000 / currencyPriceService.getLatestPriceOFCurrency(currency).getPrice();
                    assetRepository.save(Asset.builder().user(user).currency(currency).balance(randomAmount).build());
                    System.out.println(" - " + currency.getTicker() + " asset: " + randomAmount + " " + currency.getTicker());

                }
            }
        }


        ////////////////////////////////
        // TRADE ORDERS
        ////////////////////////////////

        //AUTOMATED TRADE ORDER GENERATION
        for (User user : userRepository.findAll()) {
            if (user.getId() > 1) {
                System.out.println("Creating assets and trade orders for " + user.getUsername());
                for (Currency currency : currencyRepository.findAll()) {
                    int randomCurrencyId = currency.getId();
                    while (randomCurrencyId == currency.getId()) {
                        randomCurrencyId = (int) Math.ceil(Math.random() * 8);
                    }
                    Currency randomCurrency = currencyRepository.findById(randomCurrencyId).get();
                    double randomAmountToSell = assetRepository.getAssetByUserAndCurrency(user, currency).getBalance() * Math.random();
                    double calculatedAmountToBuy = currencyService.getCurrencyAmountIn(randomCurrency, currency, randomAmountToSell);
                    System.out.println("   - trade order for " + currency.getTicker() + ":");
                    System.out.println("   - Sell " + randomAmountToSell + " " + currency.getTicker());
                    System.out.println("   - Buy " + calculatedAmountToBuy + " " + randomCurrency.getTicker());


                    TradeOrder tradeOrder = TradeOrder.builder()
                            .user(user)
                            .currencyToBuy(randomCurrency)
                            .amountToBuy(calculatedAmountToBuy)
                            .currencyToSell(currency)
                            .amountToSell(randomAmountToSell)
                            .creationDate(LocalDateTime.now())
                            .build();

                    tradeOrder = tradeOrderService.place(tradeOrder);

                    System.out.println("Trade order placed");
                    List<TradeOrder> matchingTradeOrders = tradeOrderService.match(tradeOrder);

                    if (matchingTradeOrders.size() > 0) {
                        for (TradeOrder matchingTradeOrder : matchingTradeOrders) {
                            System.out.println("maaatch: " + matchingTradeOrder.getId());
                        }

                        TradeOrder selectedTradeOrder = tradeOrderService.selectBestAmong(tradeOrder, matchingTradeOrders);
                        transactionService.executeFromTradeOrders(tradeOrder, selectedTradeOrder);
                    }
                }
            }
        }
    }
}
