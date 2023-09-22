package org.learning.customer.CustomerService;

import com.learning.clients.fraud.FraudCheckResponse;
import com.learning.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.learning.customer.CustomerRepository;
import org.learning.customer.model.Customer;
import org.learning.customer.model.CustomerRegistrationRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final FraudClient fraudClient;

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

    }
}
