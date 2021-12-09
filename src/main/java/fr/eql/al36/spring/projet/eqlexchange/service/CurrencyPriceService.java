package fr.eql.al36.spring.projet.eqlexchange.service;

import com.google.gson.Gson;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyPrice;
import fr.eql.al36.spring.projet.eqlexchange.dto.CurrencyPriceDTO;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyPriceRepository;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyPriceService {

    private final CurrencyPriceRepository currencyPriceRepository;


    public CurrencyPriceService(
            CurrencyPriceRepository currencyPriceRepository) {
        this.currencyPriceRepository = currencyPriceRepository;
    }


    public CurrencyPrice getLatestPriceOFCurrency(Currency currency) {
        return currencyPriceRepository.findTopByCurrencyOrderByIdDesc(currency);

    }


    public List<CurrencyPrice> getPricesOfCurrency(Currency currency) {
        return currencyPriceRepository.getAllByCurrencyOrderByDateTimeAsc(currency);
    }


    public String getCurrencyPricesJSON(Currency currency) {
        List<CurrencyPriceDTO> currencyPriceDTOS = new ArrayList<>();
        for(CurrencyPrice currencyPrice : getPricesOfCurrency(currency)) {
            currencyPriceDTOS.add(CurrencyPriceDTO.toDto(currencyPrice));
        }
        return new Gson().toJson(currencyPriceDTOS);
    }

}
