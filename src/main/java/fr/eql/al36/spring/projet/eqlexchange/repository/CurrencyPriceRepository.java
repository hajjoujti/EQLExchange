package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyPriceRepository extends CrudRepository<CurrencyPrice, Integer> {

    CurrencyPrice findTopByCurrencyOrderByIdDesc(Currency currency);

    List<CurrencyPrice> getAllByCurrencyOrderByIdAsc(Currency currency);

}
