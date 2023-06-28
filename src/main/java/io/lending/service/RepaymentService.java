package io.lending.service;

import io.lending.dto.RepaymentDTO;
import io.lending.entity.Loan;
import io.lending.entity.Repayment;
import io.lending.repository.RepaymentRepository;
import io.lending.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepaymentService {
    private final RepaymentRepository repaymentRepository;
    private final LoanService loanService;

    private final NotificationService notificationService;

    @Autowired
    public RepaymentService(RepaymentRepository repaymentRepository, LoanService loanService, NotificationService notificationService) {
        this.repaymentRepository = repaymentRepository;
        this.loanService = loanService;
        this.notificationService = notificationService;
    }

    public Repayment makeRepayment(Long loanId, RepaymentDTO repaymentDTO) throws CustomException {
        Loan loan = loanService.getLoanById(loanId);

        Repayment repayment = new Repayment();
        repayment.setLoan(loan);
        repayment.setAmount(repaymentDTO.getAmount());
        repayment.setRepaymentDate(LocalDateTime.now());

        repayment = repaymentRepository.save(repayment);

        // Update loan outstanding amount
        BigDecimal updatedOutstandingAmount = loan.getOutStandingBalance().subtract(repaymentDTO.getAmount());
        loan.setOutStandingBalance(updatedOutstandingAmount);

        // Check if the loan has been fully repaid
        if (updatedOutstandingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            notificationService.sendFullySettledLoanNotification(loan);
            loanService.markLoanAsFullyRepaid(loan);

        } else {
            //send notification
            notificationService.sendRepaymentNotification(loan.getSubscriber().getId(),repaymentDTO.getAmount());
            loanService.updateLoan(loan);
        }

        return repayment;
    }

    public List<Repayment> getAllRepayments() {
        return repaymentRepository.findAll();
    }
}
