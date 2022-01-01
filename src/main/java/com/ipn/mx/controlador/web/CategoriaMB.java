package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.CategoriaDAO;
import com.ipn.mx.dao.ProductoDAO;
import com.ipn.mx.entities.Categoria;
import com.ipn.mx.entities.Grafica;
import com.ipn.mx.entities.Producto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        setAccion(ACC_ACTUALIZAR);
        return "/categoria/categoriaForm?faces-redirect=true";
    }

    public String preparedIndex() {
        init();
        return "/categoria/listadoCategorias?faces-redirect=true";
    }

    public String preparedRespIndex() {
        init();
        return "/categoria/categoriaForm?faces-redirect=true";
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


    public void seleccionarCategoria(ActionEvent event) {
        String claveSeleccionada = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new Categoria();
        dto.setIdCategoria(Integer.parseInt(claveSeleccionada));

        try {
            dto = dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String obtenerGrafica(){
        JFreeChart grafica = ChartFactory.createPieChart("Productos por categoria", obtenerDatosGrafica(), true, true, Locale.getDefault());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String archivo = servletContext.getRealPath("/grafica.png");
        System.out.printf("ruta  :"+ archivo);
        try {
            ChartUtils.saveChartAsPNG(new File(archivo), grafica, 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/categoria/graficaCategoria?faces-redirect=true";
    }

    private PieDataset obtenerDatosGrafica() {
        DefaultPieDataset dpd = new DefaultPieDataset();
        List datos = dao.obtenerDatosGrafica();
        for (int i = 0; i < datos.size(); i++) {
            Grafica dto = (Grafica) datos.get(i);
            dpd.setValue(dto.getNombre(), dto.getCantidad());
        }
        return dpd;

    }


}
