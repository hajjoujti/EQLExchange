package fr.eql.al36.spring.projet.eqlexchange.repository;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.TradeOrder;
import fr.eql.al36.spring.projet.eqlexchange.domain.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    @Query("SELECT t FROM Transaction t WHERE t.sourceAsset IN :assets OR t.targetAsset IN :assets")
    List<Transaction> findAllByAssets(@Param("assets") List<Asset> assets);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAsset = :asset OR t.targetAsset = :asset")
    List<Transaction> findAllByAsset(@Param("asset") Asset asset);

}
