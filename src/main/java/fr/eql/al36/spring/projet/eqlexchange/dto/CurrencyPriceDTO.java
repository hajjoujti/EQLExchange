package fr.eql.al36.spring.projet.eqlexchange.dto;

import fr.eql.al36.spring.projet.eqlexchange.domain.CurrencyPrice;

import java.time.LocalDateTime;

public class CurrencyPriceDTO {

    private double price;

    private LocalDateTime dateTime;


    private CurrencyPriceDTO(double price, LocalDateTime dateTime) {
        this.price = price;
        this.dateTime = dateTime;
    }


    @Override
    public String toString() {
        return "CurrencyPriceDTO{" +
               "price=" + price +
               ", dateTime=" + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond() +
               '}';
    }


    public static CurrencyPriceDTO toDto(CurrencyPrice currencyPrice){
        return new CurrencyPriceDTO(currencyPrice.getPrice(), currencyPrice.getDateTime());
    }




    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }

}
