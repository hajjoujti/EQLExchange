package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {

}
