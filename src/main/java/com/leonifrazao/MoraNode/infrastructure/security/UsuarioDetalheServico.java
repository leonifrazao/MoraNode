package com.leonifrazao.MoraNode.infrastructure.security;

import com.leonifrazao.MoraNode.infrastructure.database.SpringDataUsuarioRepository;
import com.leonifrazao.MoraNode.infrastructure.database.entities.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioDetalheServico implements UserDetailsService {

    private final SpringDataUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return new UsuarioDetalhe(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.isAtivo(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPapel().name())));
    }
}
