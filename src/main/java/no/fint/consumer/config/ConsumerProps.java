package no.fint.consumer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ConsumerProps {
    
    @Value("${fint.consumer.override-org-id:false}")
    private boolean overrideOrgId;

    @Value("${fint.consumer.default-client:FINT}")
    private String defaultClient;

    @Value("${fint.consumer.default-org-id:fint.no}")
    private String defaultOrgId;
    
    @Value("${fint.events.orgIds:fint.no}")
    private String[] orgs;

    
    public static final String CACHE_INITIALDELAY_IDENTITET = "${fint.consumer.cache.initialDelay.identitet:60000}";
    public static final String CACHE_FIXEDRATE_IDENTITET = "${fint.consumer.cache.fixedRate.identitet:900000}";
    
    public static final String CACHE_INITIALDELAY_RETTIGHET = "${fint.consumer.cache.initialDelay.rettighet:70000}";
    public static final String CACHE_FIXEDRATE_RETTIGHET = "${fint.consumer.cache.fixedRate.rettighet:900000}";
    

}
