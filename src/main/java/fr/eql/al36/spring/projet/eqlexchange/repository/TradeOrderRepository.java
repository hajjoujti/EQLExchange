package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TradeOrderRepository extends CrudRepository<TradeOrder, Integer> {

    List<TradeOrder> getAllByAssetIn(List<Asset> assets);

}
