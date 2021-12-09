package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyType;
import fr.eql.al36.spring.projet.eqlexchange.repository.CurrencyTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyTypeService {

    private final CurrencyTypeRepository currencyTypeRepository;

    public CurrencyTypeService(CurrencyTypeRepository currencyTypeRepository) {
        this.currencyTypeRepository = currencyTypeRepository;
    }

    public CurrencyType getByName(String name) {
        return currencyTypeRepository.findByName(name);
    }

}
