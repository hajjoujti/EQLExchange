package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private AssetRepository assetRepository;

    public void creditFromSourceAsset(Asset targetAsset, Asset sourceAsset, double amount) {
        if (targetAsset.getCurrency() == sourceAsset.getCurrency() && sourceAsset.getBalance() >= amount) {
            sourceAsset.setBalance(sourceAsset.getBalance() - amount);
            targetAsset.setBalance(targetAsset.getBalance() + amount);
        }
    }
}
