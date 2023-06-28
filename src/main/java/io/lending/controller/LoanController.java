package io.lending.controller;

import io.lending.dto.LoanDTO;
import io.lending.service.LoanService;
import io.lending.util.CustomException;
import io.lending.util.ErrorResponse;
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

    @Operation(summary = "Create Loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan Created Successfully"),
            @ApiResponse(responseCode = "404", description = "Precondition failed, Provided subscriber not found"),
            @ApiResponse(responseCode = "504", description = "Bad Request, Parameters Missing")
    })
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanDTO loanDTO) {
        try {

            LoanDTO createdLoan = loanService.createLoan(loanDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
        }catch (CustomException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("There was an error",e.getMessage()));
        }
    }

    @Operation(summary = "Get loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan found"),
            @ApiResponse(responseCode = "404", description = "Loan not found"),
            @ApiResponse(responseCode = "504", description = "Bad Request, Parameters Missing")
    })
    @GetMapping("/{loanId}")
    public ResponseEntity<?> getLoan(@PathVariable Long loanId) {
        try {
            LoanDTO createdLoan = loanService.getLoanDTOById(loanId);
            return ResponseEntity.status(HttpStatus.OK).body(createdLoan);
        }catch (CustomException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("There was an error",e.getMessage()));
        }
    }

//    @Operation(summary = "Update loan")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Loan updated"),
//            @ApiResponse(responseCode = "404", description = "Loan not found")
//    })
//    @PutMapping("/{loanId}")
//    public ResponseEntity<?> updateLoan(@PathVariable Long loanId, @RequestBody LoanDTO loanDTO) {
//        try {
//            LoanDTO createdLoan = loanService.updateLoan(loanId,loanDTO);
//            return ResponseEntity.status(HttpStatus.OK).body(createdLoan);
//        }catch (CustomException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("There was an error",e.getMessage()));
//        }
//    }

//    @Operation(summary = "Delete loan")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Loan deleted"),
//            @ApiResponse(responseCode = "404", description = "Loan not found")
//    })
//    @DeleteMapping("/{loanId}")
//    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
//        return null;
//    }
}
