package io.lending.service;

import io.lending.dto.LoanDTO;
import io.lending.entity.Loan;
import io.lending.entity.Subscriber;
import io.lending.repository.LoanRepository;
import io.lending.repository.SubscriberRepository;
import io.lending.util.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public LoanDTO createLoan(LoanDTO loanDTO) throws  CustomException {
        Subscriber subscriber = subscriberRepository.findById(loanDTO.getSubscriberId())
                .orElseThrow(() -> new CustomException("Subscriber "+loanDTO.getSubscriberId()+ " not found"));

        Loan loan = new Loan();
        loan.setSubscriber(subscriber);
        loan.setPrincipal(loanDTO.getAmount());
        loan.setOutStandingBalance(loanDTO.getAmount());
        loan.setCurrency(loanDTO.getCurrency());

        Loan savedLoan = loanRepository.save(loan);

        LoanDTO savedLoanDTO = new LoanDTO();
        savedLoanDTO.setId(savedLoan.getId());
        savedLoanDTO.setSubscriberId(savedLoan.getSubscriber().getId());
        savedLoanDTO.setAmount(savedLoan.getPrincipal());
        savedLoanDTO.setCurrency(savedLoan.getCurrency());

        // Send SMS notification to the subscriber
        notificationService.sendLoanNotification(savedLoanDTO);

        return savedLoanDTO;
    }

    public LoanDTO getLoanDTOById(Long loanId) throws CustomException {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomException("Loan "+loanId+" not found"));

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setId(loan.getId());
        loanDTO.setSubscriberId(loan.getSubscriber().getId());
        loanDTO.setAmount(loan.getPrincipal());
        loanDTO.setCurrency(loan.getCurrency());

        return loanDTO;
    }

    public Loan getLoanById(Long loanId) throws CustomException {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomException("Loan "+loanId+" not found"));
    }

    public void updateLoan(Loan loan) {
        loanRepository.save(loan);
    }

    public void deleteLoan(Long loanId) throws CustomException {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomException("Loan "+loanId+" not found"));

        loanRepository.delete(loan);
    }

    public void markLoanAsFullyRepaid(Loan loan) {
        // Sweep fully repaid loans
        loanRepository.delete(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
