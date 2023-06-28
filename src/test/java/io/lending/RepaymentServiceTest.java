package io.lending;

import io.lending.dto.RepaymentDTO;
import io.lending.entity.Loan;
import io.lending.entity.Repayment;
import io.lending.entity.Subscriber;
import io.lending.repository.RepaymentRepository;
import io.lending.service.LoanService;
import io.lending.service.NotificationService;
import io.lending.service.RepaymentService;
import io.lending.util.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepaymentServiceTest {

    @Mock
    private RepaymentRepository repaymentRepository;

    @Mock
    private LoanService loanService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RepaymentService repaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makeRepayment_ShouldSaveRepaymentAndUpdateLoan() throws CustomException {
        // Arrange
        Long loanId = 1L;
        BigDecimal repaymentAmount = BigDecimal.valueOf(100);
        Loan loan = new Loan();
        loan.setId(loanId);
        loan.setOutStandingBalance(BigDecimal.valueOf(500));

        RepaymentDTO repaymentDTO = new RepaymentDTO();
        repaymentDTO.setAmount(repaymentAmount);

        when(loanService.getLoanById(loanId)).thenReturn(loan);
        when(repaymentRepository.save(any(Repayment.class))).thenAnswer(invocation -> {
            Repayment savedRepayment = invocation.getArgument(0);
            savedRepayment.setId(1L);
            return savedRepayment;
        });

        // Act
        Repayment repayment = repaymentService.makeRepayment(loanId, repaymentDTO);

        // Assert
        verify(loanService, times(1)).updateLoan(loan);
        verify(repaymentRepository, times(1)).save(any(Repayment.class));
    }

    @Test
    void makeRepayment_ExistingLoanId_ShouldSaveRepaymentAndUpdateLoan() throws CustomException {
        // Arrange
        Long loanId = 1L;
        BigDecimal repaymentAmount = BigDecimal.valueOf(100);
        Loan loan = new Loan(loanId, new Subscriber() ,BigDecimal.valueOf(500), BigDecimal.valueOf(500),"KES");

        RepaymentDTO repaymentDTO = new RepaymentDTO();
        repaymentDTO.setAmount(repaymentAmount);

        when(loanService.getLoanById(loanId)).thenReturn(loan);
        when(repaymentRepository.save(any(Repayment.class))).thenAnswer(invocation -> {
            Repayment savedRepayment = invocation.getArgument(0);
            savedRepayment.setId(1L);
            return savedRepayment;
        });

        // Act
        Repayment repayment = repaymentService.makeRepayment(loanId, repaymentDTO);

        // Assert
        verify(loanService, times(1)).updateLoan(loan);
        verify(repaymentRepository, times(1)).save(any(Repayment.class));
        assertEquals(repaymentAmount, repayment.getAmount());
        assertNotNull(repayment.getRepaymentDate());
    }

    @Test
    void makeRepayment_NonExistingLoanId_ShouldThrowException() throws CustomException {
        // Arrange
        Long loanId = 1L;
        BigDecimal repaymentAmount = BigDecimal.valueOf(100);

        RepaymentDTO repaymentDTO = new RepaymentDTO();
        repaymentDTO.setAmount(repaymentAmount);

        when(loanService.getLoanById(loanId)).thenThrow(CustomException.class);

        // Act & Assert
        assertThrows(CustomException.class, () -> repaymentService.makeRepayment(loanId, repaymentDTO));
        verify(loanService, never()).updateLoan(any(Loan.class));
        verify(repaymentRepository, never()).save(any(Repayment.class));
    }
}

