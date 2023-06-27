package io.lending.controller;

import io.lending.dto.SubscriberDTO;
import io.lending.service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriber")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/create")
    public ResponseEntity<SubscriberDTO> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) {
        SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscriber);
    }

    // Other controller methods for retrieving, updating, and deleting subscribers
}
