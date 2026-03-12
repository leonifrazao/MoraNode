package com.leonifrazao.MoraNode.domain.model;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImovelDomain {
    @Setter
    private Long id;
    private int valor;
    @Setter
    private boolean disponivel;
    private int metrosQuadrados;
    private String endereco;

    public ImovelDomain(int valor, String endereco, int metrosQuadrados, boolean disponivel) {
        setValor(valor);
        setEndereco(endereco);
        setMetrosQuadrados(metrosQuadrados);
        setDisponivel(disponivel);
    }

    public double calcularValorMetro() {
        return (double) this.valor / this.metrosQuadrados;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("Endereço invalido!");
        }
        this.endereco = endereco;
    }

    public void setMetrosQuadrados(int metrosQuadrados) {
        if (metrosQuadrados <= 0) {
            throw new IllegalArgumentException("Número de Metros Quadrados invalido.");
        }
        this.metrosQuadrados = metrosQuadrados;
    }

    public void setValor(int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor abaixo de 0 não permitido!");
        }
        this.valor = valor;
    }

}
