package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;


    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
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

}
