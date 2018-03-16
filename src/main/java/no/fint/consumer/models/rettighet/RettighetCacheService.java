package no.fint.consumer.models.rettighet;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.relation.FintResource;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import no.fint.model.ressurser.tilganger.Rettighet;
import no.fint.model.ressurser.tilganger.TilgangerActions;

@Slf4j
@Service
public class RettighetCacheService extends CacheService<FintResource<Rettighet>> {

    public static final String MODEL = Rettighet.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public RettighetCacheService() {
        super(MODEL, TilgangerActions.GET_ALL_RETTIGHET);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_RETTIGHET, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_RETTIGHET)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    private void populateCache(String orgId) {
		log.info("Populating Rettighet cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, TilgangerActions.GET_ALL_RETTIGHET, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<FintResource<Rettighet>> getRettighetBySystemId(String orgId, String systemId) {
        return getOne(orgId, (fintResource) -> Optional
                .ofNullable(fintResource)
                .map(FintResource::getResource)
                .map(Rettighet::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(id -> id.equals(systemId))
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        update(event, new TypeReference<List<FintResource<Rettighet>>>() {
        });
    }
}
