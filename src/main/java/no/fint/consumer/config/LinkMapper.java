package no.fint.consumer.config;

import no.fint.consumer.utils.RestEndpoints;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.ressurser.tilganger.Identitet;
import no.fint.model.ressurser.tilganger.Rettighet;

public class LinkMapper {

	public static Map<String, String> linkMapper(String contextPath) {
		return ImmutableMap.<String,String>builder()
			.put(Identitet.class.getName(), contextPath + RestEndpoints.IDENTITET)
			.put(Rettighet.class.getName(), contextPath + RestEndpoints.RETTIGHET)
			.put(Personalressurs.class.getName(), "/administrasjon/personal/personalressurs")
			.build();
	}

}
