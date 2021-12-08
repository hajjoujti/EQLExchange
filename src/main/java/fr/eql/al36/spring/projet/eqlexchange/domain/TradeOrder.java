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
    private double amountToSell;
    private double amountToBuy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currencyToBuy;

    @ManyToOne
    @JoinColumn(name = "currency_to_sell_id")
    private Currency currencyToSell;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeOrder that = (TradeOrder) o;
        return Double.compare(that.amountToSell, amountToSell) == 0 && Double.compare(that.amountToBuy, amountToBuy) == 0 && Objects.equals(id, that.id) && Objects.equals(creationDate, that.creationDate) && Objects.equals(cancellationDate, that.cancellationDate) && Objects.equals(completionDate, that.completionDate) && Objects.equals(user, that.user) && Objects.equals(currencyToBuy, that.currencyToBuy) && Objects.equals(currencyToSell, that.currencyToSell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, cancellationDate, completionDate, amountToSell, amountToBuy, user, currencyToBuy, currencyToSell);
    }
}