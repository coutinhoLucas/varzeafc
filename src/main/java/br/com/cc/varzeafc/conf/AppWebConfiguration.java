package br.com.cc.varzeafc.conf;


import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import br.com.cc.varzeafc.formatter.CalendarFormatter;
import br.com.cc.varzeafc.formatter.CampeonatoFormatter;
import br.com.cc.varzeafc.formatter.GrupoFormatter;
import br.com.cc.varzeafc.formatter.JogadorFormatter;
import br.com.cc.varzeafc.formatter.PatrocinadorFormatter;
import br.com.cc.varzeafc.formatter.PosicaoFormatter;
import br.com.cc.varzeafc.formatter.TemporadaFormatter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "br.com.cc.varzeafc")
@EnableCaching
public class AppWebConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new PatrocinadorFormatter());
		registry.addFormatter(new GrupoFormatter());
		registry.addFormatter(new TemporadaFormatter());
		registry.addFormatter(new PosicaoFormatter());
		registry.addFormatter(new CampeonatoFormatter());
		registry.addFormatter(new JogadorFormatter());
		registry.addFormatter(new CalendarFormatter());
	}

	@Bean
	public CacheManager cacheManager() {
		final SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager
				.setCaches(Arrays.asList(new ConcurrentMapCache("patrocinadores"), new ConcurrentMapCache("campeonatos"),
						new ConcurrentMapCache("locaisPartida"),  new ConcurrentMapCache("posicoes"),  new ConcurrentMapCache("jogadores")));
		return cacheManager;
	}
}
