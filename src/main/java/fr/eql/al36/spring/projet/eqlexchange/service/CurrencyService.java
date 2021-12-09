package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyPriceService currencyPriceService;
    private final CurrencyTypeService currencyTypeService;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyPriceService currencyPriceService, CurrencyTypeService currencyTypeService) {
        this.currencyRepository = currencyRepository;
        this.currencyPriceService = currencyPriceService;
        this.currencyTypeService = currencyTypeService;
    }


    public Currency findCurrencyById(Integer id) {
        if(currencyRepository.findById(id).isPresent()) {
            return currencyRepository.findById(id).get();
        }
        return null;
    }

    public List<Currency> getAllExceptOneWithId(Integer id) {
        return currencyRepository.getAllExceptOneWithId(id);
    }

    public double getCurrencyAmountIn(Currency outputCurrency, Currency inputCurrency, double inputAmount) {
        double outputCurrencyValue = currencyPriceService.getLatestPriceOFCurrency(outputCurrency).getPrice();
        double inputCurrencyValue = currencyPriceService.getLatestPriceOFCurrency(inputCurrency).getPrice();
        return inputCurrencyValue * inputAmount / outputCurrencyValue;
    }

    public List<Currency> getFiatCurrencies() {
        return currencyRepository.getCurrenciesByCurrencyTypeOrderByNameAsc(currencyTypeService.getByName("fiat"));
    }


}
