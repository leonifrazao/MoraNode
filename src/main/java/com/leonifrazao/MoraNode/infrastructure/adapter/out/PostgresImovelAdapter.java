package com.leonifrazao.MoraNode.infrastructure.adapter.out;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.database.SpringDataImovelRepository;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ImovelEntity;
import com.leonifrazao.MoraNode.infrastructure.mappers.ImovelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresImovelAdapter implements ImovelRepositoryPort {

    private final SpringDataImovelRepository jpaRepository;

    @Override
    public void deletar(Long id, Long usuarioId) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void salvar(ImovelDomain imovel) {
        jpaRepository.save(ImovelMapper.fromDomain(imovel));
    }

    @Override
    public void editarPorID(Long id, ImovelDomain imovel, Long usuarioId) {
        imovel.setId(id);
        imovel.setUsuarioId(usuarioId);
        jpaRepository.save(ImovelMapper.fromDomain(imovel));
    }

    @Override
    public ImovelDomain buscarPorID(Long id, Long usuarioId) {
        ImovelEntity entidade = jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado ou você não tem acesso a ele: " + id));
        return ImovelMapper.toDomain(entidade);
    }

    @Override
    public List<ImovelDomain> buscar(Long usuarioId) {
        List<ImovelEntity> entidades = jpaRepository.findByUsuarioId(usuarioId);
        return entidades.stream()
                .map(ImovelMapper::toDomain)
                .toList();
    }
}
