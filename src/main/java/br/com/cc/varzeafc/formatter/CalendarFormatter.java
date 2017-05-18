package br.com.cc.varzeafc.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.springframework.format.Formatter;


public class CalendarFormatter implements Formatter<Calendar>{

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public String print(Calendar calendar, Locale locale) {
		return dateFormat.format(calendar.getTime());
	}

	@Override
	public Calendar parse(String text, Locale locale) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(text));
		return calendar;
	}
}
