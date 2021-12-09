package fr.eql.al36.spring.projet.eqlexchange.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class CurrencyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "currencyType")
    Set<Currency> currencies;


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        CurrencyType that = (CurrencyType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
