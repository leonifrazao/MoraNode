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
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(imovelRepositoryPort.buscarPorID(10L)).thenReturn(imovel);

        service.cadastrar(contrato);

        verify(repositoryPort).salvar(contrato);
        verify(notificacaoImovelPort).avisarImovelOcupado(any(ImovelOcupadoEvent.class));
    }

    @Test
    void deveCadastrarContratoFinalizadoSemDispararEvento() {
        ContratoDomain contrato = criarContrato(StatusContrato.FINALIZADO, TipoContrato.VENDA);
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(imovelRepositoryPort.buscarPorID(10L)).thenReturn(imovel);

        service.cadastrar(contrato);

        verify(repositoryPort).salvar(contrato);
        verify(notificacaoImovelPort, never()).avisarImovelOcupado(any());
    }

    @Test
    void deveLancarExcecaoQuandoImovelNaoExiste() {
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(imovelRepositoryPort.buscarPorID(10L)).thenThrow(new EntityNotFoundException());

        assertThrows(SemImovelException.class, () -> service.cadastrar(contrato));
    }

    @Test
    void deveLancarExcecaoQuandoImovelJaOcupado() {
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, false);
        when(imovelRepositoryPort.buscarPorID(10L)).thenReturn(imovel);

        assertThrows(ImovelJaOcupadoException.class, () -> service.cadastrar(contrato));
    }

    @Test
    void deveDispararEventoDesocupacaoAoCancelarContrato() {
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.CANCELADO);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.CANCELADO);
        verify(notificacaoImovelPort).avisarImovelDesocupado(any(ImovelDesocupadoEvent.class));
    }

    @Test
    void deveDispararEventoDesocupacaoAoFinalizarContrato() {
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.FINALIZADO);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.FINALIZADO);
        verify(notificacaoImovelPort).avisarImovelDesocupado(any(ImovelDesocupadoEvent.class));
    }

    @Test
    void deveDispararEventoOcupacaoAoReativarContrato() {
        ContratoDomain contrato = criarContrato(StatusContrato.CANCELADO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.ATIVO);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.ATIVO);
        verify(notificacaoImovelPort).avisarImovelOcupado(any(ImovelOcupadoEvent.class));
    }

    @Test
    void deveDispararEventoOcupacaoAoColocarEmDisputa() {
        ContratoDomain contrato = criarContrato(StatusContrato.ATIVO, TipoContrato.ALUGUEL);
        when(repositoryPort.buscarPorId(1L)).thenReturn(contrato);

        service.atualizarStatus(1L, StatusContrato.EM_DISPUTA);

        verify(repositoryPort).atualizarStatus(1L, StatusContrato.EM_DISPUTA);
        verify(notificacaoImovelPort).avisarImovelOcupado(any(ImovelOcupadoEvent.class));
    }
}
