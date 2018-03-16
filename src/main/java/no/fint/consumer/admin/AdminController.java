package no.fint.consumer.admin;

import no.fint.consumer.config.Constants;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.DefaultActions;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = RestEndpoints.ADMIN, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @GetMapping("/health")
    public ResponseEntity healthCheck(@RequestHeader(HeaderConstants.ORG_ID) String orgId,
                                      @RequestHeader(HeaderConstants.CLIENT) String client) {
        Event<Health> event = new Event<>(orgId, Constants.COMPONENT, DefaultActions.HEALTH, client);
        event.addData(new Health(Constants.COMPONENT_CONSUMER, HealthStatus.SENT_FROM_CONSUMER_TO_PROVIDER));
        Optional<Event<Health>> health = consumerEventUtil.healthCheck(event);

        if (health.isPresent()) {
            Event<Health> receivedHealth = health.get();
            receivedHealth.addData(new Health(Constants.COMPONENT_CONSUMER, HealthStatus.RECEIVED_IN_CONSUMER_FROM_PROVIDER));
            return ResponseEntity.ok(receivedHealth);
        } else {
            event.setMessage("No response from adapter");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(event);
        }
    }
}