package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TradeOrderRepository extends CrudRepository<TradeOrder, Integer> {

    List<TradeOrder> getAllByAssetIn(List<Asset> assets);

    @Query("SELECT to FROM TradeOrder to WHERE to.currency = :asset_currency AND to.asset.currency = :wanted_currency AND to.completionDate IS NULL AND to.cancellationDate IS NULL ORDER by to.amount")
    List<TradeOrder> findAllMatchingTradeOrders(@Param("asset_currency") Currency assetCurrency, @Param("wanted_currency") Currency wantedCurrency);

}
