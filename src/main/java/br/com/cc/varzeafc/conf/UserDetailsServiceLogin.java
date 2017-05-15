package br.com.cc.varzeafc.conf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.cc.varzeafc.daos.GrupoDAO;
import br.com.cc.varzeafc.daos.PermissaoDAO;
import br.com.cc.varzeafc.daos.UsuarioDAO;
import br.com.cc.varzeafc.models.Grupo;
import br.com.cc.varzeafc.models.Permissao;
import br.com.cc.varzeafc.models.Usuario;

@Component
public class UserDetailsServiceLogin implements UserDetailsService {
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private GrupoDAO grupoDAO;
	
	@Autowired
	private PermissaoDAO permissaoDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDAO.buscaPorLogin(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}

		return new UsuarioSistema(usuario.getLogin(), usuario.getSenha(), authorities(usuario), usuario.getId());
	}

	public Collection<? extends GrantedAuthority> authorities(Usuario usuario) {
		return authorities(grupoDAO.buscaGrupoUsuario(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(List<Grupo> grupos) {
		Collection<GrantedAuthority> auths = new ArrayList<>();

		for (Grupo grupo : grupos) {
			List<Permissao> lista = permissaoDAO.buscaPermissoesGrupo(grupo);
			for (Permissao permissao : lista) {
				System.out.println(permissao.getNome());
				auths.add(new SimpleGrantedAuthority("ROLE_" + permissao.getNome()));
			}
		}

		return auths;
	}

}
