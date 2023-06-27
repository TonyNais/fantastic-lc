package io.lending.service;

import io.lending.dto.SubscriberDTO;
import io.lending.entity.Subscriber;
import io.lending.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(subscriberDTO.getMsisdn());
        // Set other properties if needed

        Subscriber savedSubscriber = subscriberRepository.save(subscriber);

        SubscriberDTO savedSubscriberDTO = new SubscriberDTO();
        savedSubscriberDTO.setId(savedSubscriber.getId());
        savedSubscriberDTO.setMsisdn(savedSubscriber.getMsisdn());
        // Set other properties if needed

        return savedSubscriberDTO;
    }

    // Other service methods for retrieving, updating, and deleting subscribers
}
