package no.fint.consumer.models.identitet;

import no.fint.model.ressurser.tilganger.Identitet;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class IdentitetAssembler extends FintResourceAssembler<Identitet> {

    public IdentitetAssembler() {
        super(IdentitetController.class);
    }


    @Override
    public FintResourceSupport assemble(Identitet identitet , FintResource<Identitet> fintResource) {
        return createResourceWithId(identitet.getSystemId().getIdentifikatorverdi(), fintResource, "systemid");
    }
    
    
    
}

