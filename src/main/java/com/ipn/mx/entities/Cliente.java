package com.ipn.mx.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Cliente")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCliente;
    @Column(length = 50, nullable = false)
    private String nombre;
    @Column(length = 50, nullable = false)
    private String paterno;
    @Column(length = 50, nullable = false)
    private String materno;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50, nullable = false)
    private String telefono;
    @Column(length = 100, nullable = false)
    private String calle;
    @Column(length = 50, nullable = false)
    private String colonia;
    @Column(nullable = false)
    private int cp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEstado")
    private Estado idEstado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCiudad")
    private Ciudad idCiudad;

}
