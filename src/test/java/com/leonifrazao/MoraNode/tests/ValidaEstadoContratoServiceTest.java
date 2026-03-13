package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.usecase.ValidaEstadoContratoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidaEstadoContratoServiceTest {

    @Mock
    private ContratoRepositoryPort repositoryPort;

    @InjectMocks
    private ValidaEstadoContratoService service;

    @Test
    void deveRetornarTrueQuandoExisteContratoAtivo() {
        when(repositoryPort.existeContratoAtivoParaImovel(1L)).thenReturn(true);

        assertTrue(service.existeContratoAtivoParaImovel(1L));
    }

    @Test
    void deveRetornarFalseQuandoNaoExisteContratoAtivo() {
        when(repositoryPort.existeContratoAtivoParaImovel(1L)).thenReturn(false);

        assertFalse(service.existeContratoAtivoParaImovel(1L));
    }
}
