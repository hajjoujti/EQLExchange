package fr.eql.al36.spring.projet.eqlexchange.domain;

import lombok.*;

import javax.persistence.*;
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
    private double value;
    private String contractAddress;
    private long maximumSupply;
    private String circulatingSupply;

    @ManyToOne
    @JoinColumn(name = "currency_type_id")
    private CurrencyType currencyType;

    @OneToMany(mappedBy = "currency")
    private Set<Order> orders;

    @OneToMany(mappedBy = "currency")
    private Set<Asset> assets;
}
