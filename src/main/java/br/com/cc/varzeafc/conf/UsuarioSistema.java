package br.com.cc.varzeafc.conf;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UsuarioSistema extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;

	public UsuarioSistema(String login, String senha, Collection<? extends GrantedAuthority> authorities, Integer id) {
		super(login, senha, authorities);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
