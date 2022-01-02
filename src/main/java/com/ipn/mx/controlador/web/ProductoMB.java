/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ipn.mx.controlador.web;


import com.ipn.mx.dao.CategoriaDAO;
import com.ipn.mx.dao.ProductoDAO;
import com.ipn.mx.entities.Categoria;
import com.ipn.mx.entities.Grafica;
import com.ipn.mx.entities.Producto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        setAccion(ACC_ACTUALIZAR);
        dto.setIdCat(dto.getIdCategoria().getIdCategoria());
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
            Categoria cat = new  Categoria();
            cat.setIdCategoria(dto.getIdCat());
            cat = cDao.read(cat);
            dto.setIdCategoria(cat);
            dao.create(dto);
            if (valido) {
                System.out.print("si toy aqui222222");
                return preparedIndex();
            } else {
                System.out.print("si toy aqui333333");
                return preparedAdd();
            }
        }
        System.out.print("si toy aqui");
        return preparedAdd();
    }


    public String update() {
        boolean valido = validate();
        if (valido) {
            Categoria cat = new  Categoria();
            cat.setIdCategoria(dto.getIdCat());
            cat = cDao.read(cat);
            dto.setIdCategoria(cat);
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
        return "/producto/graficaProducto?faces-redirect=true";
    }

    private PieDataset obtenerDatosGrafica() {
        DefaultPieDataset dpd = new DefaultPieDataset();
        List datos = cDao.obtenerDatosGrafica();
        for (int i = 0; i < datos.size(); i++) {
            Grafica dto = (Grafica) datos.get(i);
            dpd.setValue(dto.getNombre(), dto.getCantidad());
        }
        return dpd;

    }

    public void mostrarReporte() {


        try {
            Session s  = (new Configuration().configure().buildSessionFactory()).openSession();
            SessionImpl sessionImpl = (SessionImpl) s;
            Connection conn = sessionImpl.connection();
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            File report =  new File(servletContext.getRealPath("/reports/reportProductos.jasper"));
            byte[] b = JasperRunManager.runReportToPdf(report.getPath(),null, conn);
            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength((int)b.length);
            String repor ="reportw";

            //Inline
            //ec.setResponseHeader("Content-Disposition", "inline; filename=\"" + repor + "\"");

            //Attach for Browser
            //ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + report.getName() + "\"");

            OutputStream sos = ec.getResponseOutputStream();
            sos.write(b,0, b.length);
            sos.flush();
            sos.close();
            fc.responseComplete();
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }


    }

}
