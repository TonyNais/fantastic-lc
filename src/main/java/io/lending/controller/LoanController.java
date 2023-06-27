package io.lending.controller;

import io.lending.dto.LoanDTO;
import io.lending.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Get loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan Created Successfully"),
            @ApiResponse(responseCode = "504", description = "Bad Request, Parameters Missing")
    })
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO) {
        LoanDTO createdLoan = loanService.createLoan(loanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
    }

    @Operation(summary = "Get loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan found"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanDTO> getLoan(@PathVariable Long loanId) {
        return null;
    }

    @Operation(summary = "Update loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan updated"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @PutMapping("/{loanId}")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable Long loanId, @RequestBody LoanDTO loanDTO) {
        return null;
    }

    @Operation(summary = "Delete loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loan deleted"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
        return null;
    }
}
