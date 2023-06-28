package io.lending.controller;

import io.lending.dto.RepaymentDTO;
import io.lending.entity.Repayment;
import io.lending.service.RepaymentService;
import io.lending.util.CustomException;
import io.lending.util.ErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans/{loanId}/repayments")
public class RepaymentController {
    private final RepaymentService repaymentService;

    @Autowired
    public RepaymentController(RepaymentService repaymentService) {
        this.repaymentService = repaymentService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Repayment"),
            @ApiResponse(responseCode = "404", description = "Loan not found"),
            @ApiResponse(responseCode = "504", description = "Bad Request, Parameters Missing")
    })
    @PostMapping
    public ResponseEntity<?> makeRepayment(@PathVariable Long loanId, @RequestBody RepaymentDTO repaymentDTO) {
        try {
            Repayment repayment = repaymentService.makeRepayment(loanId, repaymentDTO);
            return ResponseEntity.ok(repayment);
        }
        catch (CustomException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("There was an error",e.getMessage()));
        }

    }
}
