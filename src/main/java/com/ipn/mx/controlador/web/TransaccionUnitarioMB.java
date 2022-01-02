package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.ProductoDAO;
import com.ipn.mx.entities.Producto;
import com.ipn.mx.entities.TransaccionTotal;
import com.ipn.mx.entities.TransaccionUnitaria;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "transaccionUnitariaMB")
@SessionScoped
public class TransaccionUnitarioMB extends BaseBean implements Serializable {

    private List<TransaccionUnitaria> listTransaccionUnitaria;
    private List<Producto> listProducto;

    private ProductoDAO pDao = new ProductoDAO();
    private TransaccionUnitaria dto;



    /**
     * Creates a new instance of TransaccionMB
     */
    public TransaccionUnitarioMB() {

    }

    public List<TransaccionUnitaria> getListTransaccionUnitaria() {
        return listTransaccionUnitaria;
    }

    public void setListTransaccionUnitaria(List<TransaccionUnitaria> listTransaccionUnitaria) {
        this.listTransaccionUnitaria = listTransaccionUnitaria;
    }

    public void init(){
        listTransaccionUnitaria = new ArrayList<>();
    }

    public List<Producto> getListProducto() {
        return listProducto;
    }

    public void setListProducto(List<Producto> listProducto) {
        this.listProducto = listProducto;
    }

    public TransaccionUnitaria getDto() {
        return dto;
    }

    public void setDto(TransaccionUnitaria dto) {
        this.dto = dto;
    }

    public String preparedAdd() {
        dto = new TransaccionUnitaria();
        listProducto = new ArrayList<>();
        listProducto = pDao.readAll();
        setAccion(ACC_CREAR);
        return "/transaccion/transaccionUnitariaForm?faces-redirect=true";
    }

}

