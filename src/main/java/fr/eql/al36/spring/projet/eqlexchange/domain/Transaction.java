package fr.eql.al36.spring.projet.eqlexchange.domain;

import fr.eql.al36.spring.projet.eqlexchange.repository.TradeOrderRepository;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;
    private String txId;

    @ManyToOne
    @JoinColumn(name = "source_asset_id")
    private Asset sourceAsset;

     @ManyToOne
    @JoinColumn(name = "target_asset_id")
    private Asset targetAsset;

    private double amount;


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(id, that.id) &&
               Objects.equals(date, that.date) && Objects.equals(txId, that.txId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, date, txId, amount);
    }

}
