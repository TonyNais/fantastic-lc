package io.lending.service;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.lending.config.TwilioConfig;
import io.lending.dto.LoanDTO;
import io.lending.entity.Loan;
import io.lending.entity.Subscriber;
import io.lending.repository.SubscriberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class NotificationService {
    private final TwilioConfig twilioConfig;
    private final TwilioRestClient twilioRestClient;

    private final SubscriberRepository subscriberRepository;

    public NotificationService(TwilioConfig twilioConfig, SubscriberRepository subscriberRepository) {
        this.twilioConfig = twilioConfig;
        this.twilioRestClient = new TwilioRestClient.Builder(
                twilioConfig.getAccountSid(), twilioConfig.getAuthToken()).build();
        this.subscriberRepository = subscriberRepository;
    }

    public void sendLoanNotification(LoanDTO loanDTO) {
        String message = "You have been granted a loan of " + loanDTO.getAmount() + " "+loanDTO.getCurrency();
        sendSmsNotification(loanDTO.getSubscriberId(), message);
    }

    public void sendRepaymentNotification(Loan loan, BigDecimal amount) {
        String message = "A repayment of " + amount + " "+ loan.getCurrency() +" has been received.";
        sendSmsNotification(loan.getSubscriber().getId(), message);
    }

    private void sendSmsNotification(Long subscriberId, String message) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new EntityNotFoundException("Subscriber not found"));

        String phoneNumber = subscriber.getMsisdn();
        PhoneNumber to = new PhoneNumber("whatsapp:+254729965156");
//        PhoneNumber to = new PhoneNumber("+254729965156");
//        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());

        Message smsMessage = Message.creator(to, from, message)
                .create(twilioRestClient);
        log.info(smsMessage.getBody());

        log.info("SMS sent to " + phoneNumber + ": " + message);
    }

    public void sendFullySettledLoanNotification(Loan loan) {
        String message = "Your loan for " + loan.getCurrency() + " "+ loan.getPrincipal() +" has been fully settled.";
        sendSmsNotification(loan.getSubscriber().getId(), message);
    }
}
