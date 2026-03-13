package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import com.leonifrazao.MoraNode.domain.model.events.ImovelDesocupadoEvent;
import com.leonifrazao.MoraNode.domain.model.events.ImovelOcupadoEvent;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.NotificacaoImovelPort;
import com.leonifrazao.MoraNode.domain.usecase.CadastrarContratoService;
import com.leonifrazao.MoraNode.infrastructure.exceptions.ImovelJaOcupadoException;
import com.leonifrazao.MoraNode.infrastructure.exceptions.SemImovelException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarContratoServiceTest {

    @Mock
    private ContratoRepositoryPort repositoryPort;

    @Mock
    private NotificacaoImovelPort notificacaoImovelPort;

    @Mock
    private ImovelRepositoryPort imovelRepositoryPort;

    @InjectMocks
    private CadastrarContratoService service;

    private ContratoDomain criarContrato(StatusContrato status, TipoContrato tipo) {
        return new ContratoDomain(
                1L, 10L, "Dono", "Inquilino",
                new BigDecimal("1500.00"),
                LocalDate.now(), LocalDate.now().plusMonths(12),
                new BigDecimal("1.5"), tipo, status
        );
    }

    @Test
    void deveCadastrarContratoAtivoEDispararEvento() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(imovelRepositoryPort.buscarPorID(10L, usuarioId)).thenReturn(imovel);

        service.cadastrar(contrato, usuarioId);

        verify(repositoryPort).salvar(contrato);
        verify(notificacaoImovelPort).avisarImovelOcupado(any(ImovelOcupadoEvent.class));
    }

    @Test
    void deveCadastrarContratoFinalizadoSemDispararEvento() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.FINALIZADO, TipoContrato.VENDA);
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(imovelRepositoryPort.buscarPorID(10L, usuarioId)).thenReturn(imovel);

        service.cadastrar(contrato, usuarioId);

        verify(repositoryPort).salvar(contrato);
        verify(notificacaoImovelPort, never()).avisarImovelOcupado(any());
    }

    @Test
    void deveLancarExcecaoQuandoImovelNaoExiste() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(imovelRepositoryPort.buscarPorID(10L, usuarioId)).thenThrow(new EntityNotFoundException());

        assertThrows(SemImovelException.class, () -> service.cadastrar(contrato, usuarioId));
    }

    @Test
    void deveLancarExcecaoQuandoImovelJaOcupado() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, false);
        when(imovelRepositoryPort.buscarPorID(10L, usuarioId)).thenReturn(imovel);

        assertThrows(ImovelJaOcupadoException.class, () -> service.cadastrar(contrato, usuarioId));
    }

    @Test
    void deveDispararEventoDesocupacaoAoCancelarContrato() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L, usuarioId)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.CANCELADO, usuarioId);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.CANCELADO, usuarioId);
        verify(notificacaoImovelPort).avisarImovelDesocupado(any(ImovelDesocupadoEvent.class));
    }

    @Test
    void deveDispararEventoDesocupacaoAoFinalizarContrato() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L, usuarioId)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.FINALIZADO, usuarioId);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.FINALIZADO, usuarioId);
        verify(notificacaoImovelPort).avisarImovelDesocupado(any(ImovelDesocupadoEvent.class));
    }

    @Test
    void deveDispararEventoOcupacaoAoReativarContrato() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.CANCELADO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L, usuarioId)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.ATIVO, usuarioId);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.ATIVO, usuarioId);
        verify(notificacaoImovelPort).avisarImovelOcupado(any(ImovelOcupadoEvent.class));
    }

    @Test
    void deveDispararEventoOcupacaoAoColocarEmDisputa() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L, usuarioId)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.EM_DISPUTA, usuarioId);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.EM_DISPUTA, usuarioId);
        verify(notificacaoImovelPort).avisarImovelOcupado(any(ImovelOcupadoEvent.class));
    }
}
