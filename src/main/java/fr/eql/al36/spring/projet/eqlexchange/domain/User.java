package fr.eql.al36.spring.projet.eqlexchange.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String username;

    private LocalDate dateOfBirth;

    private String email;

    private String password;

    private String walletAddress;

    @OneToMany(mappedBy = "user")
    Set<Asset> assets;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_authority", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities;


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName,
                                                                                                          user.lastName) &&
               Objects.equals(username, user.username) && Objects.equals(dateOfBirth, user.dateOfBirth) &&
               Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(
                walletAddress, user.walletAddress);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, dateOfBirth, email, password, walletAddress);
    }


    public double totalAssetsValue(List<Asset> assets) {
        double totalValue = 0;
        for(Asset asset : assets) {
            List<CurrencyPrice> currencyPrices = asset.getCurrency().getCurrencyPrices();
            totalValue += (currencyPrices.get(currencyPrices.size() - 1).getPrice()) * asset.getBalance();
        }
        return totalValue;
    }

}
