package io.lending;

import io.lending.dto.SubscriberDTO;
import io.lending.entity.Subscriber;
import io.lending.repository.SubscriberRepository;
import io.lending.service.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriberServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private SubscriberService subscriberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSubscribers_ShouldReturnListOfSubscribers() {
        // Arrange
        List<Subscriber> expectedSubscribers = new ArrayList<>();
        expectedSubscribers.add(new Subscriber(1L, "John Doe", "1234567890"));
        expectedSubscribers.add(new Subscriber(2L, "Jane Smith", "0987654321"));

        when(subscriberRepository.findAll()).thenReturn(expectedSubscribers);

        // Act
        List<Subscriber> actualSubscribers = subscriberService.getAllSubscribers();

        // Assert
        assertEquals(expectedSubscribers, actualSubscribers);
    }

    @Test
    void getSubscriberById_ExistingId_ShouldReturnSubscriber() {
        // Arrange
        Long subscriberId = 1L;
        Subscriber expectedSubscriber = new Subscriber(subscriberId, "John Doe", "1234567890");

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(expectedSubscriber));

        // Act
        Optional<Subscriber> actualSubscriber = subscriberService.getSubscriberById(subscriberId);

        // Assert
        assertTrue(actualSubscriber.isPresent());
        assertEquals(expectedSubscriber, actualSubscriber.get());
    }

    @Test
    void getSubscriberById_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        Long subscriberId = 1L;

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

        // Act
        Optional<Subscriber> actualSubscriber = subscriberService.getSubscriberById(subscriberId);

        // Assert
        assertFalse(actualSubscriber.isPresent());
    }

    @Test
    void createSubscriber_ValidSubscriber_ShouldSaveSubscriber() {
        // Arrange
        Subscriber subscriber = new Subscriber();
        subscriber.setId(1L);
        subscriber.setName("John Doe");
        subscriber.setMsisdn("1234567890");

        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setId(subscriber.getId());
        subscriberDTO.setName(subscriber.getName());
        subscriberDTO.setMsisdn(subscriber.getMsisdn());

        when(subscriberRepository.save(any(Subscriber.class))).thenAnswer(invocation -> {
            Subscriber savedSubscriber = invocation.getArgument(0);
                savedSubscriber.setId(1L);
                savedSubscriber.setName("John Doe");
                savedSubscriber.setMsisdn("1234567890");
                    return savedSubscriber;
                });

        // Act
        SubscriberDTO savedSubscriber = subscriberService.createSubscriber(subscriberDTO);

        // Assert
        assertNotNull(savedSubscriber.getId());
        assertEquals(subscriber.getName(), savedSubscriber.getName());
        assertEquals(subscriber.getMsisdn(), savedSubscriber.getMsisdn());
    }

    @Test
    void updateSubscriber_ExistingSubscriber_ShouldUpdateSubscriber() {
        // Arrange
        Long subscriberId = 1L;
        Subscriber existingSubscriber = new Subscriber(subscriberId, "John Doe", "1234567890");
        Subscriber updatedSubscriber = new Subscriber(subscriberId, "Jane Smith", "0987654321");
        SubscriberDTO updatedSubscriberDTO = new SubscriberDTO();
        updatedSubscriberDTO.setName(updatedSubscriber.getName());
        updatedSubscriberDTO.setMsisdn(updatedSubscriber.getMsisdn());

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(existingSubscriber));
        when(subscriberRepository.save(any(Subscriber.class))).thenAnswer(invocation -> updatedSubscriber);

        // Act
        Optional<Subscriber> savedSubscriber = subscriberService.updateSubscriber(subscriberId, updatedSubscriberDTO);

        // Assert
        assertTrue(savedSubscriber.isPresent());
        assertEquals(updatedSubscriber.getName(), savedSubscriber.get().getName());
        assertEquals(updatedSubscriber.getMsisdn(), savedSubscriber.get().getMsisdn());
        verify(subscriberRepository, times(1)).findById(subscriberId);
    }

    @Test
    void updateSubscriber_NonExistingSubscriber_ShouldThrowException() {
        // Arrange
        Long subscriberId = 1L;
        Subscriber updatedSubscriber = new Subscriber(subscriberId, "Jane Smith", "0987654321");
        SubscriberDTO updatedSubscriberDTO = new SubscriberDTO();
        updatedSubscriberDTO.setName(updatedSubscriber.getName());
        updatedSubscriberDTO.setMsisdn(updatedSubscriber.getMsisdn());

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

        // Act
        Optional<Subscriber> subscriber = subscriberService.updateSubscriber(subscriberId, updatedSubscriberDTO);

        assertFalse(subscriber.isPresent());
        verify(subscriberRepository, never()).save(updatedSubscriber);
    }

}
