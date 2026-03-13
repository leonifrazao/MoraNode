package com.leonifrazao.MoraNode.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_imoveis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImovelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    private int valor;

    @Column(length = 500)
    private String endereco;
    private int metrosQuadrados;
    private boolean disponivel;
}
