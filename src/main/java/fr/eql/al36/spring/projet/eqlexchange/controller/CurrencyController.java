package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.Currency;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.service.CurrencyService;
import fr.eql.al36.spring.projet.eqlexchange.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class CurrencyController {

    private final TransactionService transactionService;
    private final CurrencyService currencyService;


    public CurrencyController(TransactionService transactionService, CurrencyService currencyService) {
        this.transactionService = transactionService;
        this.currencyService = currencyService;
    }

    @GetMapping("currency/{id}/details")
    public String displayCurrencyDetails(Model model, HttpSession session, @PathVariable String id) {
        User connectedUser = (User) session.getAttribute("sessionUser");
        Currency currency = currencyService.findCurrencyById(Integer.parseInt(id));

        if(currency == null){
            return "currency/non-existant";
        }
        model.addAttribute("currency", currency);
        model.addAttribute("connectedUser", connectedUser);
        //model.addAttribute("transactions", transactionService.getTransactionsDoneOnCurrency(currency));
        return "currency/details";
    }

}
