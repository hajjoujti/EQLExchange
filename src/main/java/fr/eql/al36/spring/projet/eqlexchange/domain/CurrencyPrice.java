package fr.eql.al36.spring.projet.eqlexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class CurrencyPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double price;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        CurrencyPrice that = (CurrencyPrice) o;
        return Objects.equals(id, that.id) && Objects.equals(price, that.price);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, price);
    }

}
