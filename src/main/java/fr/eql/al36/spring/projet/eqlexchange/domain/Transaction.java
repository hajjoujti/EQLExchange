package fr.eql.al36.spring.projet.eqlexchange.domain;

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
    private double remainingAmount;

    @OneToMany(mappedBy = "transaction")
    Set<TradeOrder> tradeOrders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.remainingAmount, remainingAmount) == 0 && Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(txId, that.txId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, txId, remainingAmount);
    }
}
