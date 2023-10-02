package com.learning.customer.CustomerService;

import com.learning.amqp.RabbitMQMessageProducer;
import com.learning.clients.fraud.FraudCheckResponse;
import com.learning.clients.fraud.FraudClient;
import com.learning.clients.notification.NotificationClient;
import com.learning.clients.notification.NotificationRequest;
import com.learning.customer.CustomerRepository;
import com.learning.customer.model.Customer;
import com.learning.customer.model.CustomerRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer producer;
    //    private final RestTemplate restTemplate;
    private CustomerRepository customerRepository;

    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder().firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        //        todo: check if email valid
        //        todo: check if email not taken
        //        store customer in db
        customerRepository.saveAndFlush(customer);
        //        todo: check if fraudster
        /*FraudCheckResponse fraudCheckResponse = restTemplate.getForObject("http://localhost:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId());*/
//        After using SERVICE DISCOVERY
       /* FraudCheckResponse fraudCheckResponse = restTemplate.getForObject("http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId());*/

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalArgumentException("fraudster");
        }

        //        todo: send notification
        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to Leaning...",
                        customer.getFirstName())
        );
       /* notificationClient.sendNotification(
                notificationRequest
        );*/
//        ADD to QUEUE
        producer.publish(notificationRequest,"internal.exchange","internal.notification.routing-key");

    }
}
