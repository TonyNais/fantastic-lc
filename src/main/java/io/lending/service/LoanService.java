package io.lending.service;

import io.lending.dto.LoanDTO;
import io.lending.entity.Loan;
import io.lending.entity.Subscriber;
import io.lending.repository.LoanRepository;
import io.lending.repository.SubscriberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final SubscriberRepository subscriberRepository;
    private final NotificationService notificationService;

    public LoanService(LoanRepository loanRepository, SubscriberRepository subscriberRepository, NotificationService notificationService) {
        this.loanRepository = loanRepository;
        this.subscriberRepository = subscriberRepository;
        this.notificationService = notificationService;
    }

    public LoanDTO createLoan(LoanDTO loanDTO) {
        Subscriber subscriber = subscriberRepository.findById(loanDTO.getSubscriberId())
                .orElseThrow(() -> new EntityNotFoundException("Subscriber not found"));

        Loan loan = new Loan();
        loan.setSubscriber(subscriber);
        loan.setAmount(loanDTO.getAmount());
        loan.setCurrency(loanDTO.getCurrency());

        Loan savedLoan = loanRepository.save(loan);

        LoanDTO savedLoanDTO = new LoanDTO();
        savedLoanDTO.setId(savedLoan.getId());
        savedLoanDTO.setSubscriberId(savedLoan.getSubscriber().getId());
        savedLoanDTO.setAmount(savedLoan.getAmount());
        savedLoanDTO.setCurrency(savedLoan.getCurrency());
        // Set other properties if needed

        // Send SMS notification to the subscriber
        notificationService.sendLoanNotification(savedLoanDTO);

        return savedLoanDTO;
    }

    // Other service methods for retrieving, updating, and deleting loans
}
