package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.CategoriaDAO;
import com.ipn.mx.dao.ProductoDAO;
import com.ipn.mx.entities.Categoria;
import com.ipn.mx.entities.Producto;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ManagedBean(name = "categoriaMB")
@SessionScoped
public class CategoriaMB extends BaseBean implements Serializable {

    private final CategoriaDAO dao = new CategoriaDAO();

    private Categoria dto;
    private List<Categoria> listCategoria;
    private boolean exito;
    private boolean error;

    /**
     * Creates a new instance of CategoriaMB
     */
    public CategoriaMB() {

    }


    public Categoria getDto() {
        return dto;
    }

    public void setDto(Categoria dto) {
        this.dto = dto;
    }

    public List<Categoria> getListCategoria() {
        return listCategoria;
    }

    public void setListCategoria(List<Categoria> listCategoria) {
        this.listCategoria = listCategoria;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @PostConstruct
    public void init() {
        dto = new Categoria();
        listCategoria = new ArrayList<>();
        listCategoria = dao.readAll();
    }

    public String preparedAdd() {
        dto = new Categoria();
        setAccion(ACC_CREAR);
        return "/categoria/categoriaForm?faces-redirect=true";
    }

    public String preparedUpdate() {
        dto = new Categoria();
        setAccion(ACC_ACTUALIZAR);
        return "/categoria/categoriaForm?faces-redirect=true";
    }

    public String preparedIndex() {
        init();
        this.exito = false;
        this.error = false;
        return "/categoria/listadoCategorias?faces-redirect=true";
    }

    public String preparedRespIndex() {
        init();
        return "/categoria/listadoCategorias?faces-redirect=true";
    }

    public boolean validate() {
        // nos toca todas las reglas de validacion
        return dto.getNombreCategoria() != null && !Objects.equals(dto.getNombreCategoria(), "");
    }

    public String add() {
        boolean valido = validate();

        if (valido) {
            dao.create(dto);
            if (valido) {
                this.exito = true;
                this.error = false;
                return preparedRespIndex();
            } else {
                this.error = true;
                this.exito = false;
                return preparedAdd();
            }
        }
        this.exito = false;
        this.error = true;
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
}
