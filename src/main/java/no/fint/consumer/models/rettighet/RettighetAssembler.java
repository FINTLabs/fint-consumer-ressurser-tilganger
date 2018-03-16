package no.fint.consumer.models.rettighet;

import no.fint.model.ressurser.tilganger.Rettighet;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class RettighetAssembler extends FintResourceAssembler<Rettighet> {

    public RettighetAssembler() {
        super(RettighetController.class);
    }


    @Override
    public FintResourceSupport assemble(Rettighet rettighet , FintResource<Rettighet> fintResource) {
        return createResourceWithId(rettighet.getSystemId().getIdentifikatorverdi(), fintResource, "systemid");
    }
    
    
    
}

