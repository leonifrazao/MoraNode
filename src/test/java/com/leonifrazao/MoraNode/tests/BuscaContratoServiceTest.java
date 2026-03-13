package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.usecase.BuscaContratoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaContratoServiceTest {

    @Mock
    private ContratoRepositoryPort repositoryPort;

    @InjectMocks
    private BuscaContratoService service;

    private ContratoDomain criarContrato(Long id, Long imovelId) {
        return new ContratoDomain(
                id, imovelId, "Dono", "Inquilino",
                new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(12),
                new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO
        );
    }

    @Test
    void deveRetornarTodosContratos() {
        Long usuarioId = 100L;
        List<ContratoDomain> contratos = List.of(criarContrato(1L, 10L), criarContrato(2L, 20L));
        when(repositoryPort.buscar(usuarioId)).thenReturn(contratos);

        List<ContratoDomain> resultado = service.buscar(usuarioId);

        assertEquals(2, resultado.size());
        verify(repositoryPort).buscar(usuarioId);
    }

    @Test
    void deveBuscarContratoPorId() {
        Long usuarioId = 100L;
        ContratoDomain contrato = criarContrato(1L, 10L);
        when(repositoryPort.buscarPorId(1L, usuarioId)).thenReturn(contrato);

        ContratoDomain resultado = service.buscarPorId(1L, usuarioId);

        assertEquals(contrato, resultado);
        verify(repositoryPort).buscarPorId(1L, usuarioId);
    }

    @Test
    void deveBuscarContratosPorImovelId() {
        Long usuarioId = 100L;
        List<ContratoDomain> contratos = List.of(criarContrato(1L, 10L), criarContrato(2L, 10L));
        when(repositoryPort.buscarPorImovelId(10L, usuarioId)).thenReturn(contratos);

        List<ContratoDomain> resultado = service.buscarPorImovelId(10L, usuarioId);

        assertEquals(2, resultado.size());
        verify(repositoryPort).buscarPorImovelId(10L, usuarioId);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemContratos() {
        Long usuarioId = 100L;
        when(repositoryPort.buscar(usuarioId)).thenReturn(List.of());

        List<ContratoDomain> resultado = service.buscar(usuarioId);

        assertTrue(resultado.isEmpty());
    }
}
