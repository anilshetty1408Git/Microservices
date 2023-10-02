package com.learning.notification;

import com.learning.amqp.RabbitMQMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.learning.notification", "com.learning.amqp"})
//@EnableEurekaClient
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

   /* @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer, NotificationConfig notificationConfig) {
        return args -> {
            producer.publish(new Person("Anil",25), notificationConfig.getInternalExchange(), notificationConfig.getInternalNotificationRoutingKey());
        };
    }*/

   /* record Person(String name,int age){}*/
}
