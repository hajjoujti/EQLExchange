package fr.eql.al36.spring.projet.eqlexchange.service;

import fr.eql.al36.spring.projet.eqlexchange.domain.Payment;
import fr.eql.al36.spring.projet.eqlexchange.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TransactionService transactionService;

    public PaymentService(PaymentRepository paymentRepository, TransactionService transactionService) {
        this.paymentRepository = paymentRepository;
        this.transactionService = transactionService;
    }

    public void execute(Payment payment) {
        transactionService.executeFromPayment(payment);
        paymentRepository.save(payment);
    }

}
