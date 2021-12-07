package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    private final CurrencyService currencyService;


    public AssetService(AssetRepository assetRepository,
                        CurrencyService currencyService) {
        this.assetRepository = assetRepository;
        this.currencyService = currencyService;
    }



    public void creditFromSourceAsset(Asset targetAsset, Asset sourceAsset, double amount) {
        if (targetAsset.getCurrency() == sourceAsset.getCurrency() && sourceAsset.getBalance() >= amount) {
            sourceAsset.setBalance(sourceAsset.getBalance() - amount);
            targetAsset.setBalance(targetAsset.getBalance() + amount);
        }
    }

    public List<Asset> getUserWallet(User user) {
        List<Asset> assets = assetRepository.getAssetsByUserOrderByBalanceDesc(user);
        System.out.println("userrrrr" + user.getUsername());
        for(Asset asset : assets) {
            System.out.println("assettttt" + asset.getBalance());
        }
        return assets;
    }

    public Asset getByUserAndCurrency(User user, Currency currency) {
        return assetRepository.getAssetByUserAndCurrency(user, currency);
    }

    public List<Asset> getAllByUser(User user) {
        return assetRepository.getAllByUser(user);
    }

    public List<Asset> getAllByCurrency(Currency currency) {
        return assetRepository.getAllByCurrency(currency);
    }

    public Asset getById(Integer id) {
        return assetRepository.findById(id).get();
    }
}
