/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ipn.mx.controlador.web;


import com.ipn.mx.dao.CategoriaDAO;
import com.ipn.mx.dao.ProductoDAO;
import com.ipn.mx.entities.Categoria;
import com.ipn.mx.entities.Producto;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zanzakigus
 */
@ManagedBean(name = "productoMB")
@SessionScoped
public class ProductoMB extends BaseBean implements Serializable {

    private final ProductoDAO dao = new ProductoDAO();
    private final CategoriaDAO cDao = new CategoriaDAO();

    private Producto dto;
    private List<Producto> listProducto;
    private List<Categoria> listCategoria;

    /**
     * Creates a new instance of ProductoMB
     */
    public ProductoMB() {

    }

    public List<Producto> getListProducto() {
        return listProducto;
    }

    public void setListProducto(List<Producto> listProducto) {
        this.listProducto = listProducto;
    }

    public List<Categoria> getListCategoria() {
        return listCategoria;
    }

    public void setListCategoria(List<Categoria> listCategoria) {
        this.listCategoria = listCategoria;
    }

    public Producto getDto() {
        return dto;
    }

    public void setDto(Producto dto) {
        this.dto = dto;
    }


    @PostConstruct
    public void init() {
        listProducto = new ArrayList<>();
        listProducto = dao.readAll();

        listCategoria = new ArrayList<>();
        listCategoria = cDao.readAll();

    }

    public String preparedAdd() {
        dto = new Producto();
        setAccion(ACC_CREAR);
        return "/producto/productoForm?faces-redirect=true";
    }

    public String preparedUpdate() {
        dto = new Producto();
        setAccion(ACC_ACTUALIZAR);
        return "/producto/productoForm?faces-redirect=true";
    }

    public String preparedIndex() {
        init();
        return "/producto/listadoProductos?faces-redirect=true";
    }

    public boolean validate() {
        boolean valido = true;

        // nos toca todas las reglas de validacion
        if (dto.getNombreProducto() == null) {
            valido = false;
        }
        return valido;
    }

    public String add() {
        boolean valido = validate();

        if (valido) {
            dao.create(dto);
            if (valido) {
                return preparedIndex();
            } else {
                return preparedAdd();
            }
        }
        return preparedAdd();
    }


    public String update() {
        boolean valido = validate();
        if (valido) {
            dao.update(dto);
            if (valido) {
                return preparedIndex();
            } else {
                return preparedUpdate();
            }
        }
        return preparedUpdate();
    }

    public String delete() {
        dao.delete(dto);
        return preparedIndex();
    }

    public void seleccionarProducto(ActionEvent event) {
        String claveSeleccionada = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new Producto();
        dto.setIdProducto(Integer.parseInt(claveSeleccionada));

        try {
            dto = dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
