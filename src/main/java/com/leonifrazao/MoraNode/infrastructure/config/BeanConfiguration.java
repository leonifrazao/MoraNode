package com.leonifrazao.MoraNode.infrastructure.config;

import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.NotificacaoImovelPort;
import com.leonifrazao.MoraNode.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public DeletaImovelService deletaImovelService(ImovelRepositoryPort repositoryPort, ContratoRepositoryPort contratoRepositoryPort) {
        return new DeletaImovelService(repositoryPort, contratoRepositoryPort);
    }

    @Bean
    public CadastrarImovelService cadastrarImovelService(ImovelRepositoryPort repositoryPort, ContratoRepositoryPort contratoRepositoryPort) {
        return new CadastrarImovelService(repositoryPort, contratoRepositoryPort);
    }

    @Bean
    public BuscaImovelService buscaIdImovelService(ImovelRepositoryPort repositoryPort) {
        return new BuscaImovelService(repositoryPort);
    }

    @Bean
    public CadastrarContratoService cadastrarContratoService(ContratoRepositoryPort repositoryPort, NotificacaoImovelPort notificacaoImovelPort, ImovelRepositoryPort imovelRepositoryPort) {
        return new CadastrarContratoService(repositoryPort, notificacaoImovelPort, imovelRepositoryPort);
    }

    @Bean
    public BuscaContratoService buscaContratoService(ContratoRepositoryPort repositoryPort) {
        return new BuscaContratoService(repositoryPort);
    }
}