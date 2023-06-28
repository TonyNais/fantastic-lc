package io.lending.service;

import io.lending.dto.SubscriberDTO;
import io.lending.entity.Subscriber;
import io.lending.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(subscriberDTO.getMsisdn());
        subscriber.setName(subscriberDTO.getName());

        Subscriber savedSubscriber = subscriberRepository.save(subscriber);

        SubscriberDTO savedSubscriberDTO = new SubscriberDTO();
        savedSubscriberDTO.setId(savedSubscriber.getId());
        savedSubscriberDTO.setName(savedSubscriber.getName());
        savedSubscriberDTO.setMsisdn(savedSubscriber.getMsisdn());

        return savedSubscriberDTO;
    }

    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    public Optional<Subscriber> getSubscriberById(Long id) {
        return subscriberRepository.findById(id);
    }

    public Optional<Subscriber> updateSubscriber(Long id, SubscriberDTO subscriberDTO) {
        Optional<Subscriber> optionalSubscriber = subscriberRepository.findById(id);
        if (optionalSubscriber.isPresent()) {
            Subscriber subscriber = optionalSubscriber.get();
            subscriber.setMsisdn(subscriberDTO.getMsisdn());
            subscriber.setName(subscriberDTO.getName());
            return Optional.of(subscriberRepository.save(subscriber));
        }
        return Optional.empty();
    }

    public boolean deleteSubscriber(Long id) {
        Optional<Subscriber> optionalSubscriber = subscriberRepository.findById(id);
        if (optionalSubscriber.isPresent()) {
            subscriberRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
