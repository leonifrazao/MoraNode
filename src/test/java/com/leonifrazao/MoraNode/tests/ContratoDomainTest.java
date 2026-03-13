package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContratoDomainTest {

    private ContratoDomain criarContratoValido() {
        return new ContratoDomain(
                1L, 10L, "Dono", "Inquilino",
                new BigDecimal("1500.00"),
                LocalDate.now(), LocalDate.now().plusMonths(12),
                new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO
        );
    }

    @Test
    void deveCriarContratoDomainComSucesso() {
        ContratoDomain contrato = criarContratoValido();

        assertEquals(1L, contrato.getId());
        assertEquals(10L, contrato.getImovelId());
        assertEquals("Dono", contrato.getNomeDono());
        assertEquals("Inquilino", contrato.getNomeInquilino());
        assertEquals(StatusContrato.ATIVO, contrato.getStatusContrato());
        assertEquals(TipoContrato.ALUGUEL, contrato.getTipo());
    }

    @Test
    void deveLancarExcecaoParaNomedDonoVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new ContratoDomain(1L, 10L, "", "Inquilino",
                        new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(12),
                        new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO)
        );
    }

    @Test
    void deveLancarExcecaoParaNomeInquilinoVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new ContratoDomain(1L, 10L, "Dono", "",
                        new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(12),
                        new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO)
        );
    }

    @Test
    void deveLancarExcecaoParaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new ContratoDomain(1L, 10L, "Dono", "Inquilino",
                        new BigDecimal("-100"), LocalDate.now(), LocalDate.now().plusMonths(12),
                        new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO)
        );
    }

    @Test
    void deveLancarExcecaoParaTaxaJurosNegativa() {
        assertThrows(IllegalArgumentException.class, () ->
                new ContratoDomain(1L, 10L, "Dono", "Inquilino",
                        new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(12),
                        new BigDecimal("-1"), TipoContrato.ALUGUEL, StatusContrato.ATIVO)
        );
    }

    @Test
    void deveLancarExcecaoParaDataInicioNula() {
        assertThrows(IllegalArgumentException.class, () ->
                new ContratoDomain(1L, 10L, "Dono", "Inquilino",
                        new BigDecimal("1500"), null, LocalDate.now().plusMonths(12),
                        new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO)
        );
    }

    @Test
    void deveLancarExcecaoParaDataFimAntesDeDataInicio() {
        assertThrows(IllegalArgumentException.class, () ->
                new ContratoDomain(1L, 10L, "Dono", "Inquilino",
                        new BigDecimal("1500"), LocalDate.now(), LocalDate.now().minusDays(1),
                        new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO)
        );
    }

    @Test
    void devePermitirRenovacaoParaAluguelAtivo() {
        ContratoDomain contrato = criarContratoValido();

        assertTrue(contrato.isPodeRenovar());
    }

    @Test
    void naoDevePermitirRenovacaoParaVenda() {
        ContratoDomain contrato = new ContratoDomain(
                1L, 10L, "Dono", "Inquilino",
                new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(12),
                new BigDecimal("1.5"), TipoContrato.VENDA, StatusContrato.ATIVO
        );

        assertFalse(contrato.isPodeRenovar());
    }

    @Test
    void naoDevePermitirRenovacaoParaContratoFinalizado() {
        ContratoDomain contrato = new ContratoDomain(
                1L, 10L, "Dono", "Inquilino",
                new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(12),
                new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.FINALIZADO
        );

        assertFalse(contrato.isPodeRenovar());
    }
}
