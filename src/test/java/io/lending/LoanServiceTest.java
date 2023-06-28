package io.lending;

import io.lending.entity.Loan;
import io.lending.entity.Subscriber;
import io.lending.repository.LoanRepository;
import io.lending.service.LoanService;
import io.lending.util.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void markLoanAsFullyRepaid_ShouldDeleteLoanFromRepository() {
        // Arrange
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOutStandingBalance(BigDecimal.ZERO);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Act
        loanService.markLoanAsFullyRepaid(loan);

        // Assert
        verify(loanRepository, times(1)).delete(loan);
    }

    @Test
    void updateLoan_ShouldSaveLoanToRepository() {
        // Arrange
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOutStandingBalance(BigDecimal.valueOf(500));

        when(loanRepository.save(loan)).thenReturn(loan);

        // Act
        loanService.updateLoan(loan);

        // Assert
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void getAllLoans_ShouldReturnListOfLoans() {
        // Arrange
        List<Loan> expectedLoans = new ArrayList<>();
        expectedLoans.add(new Loan(1L, new Subscriber() ,BigDecimal.valueOf(1000), BigDecimal.valueOf(1000),"KES"));
        expectedLoans.add(new Loan(2L, new Subscriber() ,BigDecimal.valueOf(2000), BigDecimal.valueOf(2000),"KES"));

        when(loanRepository.findAll()).thenReturn(expectedLoans);

        // Act
        List<Loan> actualLoans = loanService.getAllLoans();

        // Assert
        assertEquals(expectedLoans, actualLoans);
    }

    @Test
    void getLoanById_ExistingId_ShouldReturnLoan() throws CustomException {
        // Arrange
        Long loanId = 1L;
        Loan expectedLoan = new Loan(loanId, new Subscriber() ,BigDecimal.valueOf(2000), BigDecimal.valueOf(2000),"KES");

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(expectedLoan));

        // Act
        Loan actualLoan = loanService.getLoanById(loanId);

        // Assert
        assertEquals(expectedLoan, actualLoan);
    }

    @Test
    void getLoanById_NonExistingId_ShouldReturnEmptyOptional() throws CustomException {
        // Arrange
        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomException.class, () -> loanService.getLoanById(loanId));
    }
}
