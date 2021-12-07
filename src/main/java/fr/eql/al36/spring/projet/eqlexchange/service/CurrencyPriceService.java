package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyPrice;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyPriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyPriceService {
    private final CurrencyPriceRepository currencyPriceRepository;


    public CurrencyPriceService(
            CurrencyPriceRepository currencyPriceRepository) {
        this.currencyPriceRepository = currencyPriceRepository;
    }

    public CurrencyPrice getLatestPriceOFCurrency(Currency currency){
        return currencyPriceRepository.findTopByCurrencyOrderByIdDesc(currency);

    }

    public List<CurrencyPrice> getPricesOfCurrency(Currency currency){
        return currencyPriceRepository.getAllByCurrencyOrderByIdAsc(currency);
    }

}
