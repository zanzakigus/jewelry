package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.CiudadDAO;
import com.ipn.mx.dao.ClienteDAO;
import com.ipn.mx.dao.EstadoDAO;
import com.ipn.mx.entities.*;
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
@ManagedBean(name = "clienteMB")
@SessionScoped
public class ClienteMB extends BaseBean implements Serializable {

    private final ClienteDAO dao = new ClienteDAO();
    private final CiudadDAO cDao = new CiudadDAO();
    private final EstadoDAO eDao = new EstadoDAO();

    private Cliente dto;
    private List<Cliente> listCliente;
    private List<Ciudad> listCiudad;
    private List<Estado> listEstado;

    /**
     * Creates a new instance of ClienteMB
     */
    public ClienteMB() {

    }

    public List<Cliente> getListCliente() {
        return listCliente;
    }

    public void setListCliente(List<Cliente> listCliente) {
        this.listCliente = listCliente;
    }

    public List<Ciudad> getListCiudad() {
        return listCiudad;
    }

    public void setListCiudad(List<Ciudad> listCiudad) {
        this.listCiudad = listCiudad;
    }

    public Cliente getDto() {
        return dto;
    }

    public void setDto(Cliente dto) {
        this.dto = dto;
    }

    public List<Estado> getListEstado() {
        return listEstado;
    }

    public void setListEstado(List<Estado> listEstado) {
        this.listEstado = listEstado;
    }

    @PostConstruct
    public void init() {
        listCliente = new ArrayList<>();
        listCliente = dao.readAll();

        listCiudad = new ArrayList<>();
        listCiudad = cDao.readAll();

        listEstado = new ArrayList<>();
        listEstado = eDao.readAll();

    }

    public String preparedAdd() {
        dto = new Cliente();
        setAccion(ACC_CREAR);
        return "/cliente/clienteForm?faces-redirect=true";
    }

    public String preparedUpdate() {
        setAccion(ACC_ACTUALIZAR);
        dto.setIdDCi(dto.getIdCiudad().getIdCiudad());
        dto.setIdDEs(dto.getIdEstado().getIdEstado());
        return "/cliente/clienteForm?faces-redirect=true";
    }

    public String preparedIndex() {
        init();
        return "/cliente/listadoClientes?faces-redirect=true";
    }

    public boolean validate() {
        boolean valido = true;

        // nos toca todas las reglas de validacion
        if (dto.getNombre() == null) {
            valido = false;
        }
        return valido;
    }

    public String add() {
        boolean valido = validate();
        if (valido) {
            Ciudad cat = new  Ciudad();
            cat.setIdCiudad(dto.getIdDCi());
            cat = cDao.read(cat);
            dto.setIdCiudad(cat);

            Estado est = new  Estado();
            est.setIdEstado(dto.getIdDEs());
            est = eDao.read(est);
            dto.setIdEstado(est);

            dao.create(dto);
            if (valido) {
                return preparedIndex();
            } else {
                return preparedAdd();
            }
        }
        System.out.print("si toy aqui");
        return preparedAdd();
    }


    public String update() {
        boolean valido = validate();
        if (valido) {
            Ciudad cat = new  Ciudad();
            cat.setIdCiudad(dto.getIdDCi());
            cat = cDao.read(cat);
            dto.setIdCiudad(cat);

            Estado est = new  Estado();
            est.setIdEstado(dto.getIdDEs());
            est = eDao.read(est);
            dto.setIdEstado(est);

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

    public void seleccionarCliente(ActionEvent event) {
        String claveSeleccionada = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new Cliente();
        dto.setIdCliente(Integer.parseInt(claveSeleccionada));

        try {
            dto = dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String obtenerGrafica(){
        JFreeChart grafica = ChartFactory.createPieChart("Clientes por ciudad", obtenerDatosGrafica(), true, true, Locale.getDefault());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String archivo = servletContext.getRealPath("/grafica.png");
        System.out.printf("ruta  :"+ archivo);
        try {
            ChartUtils.saveChartAsPNG(new File(archivo), grafica, 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/cliente/graficaCliente?faces-redirect=true";
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

    public void mostrarReporte() {


        try {
            Session s  = (new Configuration().configure().buildSessionFactory()).openSession();
            SessionImpl sessionImpl = (SessionImpl) s;
            Connection conn = sessionImpl.connection();
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            File report =  new File(servletContext.getRealPath("/reports/reportCliente.jasper"));
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