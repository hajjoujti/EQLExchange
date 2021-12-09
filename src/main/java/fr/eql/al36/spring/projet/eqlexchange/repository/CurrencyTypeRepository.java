package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyType;
import fr.eql.al36.spring.projet.eqlexchange.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyTypeRepository extends CrudRepository<CurrencyType, Integer> {

    public CurrencyType findByName(String name);
}
