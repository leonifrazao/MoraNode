package com.leonifrazao.MoraNode.infrastructure.adapter.out;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.database.SpringDataImovelRepository;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ImovelEntity;
import com.leonifrazao.MoraNode.infrastructure.mappers.ImovelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostgresImovelAdapter implements ImovelRepositoryPort {

    private final SpringDataImovelRepository jpaRepository;


    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void salvar(ImovelDomain imovel) {
        jpaRepository.save(ImovelMapper.fromDomain(imovel));
    }

    @Override
    public void editarPorID(Long id, ImovelDomain imovel) {
        imovel.setId(id);
        jpaRepository.save(ImovelMapper.fromDomain(imovel));
    }

    @Override
    public ImovelDomain buscarPorID(Long id) {
        ImovelEntity entidade = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com o ID: " + id));
        return ImovelMapper.toDomain(entidade);
    }

    @Override
    public List<ImovelDomain> buscar() {
        List<ImovelEntity> entidades = jpaRepository.findAll();
        return entidades.stream()
                .map(ImovelMapper::toDomain)
                .toList();
    }
}
