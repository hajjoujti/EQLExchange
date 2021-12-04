package fr.eql.al36.spring.projet.eqlexchange.bootstrap;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyType;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BootStrapData implements CommandLineRunner {

    private final AssetRepository assetRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyTypeRepository currencyTypeRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public BootStrapData(AssetRepository assetRepository,
                         CurrencyRepository currencyRepository,
                         CurrencyTypeRepository currencyTypeRepository,
                         OrderRepository orderRepository,
                         PaymentRepository paymentRepository,
                         TransactionRepository transactionRepository,
                         UserRepository userRepository) {

        this.assetRepository = assetRepository;
        this.currencyRepository = currencyRepository;
        this.currencyTypeRepository = currencyTypeRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    private void initData() {

        ////////////////////////////////
        // CURRENCY TYPES
        ////////////////////////////////

        CurrencyType fiat = currencyTypeRepository.save(CurrencyType.builder()
                .name("Fiat")
                .build());

        CurrencyType cryptocurrency = currencyTypeRepository.save(CurrencyType.builder()
                .name("Cryptocurrency")
                .build());

        ////////////////////////////////
        // CURRENCIES
        ////////////////////////////////

        Currency bitcoin = currencyRepository.save(Currency.builder()
                        .name("Bitcoin")
                        .ticker("BTC")
                        .currencyType(cryptocurrency)
                        .value(47128.33)
                        .contractAddress("0x321162Cd933E2Be498Cd2267a90534A804051b11")
                        .maximumSupply(21000000)
                        .circulatingSupply("188890987")
                .build());

        Currency ethereum = currencyRepository.save(Currency.builder()
                        .name("Ethereum")
                        .ticker("ETH")
                        .currencyType(cryptocurrency)
                        .value(3922.09)
                        .contractAddress("0xAb5801a7D398351b8bE11C439e05C5B3259aeC9B")
                        .circulatingSupply("118602065")
                .build());

        Currency binanceCoin = currencyRepository.save(Currency.builder()
                        .name("Binance Coin")
                        .ticker("BNB")
                        .currencyType(cryptocurrency)
                        .value(544.18)
                        .contractAddress("0xb8c77482e45f1f44de1745f52c74426c631bdd52")
                        .circulatingSupply("166801148")
                .build());

        Currency tether = currencyRepository.save(Currency.builder()
                        .name("Tether")
                        .ticker("USDT")
                        .currencyType(cryptocurrency)
                        .value(1)
                        .contractAddress("0xdac17f958d2ee523a2206206994597c13d831ec7")
                        .circulatingSupply("74157654134")
                .build());

        Currency eqlcoin = currencyRepository.save(Currency.builder()
                        .name("EQL Coin")
                        .ticker("XQL")
                        .currencyType(cryptocurrency)
                        .value(.5)
                        .contractAddress("0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae")
                        .circulatingSupply("1000")
                .build());

        Currency dollar = currencyRepository.save(Currency.builder()
                .name("US Dollar")
                .ticker("USD")
                .currencyType(fiat)
                .value(1)
                .circulatingSupply("2100000000000")
                .build());

        Currency euro = currencyRepository.save(Currency.builder()
                .name("Euro")
                .ticker("EUR")
                .currencyType(fiat)
                .value(1.13)
                .circulatingSupply("1100000000000")
                .build());

        Currency pound = currencyRepository.save(Currency.builder()
                .name("Pound Sterling")
                .ticker("GBP")
                .currencyType(fiat)
                .value(1.32)
                .circulatingSupply("80000000000")
                .build());

        ////////////////////////////////
        // USERS
        ////////////////////////////////

        User owner = userRepository.save(User.builder()
                .firstName("EQL")
                .lastName("Exchange")
                .username("EQLExchange")
                .dateOfBirth(LocalDate.of(1969,6,1))
                .email("bigbux@eqlexchange.io")
                .password("admin")
                .walletAddress("EQL_0F887AC6986B00BBDE4AA91A0B82430A782E93603973AE81A6EC1041789EDA94")
                .build());

        User alain = userRepository.save(User.builder()
                .firstName("Alain")
                .lastName("Musque")
                .username("imsocool420")
                .dateOfBirth(LocalDate.of(1969,6,1))
                .email("alain.musque@yahoo.fr")
                .password("alain010669")
                .walletAddress("EQL_DBF8B58DEC30242DD3E1A64331B9DACDB58CFA0F7742AA47E0984CF4098997AB")
                .build());

        User annesophie = userRepository.save(User.builder()
                .firstName("annesophie")
                .lastName("Ladouille")
                .username("douilledu13")
                .dateOfBirth(LocalDate.of(1994,4,1))
                .email("douilledu13@yopmail.com")
                .password("douilledouilledouille")
                .walletAddress("EQL_DEBD5C88C70C54820665D03373F1DB3EFE45551F5D3856EDD6A9EAC7920435D7")
                .build());

        User robert = userRepository.save(User.builder()
                .firstName("Robert")
                .lastName("Pushard")
                .username("pusher_bobby")
                .dateOfBirth(LocalDate.of(1956,11,8))
                .email("pouchard11@numericable.fr")
                .password("jesusmarietf1")
                .walletAddress("EQL_DEBD5C88C70C54820665D03373F1DB3EFE45551F5D3856EDD6A9EAC7920435D7")
                .build());

        ////////////////////////////////
        // ASSETS
        ////////////////////////////////

        // OWNER ASSET

        Asset ownerDollar = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(dollar)
                        .balance(10000)
                .build());

        Asset ownerEuro = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(euro)
                        .balance(5000)
                .build());

        Asset ownerPound = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(pound)
                        .balance(1000)
                .build());

        Asset ownerBitcoin = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(bitcoin)
                        .balance(5)
                .build());

        Asset ownerEthereum = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(ethereum)
                        .balance(10)
                .build());

        Asset ownerBinanceCoin = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(binanceCoin)
                        .balance(20)
                .build());

        Asset ownerTether = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(tether)
                        .balance(10000)
                .build());

        Asset ownerEQLCoin = assetRepository.save(Asset.builder()
                        .user(owner)
                        .currency(eqlcoin)
                        .balance(1000)
                .build());

        // ALAIN ASSET
        Asset alainDollar = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(dollar)
                .balance(20)
                .build());

        Asset alainEuro = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(euro)
                .balance(0)
                .build());

        Asset alainPound = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(pound)
                .balance(0)
                .build());

        Asset alainBitcoin = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(bitcoin)
                .balance(.02239446)
                .build());

        Asset alainEthereum = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(ethereum)
                .balance(.0551802)
                .build());

        Asset alainBinanceCoin = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(binanceCoin)
                .balance(.65766)
                .build());

        Asset alainTether = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(tether)
                .balance(20.015223)
                .build());

        Asset alainEQLCoin = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(eqlcoin)
                .balance(0)
                .build());

        // ANNESOPHIE ASSET

        Asset annesophieDollar = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(dollar)
                .balance(0)
                .build());

        Asset annesophieEuro = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(euro)
                .balance(35)
                .build());

        Asset annesophiePound = assetRepository.save(Asset.builder()
                .user(alain)
                .currency(pound)
                .balance(0)
                .build());

        Asset annesophieBitcoin = assetRepository.save(Asset.builder()
                .user(annesophie)
                .currency(bitcoin)
                .balance(.06445)
                .build());

        Asset annesophieEthereum = assetRepository.save(Asset.builder()
                .user(annesophie)
                .currency(ethereum)
                .balance(.031458)
                .build());

        Asset annesophieBinanceCoin = assetRepository.save(Asset.builder()
                .user(annesophie)
                .currency(binanceCoin)
                .balance(.8451)
                .build());

        Asset annesophieTether = assetRepository.save(Asset.builder()
                .user(annesophie)
                .currency(tether)
                .balance(31.84565)
                .build());

        Asset annesophieEQLCoin = assetRepository.save(Asset.builder()
                .user(annesophie)
                .currency(eqlcoin)
                .balance(0)
                .build());

        // ROBERT ASSET

        Asset robertDollar = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(dollar)
                .balance(0)
                .build());

        Asset robertEuro = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(euro)
                .balance(0)
                .build());

        Asset robertPound = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(pound)
                .balance(30)
                .build());

        Asset robertBitcoin = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(bitcoin)
                .balance(.13568)
                .build());

        Asset robertEthereum = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(ethereum)
                .balance(1.045)
                .build());

        Asset robertBinanceCoin = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(binanceCoin)
                .balance(.68426)
                .build());

        Asset robertTether = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(tether)
                .balance(32.852)
                .build());

        Asset robertEQLCoin = assetRepository.save(Asset.builder()
                .user(robert)
                .currency(eqlcoin)
                .balance(0)
                .build());
    }
}
