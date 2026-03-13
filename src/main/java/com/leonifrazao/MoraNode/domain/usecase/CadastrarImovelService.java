package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.port.in.CadastrarImovelUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.exceptions.ImovelComContratoAtivo;
import com.leonifrazao.MoraNode.infrastructure.exceptions.SemImovelException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CadastrarImovelService implements CadastrarImovelUseCase {

    private final ImovelRepositoryPort repositoryPort;
    private final ContratoRepositoryPort contratoRepositoryPort;

    @Override
    public void cadastrar(ImovelDomain dominio) {

        repositoryPort.salvar(dominio);
    }

    @Override
    public void editarPorID(Long id, ImovelDomain dominio) {
        try {
            repositoryPort.buscarPorID(id);
        } catch (EntityNotFoundException e) {
            throw new SemImovelException("Usuário não possui residência vinculada", e);
        }

        List<ContratoDomain> contratos = contratoRepositoryPort.buscarPorImovelId(id);

        boolean temContratoAtivo = contratos.stream()
                .anyMatch(c -> c.getStatusContrato() == StatusContrato.ATIVO);

        if (temContratoAtivo) {
            throw new ImovelComContratoAtivo(
                    "Não é possível editar o imóvel. Existe um contrato ativo vinculado a ele."
            );
        }

        repositoryPort.editarPorID(id, dominio);
    }

    @Transactional
    @Override
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        ImovelDomain imovel = repositoryPort.buscarPorID(id);
        imovel.setDisponivel(disponivel);
        repositoryPort.salvar(imovel);
    }


}
