package com.ipn.mx.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grafica implements Serializable {
    private String nombre;
    private int cantidad;

}
