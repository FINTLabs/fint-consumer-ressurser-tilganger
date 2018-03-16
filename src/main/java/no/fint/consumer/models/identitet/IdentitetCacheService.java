package no.fint.consumer.models.identitet;

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

import no.fint.model.ressurser.tilganger.Identitet;
import no.fint.model.ressurser.tilganger.TilgangerActions;

@Slf4j
@Service
public class IdentitetCacheService extends CacheService<FintResource<Identitet>> {

    public static final String MODEL = Identitet.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public IdentitetCacheService() {
        super(MODEL, TilgangerActions.GET_ALL_IDENTITET);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_IDENTITET, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_IDENTITET)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    private void populateCache(String orgId) {
		log.info("Populating Identitet cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, TilgangerActions.GET_ALL_IDENTITET, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<FintResource<Identitet>> getIdentitetBySystemId(String orgId, String systemId) {
        return getOne(orgId, (fintResource) -> Optional
                .ofNullable(fintResource)
                .map(FintResource::getResource)
                .map(Identitet::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(id -> id.equals(systemId))
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        update(event, new TypeReference<List<FintResource<Identitet>>>() {
        });
    }
}
