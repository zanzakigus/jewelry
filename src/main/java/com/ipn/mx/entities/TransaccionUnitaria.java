package com.ipn.mx.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TransaccionUnitaria")
public class TransaccionUnitaria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransaccionUnitaria;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private double precioUnitario;
    @Column(nullable = false)
    private double monto;
    @Column(nullable = false)
    private double descuento;
    @Column(nullable = false)
    private double peso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTransaccionTotal")
    private TransaccionTotal idTransaccionTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto")
    private Producto idProducto;

    @Transient
    private Integer idPro;
}
