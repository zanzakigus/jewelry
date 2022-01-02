package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.TipoRolDAO;
import com.ipn.mx.dao.UsuarioDAO;
import com.ipn.mx.entities.TipoRol;
import com.ipn.mx.entities.Grafica;
import com.ipn.mx.entities.Producto;
import com.ipn.mx.entities.Usuario;
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
@ManagedBean(name = "usuarioMB")
@SessionScoped
public class UsuarioMB extends BaseBean implements Serializable {

    private final UsuarioDAO dao = new UsuarioDAO();
    private final TipoRolDAO cDao = new TipoRolDAO();

    private Usuario dto;
    private List<Usuario> listUsuario;
    private List<TipoRol> listTipoRol;

    /**
     * Creates a new instance of UsuarioMB
     */
    public UsuarioMB() {

    }

    public List<Usuario> getListUsuario() {
        return listUsuario;
    }

    public void setListUsuario(List<Usuario> listUsuario) {
        this.listUsuario = listUsuario;
    }

    public List<TipoRol> getListTipoRol() {
        return listTipoRol;
    }

    public void setListTipoRol(List<TipoRol> listTipoRol) {
        this.listTipoRol = listTipoRol;
    }

    public Usuario getDto() {
        return dto;
    }

    public void setDto(Usuario dto) {
        this.dto = dto;
    }


    @PostConstruct
    public void init() {
        listUsuario = new ArrayList<>();
        listUsuario = dao.readAll();

        listTipoRol = new ArrayList<>();
        listTipoRol = cDao.readAll();

    }

    public String preparedAdd() {
        dto = new Usuario();
        setAccion(ACC_CREAR);
        return "/usuario/usuarioForm?faces-redirect=true";
    }

    public String preparedUpdate() {
        setAccion(ACC_ACTUALIZAR);
        dto.setIdTipo(dto.getIdTipoRol().getIdTipoRol());
        return "/usuario/usuarioForm?faces-redirect=true";
    }

    public String preparedIndex() {
        init();
        return "/usuario/listadoUsuarios?faces-redirect=true";
    }

    public boolean validate() {
        boolean valido = true;

        // nos toca todas las reglas de validacion
        if (dto.getNombreUsuario() == null) {
            valido = false;
        }
        return valido;
    }

    public String add() {
        boolean valido = validate();
        if (valido) {
            TipoRol cat = new  TipoRol();
            cat.setIdTipoRol(dto.getIdTipo());
            cat = cDao.read(cat);
            dto.setIdTipoRol(cat);
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
            TipoRol cat = new  TipoRol();
            cat.setIdTipoRol(dto.getIdTipo());
            cat = cDao.read(cat);
            dto.setIdTipoRol(cat);
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

    public void seleccionarUsuario(ActionEvent event) {
        String claveSeleccionada = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new Usuario();
        dto.setIdUsuario(Integer.parseInt(claveSeleccionada));

        try {
            dto = dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String obtenerGrafica(){
        JFreeChart grafica = ChartFactory.createPieChart("Usuarios por tipo", obtenerDatosGrafica(), true, true, Locale.getDefault());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String archivo = servletContext.getRealPath("/grafica.png");
        System.out.printf("ruta  :"+ archivo);
        try {
            ChartUtils.saveChartAsPNG(new File(archivo), grafica, 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/usuario/graficaUsuario?faces-redirect=true";
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
            File report =  new File(servletContext.getRealPath("/reports/reportUsers.jasper"));
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