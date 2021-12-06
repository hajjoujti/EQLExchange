package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetRepository extends CrudRepository<Asset, Integer> {

    List<Asset> getAssetsByUserOrderByBalanceDesc(User user);

    @Query("SELECT a FROM Asset a WHERE a.user = :user AND a.currency = :currency")
    Asset getAssetByUserAndCurrency(@Param("user") User user, @Param("currency") Currency currency);

    List<Asset> getAllByUser(User user);

    List<Asset> getAllByCurrency(Currency currency);
}
