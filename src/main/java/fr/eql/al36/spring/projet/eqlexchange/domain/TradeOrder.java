package fr.eql.al36.spring.projet.eqlexchange.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class TradeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime creationDate;
    private LocalDateTime cancellationDate;
    private LocalDateTime completionDate;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "currency_to_sell_id")
    private Currency currencyToSell;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeOrder tradeOrder = (TradeOrder) o;
        return Double.compare(tradeOrder.amount, amount) == 0 && Objects.equals(id, tradeOrder.id) && Objects.equals(creationDate, tradeOrder.creationDate) && Objects.equals(cancellationDate, tradeOrder.cancellationDate) && Objects.equals(completionDate, tradeOrder.completionDate) && Objects.equals(asset, tradeOrder.asset) && Objects.equals(currency, tradeOrder.currency) && Objects.equals(transaction, tradeOrder.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, cancellationDate, completionDate, amount, asset, currency, transaction);
    }
}
