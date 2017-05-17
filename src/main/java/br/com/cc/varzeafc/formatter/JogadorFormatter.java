package br.com.cc.varzeafc.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import br.com.cc.varzeafc.models.Jogador;

public class JogadorFormatter implements Formatter<Jogador> {

	@Override
	public String print(Jogador jogador, Locale locale) {
		return jogador.getId().toString();
	}

	@Override
	public Jogador parse(String id, Locale locale) throws ParseException {
		Jogador jogador = new Jogador();
		jogador.setId(Integer.parseInt(id));
		return jogador;
	}

}
