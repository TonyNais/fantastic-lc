package io.lending.controller;

import io.lending.dto.SubscriberDTO;
import io.lending.entity.Subscriber;
import io.lending.service.SubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscriber")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Operation(summary = "Create Subscriber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscriber Created Successfully"),
            @ApiResponse(responseCode = "504", description = "Bad Request, Parameters Missing")
    })
    @PostMapping
    public ResponseEntity<SubscriberDTO> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) {
        SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscriber);
    }

    @Operation(summary = "Get all subscribers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched Subscribers Successfully")
    })
    @GetMapping
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.getAllSubscribers();
        return ResponseEntity.ok(subscribers);
    }

    @Operation(summary = "Get subscriber by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriber found"),
            @ApiResponse(responseCode = "404", description = "Subscriber not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Subscriber> getSubscriberById(@PathVariable Long id) {
        Optional<Subscriber> optionalSubscriber = subscriberService.getSubscriberById(id);
        if (optionalSubscriber.isPresent()) {
            Subscriber subscriber = optionalSubscriber.get();
            return ResponseEntity.ok(subscriber);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update Subscriber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriber updated"),
            @ApiResponse(responseCode = "404", description = "Subscriber not found")
    })
    @PutMapping("/{subscriberId}")
    public ResponseEntity<?> updateSubscriber(@PathVariable Long subscriberId, @RequestBody SubscriberDTO subscriberDTO) {
        Optional<Subscriber>  updatedSubscriber = subscriberService.updateSubscriber(subscriberId,subscriberDTO);
        if (updatedSubscriber.isPresent()) {
            Subscriber subscriber = updatedSubscriber.get();
            return ResponseEntity.ok(subscriber);
        }
        return ResponseEntity.notFound().build();
    }
}
