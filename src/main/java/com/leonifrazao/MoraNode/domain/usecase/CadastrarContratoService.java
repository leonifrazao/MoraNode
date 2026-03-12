package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.events.ImovelOcupadoEvent;
import com.leonifrazao.MoraNode.domain.port.in.CadastrarContratoUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.NotificacaoImovelPort;
import com.leonifrazao.MoraNode.infrastructure.exceptions.ImovelJaOcupadoException;
import com.leonifrazao.MoraNode.infrastructure.exceptions.SemImovelException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CadastrarContratoService implements CadastrarContratoUseCase {

    private final ContratoRepositoryPort repositoryPort;
    private final NotificacaoImovelPort notificacaoImovelPort;
    private final ImovelRepositoryPort imovelRepositoryPort;

    @Override
    @Transactional
    public void cadastrar(ContratoDomain dominio) {

        try {
            ImovelDomain dominioImovel = imovelRepositoryPort.buscarPorID(dominio.getImovelId());

            if (!dominioImovel.isDisponivel()) {
                throw new ImovelJaOcupadoException();
            }

        } catch (EntityNotFoundException e) {
            throw new SemImovelException("Imovel Não existe!", e);
        }

        repositoryPort.salvar(dominio);

        var evento = new ImovelOcupadoEvent(
                dominio.getImovelId(),
                dominio.getId(),
                dominio.getTipo().name()
        );

        if (dominio.getStatusContrato() == StatusContrato.ATIVO ||
                dominio.getStatusContrato() == StatusContrato.EM_DISPUTA) {
            notificacaoImovelPort.avisarImovelOcupado(evento);
        }

    }
}
