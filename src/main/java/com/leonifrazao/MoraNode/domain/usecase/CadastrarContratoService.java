package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.events.ImovelDesocupadoEvent;
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

    private void verificarSeExiste(Long id, Long usuarioId) {
        try {
            ImovelDomain dominioImovel = imovelRepositoryPort.buscarPorID(id, usuarioId);

            if (!dominioImovel.isDisponivel()) {
                throw new ImovelJaOcupadoException();
            }

        } catch (EntityNotFoundException e) {
            throw new SemImovelException("Imovel Não existe ou não te pertence!", e);
        }
    }

    private void dispararEventoOcupacao(ContratoDomain dominio) {
        var evento = new ImovelOcupadoEvent(
                dominio.getImovelId(),
                dominio.getId(),
                dominio.getTipo().name(),
                dominio.getUsuarioId()
        );
        notificacaoImovelPort.avisarImovelOcupado(evento);
    }

    @Override
    @Transactional
    public void cadastrar(ContratoDomain dominio, Long usuarioId) {

        verificarSeExiste(dominio.getImovelId(), usuarioId);

        dominio.setUsuarioId(usuarioId);
        repositoryPort.salvar(dominio);

        if (dominio.getStatusContrato() == StatusContrato.ATIVO ||
                dominio.getStatusContrato() == StatusContrato.EM_DISPUTA) {

            dispararEventoOcupacao(dominio);
        }

    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusContrato status, Long usuarioId) {
        ContratoDomain dominio = repositoryPort.buscarPorId(id, usuarioId);

        repositoryPort.atualizarStatus(id, status, usuarioId);


        if (status == StatusContrato.CANCELADO ||
                status == StatusContrato.FINALIZADO) {

            var evento = new ImovelDesocupadoEvent(
                    dominio.getImovelId(),
                    dominio.getId(),
                    dominio.getTipo().name(),
                    dominio.getUsuarioId()
            );

            notificacaoImovelPort.avisarImovelDesocupado(evento);
        } else if (status == StatusContrato.ATIVO ||
                status == StatusContrato.EM_DISPUTA) {

            dispararEventoOcupacao(dominio);
        }

    }
}
