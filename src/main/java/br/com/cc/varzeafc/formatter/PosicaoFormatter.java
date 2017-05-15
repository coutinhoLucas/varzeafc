package br.com.cc.varzeafc.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import br.com.cc.varzeafc.models.Posicao;

public class PosicaoFormatter implements Formatter<Posicao>{

	@Override
	public String print(Posicao posicao, Locale locale) {
		return posicao.getId().toString();
	}

	@Override
	public Posicao parse(String id, Locale locale) throws ParseException {
		Posicao posicao = new Posicao();
		posicao.setId(Integer.parseInt(id));
		return posicao;
	}

}
