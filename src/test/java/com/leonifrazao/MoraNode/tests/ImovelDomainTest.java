package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImovelDomainTest {

    @Test
    void deveCriarImovelDomainComSucesso() {
        ImovelDomain imovel = new ImovelDomain(1500, "Rua A, 123", 50, true);

        assertEquals(1500, imovel.getValor());
        assertEquals("Rua A, 123", imovel.getEndereco());
        assertEquals(50, imovel.getMetrosQuadrados());
        assertTrue(imovel.isDisponivel());
    }

    @Test
    void deveCalcularValorPorMetroQuadrado() {
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);

        assertEquals(20.0, imovel.calcularValorMetro());
    }

    @Test
    void deveLancarExcecaoParaEnderecoVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImovelDomain(1000, "", 50, true)
        );
    }

    @Test
    void deveLancarExcecaoParaEnderecoNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImovelDomain(1000, null, 50, true)
        );
    }

    @Test
    void deveLancarExcecaoParaMetrosQuadradosZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImovelDomain(1000, "Rua A", 0, true)
        );
    }

    @Test
    void deveLancarExcecaoParaMetrosQuadradosNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImovelDomain(1000, "Rua A", -10, true)
        );
    }

    @Test
    void deveLancarExcecaoParaValorZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImovelDomain(0, "Rua A", 50, true)
        );
    }

    @Test
    void deveLancarExcecaoParaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImovelDomain(-500, "Rua A", 50, true)
        );
    }

    @Test
    void deveAlterarDisponibilidade() {
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);

        imovel.setDisponivel(false);

        assertFalse(imovel.isDisponivel());
    }
}
