package com.leonifrazao.MoraNode.infrastructure.adapter.out;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.database.SpringDataContratoRepository;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ContratoEntity;
import com.leonifrazao.MoraNode.infrastructure.mappers.ContratoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresContratoImovelAdapter implements ContratoRepositoryPort {

    private final SpringDataContratoRepository jpaRepository;

    @Override
    public List<ContratoDomain> buscar() {
        List<ContratoEntity> entidades = jpaRepository.findAll();
        return entidades.stream()
                .map(ContratoMapper::toDomain)
                .toList();
    }

    @Override
    public void salvar(ContratoDomain dominio) {
        jpaRepository.save(ContratoMapper.toEntity(dominio));
    }

    @Override
    public ContratoDomain buscarPorId(Long id){
        ContratoEntity entidade = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado com o ID: " + id));
        return ContratoMapper.toDomain(entidade);
    }

    @Override
    public List<ContratoDomain> buscarPorImovelId(Long imovelId) {
        return jpaRepository.findByImovelId(imovelId)
                .stream()
                .map(ContratoMapper::toDomain)
                .toList();

    }

    @Override
    public boolean existeContratoAtivoParaImovel(Long imovelId) {
        List<StatusContrato> statusQueTrancam = List.of(
                StatusContrato.ATIVO,
                StatusContrato.EM_DISPUTA
        );

        return jpaRepository.existsByImovelIdAndStatusContratoIn(imovelId, statusQueTrancam);
    }

    @Override
    public void atualizarStatus(Long id, StatusContrato novoStatus) {
        ContratoEntity entidade = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        entidade.setStatusContrato(novoStatus);

        jpaRepository.save(entidade);
    }
}
