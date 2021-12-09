package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    @Query("SELECT c FROM Currency c WHERE c.id <> :id ORDER BY c.ticker ASC")
    List<Currency> getAllExceptOneWithId(@Param("id") Integer id);

    List<Currency> getCurrenciesByCurrencyTypeOrderByNameAsc(CurrencyType currencyType);
}
