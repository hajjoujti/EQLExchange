package fr.eql.al36.spring.projet.eqlexchange.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String ticker;
    private String contractAddress;
    private long maximumSupply;
    private String circulatingSupply;

    @ManyToOne
    @JoinColumn(name = "currency_type_id")
    private CurrencyType currencyType;

    @OneToMany(mappedBy = "currency")
    private Set<TradeOrder> tradeOrders;

    @OneToMany(mappedBy = "currency")
    private Set<Asset> assets;

    @OneToMany(mappedBy = "currency")
    private List<CurrencyPrice> currencyPrices;


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return maximumSupply == currency.maximumSupply && Objects.equals(id, currency.id) && Objects.equals(name,
                                                                                                            currency.name) &&
               Objects.equals(ticker, currency.ticker) && Objects.equals(contractAddress, currency.contractAddress) &&
               Objects.equals(circulatingSupply, currency.circulatingSupply);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, ticker, contractAddress, maximumSupply, circulatingSupply);
    }

}
