package br.com.cc.varzeafc.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import br.com.cc.varzeafc.models.Campeonato;

public class CampeonatoFormatter implements Formatter<Campeonato> {

	@Override
	public String print(Campeonato campeonato, Locale locale) {
		return campeonato.getId().toString();
	}

	@Override
	public Campeonato parse(String id, Locale locale) throws ParseException {
		Campeonato campeonato = new Campeonato();
		campeonato.setId(Integer.parseInt(id));
		return campeonato;
	}

}
