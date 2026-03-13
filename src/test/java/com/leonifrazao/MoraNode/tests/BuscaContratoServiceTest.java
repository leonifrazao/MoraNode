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
        List<ContratoDomain> contratos = List.of(criarContrato(1L, 10L), criarContrato(2L, 20L));
        when(repositoryPort.buscar()).thenReturn(contratos);

        List<ContratoDomain> resultado = service.buscar();

        assertEquals(2, resultado.size());
        verify(repositoryPort).buscar();
    }

    @Test
    void deveBuscarContratoPorId() {
        ContratoDomain contrato = criarContrato(1L, 10L);
        when(repositoryPort.buscarPorId(1L)).thenReturn(contrato);

        ContratoDomain resultado = service.buscarPorId(1L);

        assertEquals(contrato, resultado);
        verify(repositoryPort).buscarPorId(1L);
    }

    @Test
    void deveBuscarContratosPorImovelId() {
        List<ContratoDomain> contratos = List.of(criarContrato(1L, 10L), criarContrato(2L, 10L));
        when(repositoryPort.buscarPorImovelId(10L)).thenReturn(contratos);

        List<ContratoDomain> resultado = service.buscarPorImovelId(10L);

        assertEquals(2, resultado.size());
        verify(repositoryPort).buscarPorImovelId(10L);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemContratos() {
        when(repositoryPort.buscar()).thenReturn(List.of());

        List<ContratoDomain> resultado = service.buscar();

        assertTrue(resultado.isEmpty());
    }
}
