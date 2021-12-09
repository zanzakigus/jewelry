package com.ipn.mx.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TransaccionTotal")
public class TransaccionTotal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransaccionTotal;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(nullable = false)
    private double total;
    @Column(nullable = false)
    private double descuentoTotal;
    @Column(nullable = false)
    private double netoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTipoPago")
    private TipoPago idTipoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    private Cliente idCliente;


    @PrePersist
    public void prePersist(){
        this.fecha= new Date();

    }
}
