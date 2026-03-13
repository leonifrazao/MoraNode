package com.leonifrazao.MoraNode.infrastructure.adapter.out;

import com.leonifrazao.MoraNode.domain.port.out.UsuarioRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.database.SpringDataUsuarioRepository;
import com.leonifrazao.MoraNode.infrastructure.database.entities.UsuarioEntity;
import com.leonifrazao.MoraNode.infrastructure.security.Papel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostgresUsuarioAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository jpaRepository;

    @Override
    public void salvar(String nome, String email, String senhaHash, String papel) {
        UsuarioEntity entidade = new UsuarioEntity();
        entidade.setNome(nome);
        entidade.setEmail(email);
        entidade.setSenha(senhaHash);
        entidade.setPapel(Papel.valueOf(papel));
        jpaRepository.save(entidade);
    }

    @Override
    public Optional<UsuarioBuscado> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(e -> new UsuarioBuscado(
                        e.getId(),
                        e.getNome(),
                        e.getEmail(),
                        e.getSenha(),
                        e.getPapel().name(),
                        e.isAtivo()
                ));
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
