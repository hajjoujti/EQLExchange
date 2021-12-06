package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    @Query("SELECT t FROM Transaction t WHERE t.tradeOrder1 IN :tradeOrders OR t.tradeOrder2 IN :tradeOrders")
    List<Transaction> findAllByTradeOrders(@Param("tradeOrders") List<TradeOrder> tradeOrders);

}
