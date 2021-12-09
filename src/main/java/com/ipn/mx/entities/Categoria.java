package com.ipn.mx.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Data
@NoArgsConstructor
@Entity
@Table(name = "Categoria")
public class Categoria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    @Column(name = "nombreCategoria", length = 50, nullable = false)
    private String nombreCategoria;

    @Column(name = "descripcionCategoria", length = 100, nullable = false)
    private String descripcionCategoria;
}
