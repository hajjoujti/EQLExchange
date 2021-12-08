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


}
