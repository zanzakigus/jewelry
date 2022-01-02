package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.*;
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
@ManagedBean(name = "transaccionMB")
@SessionScoped
public class TransaccionMB extends BaseBean implements Serializable {

    private final TransaccionTotalDAO dao = new TransaccionTotalDAO();
    private final TransaccionUnitariaDAO uDao = new TransaccionUnitariaDAO();
    private final ClienteDAO cDao = new ClienteDAO();
    private final UsuarioDAO usDao = new UsuarioDAO();
    private final TipoPagoDAO pDao = new TipoPagoDAO();

    private TransaccionTotal dto;
    private List<TransaccionTotal> listTransaccionTotal;
    private List<TransaccionUnitaria> listTransaccionUnitaria;
    private List<Cliente> listCliente;
    private List<TipoPago> listTipoPago;

    /**
     * Creates a new instance of TransaccionMB
     */
    public TransaccionMB() {

    }

    public List<TransaccionTotal> getListTransaccionTotal() {
        return listTransaccionTotal;
    }

    public void setListTransaccionTotal(List<TransaccionTotal> listTransaccionTotal) {
        this.listTransaccionTotal = listTransaccionTotal;
    }

    public List<TransaccionUnitaria> getListTransaccionUnitaria() {
        return listTransaccionUnitaria;
    }

    public void setListTransaccionUnitaria(List<TransaccionUnitaria> listTransaccionUnitaria) {
        this.listTransaccionUnitaria = listTransaccionUnitaria;
    }

    public TransaccionTotal getDto() {
        return dto;
    }

    public void setDto(TransaccionTotal dto) {
        this.dto = dto;
    }

    public List<Cliente> getListCliente() {
        return listCliente;
    }

    public void setListCliente(List<Cliente> listCliente) {
        this.listCliente = listCliente;
    }

    public List<TipoPago> getListTipoPago() {
        return listTipoPago;
    }

    public void setListTipoPago(List<TipoPago> listTipoPago) {
        this.listTipoPago = listTipoPago;
    }

    @PostConstruct
    public void init() {
        listTransaccionTotal = new ArrayList<>();
        listTransaccionTotal = dao.readAll();

        listCliente = new ArrayList<>();
        listCliente = cDao.readAll();

        listTipoPago = new ArrayList<>();
        listTipoPago = pDao.readAll();

    }

    public String preparedAdd() {
        dto = new TransaccionTotal();
        setAccion(ACC_CREAR);
        return "/transaccion/transaccionForm?faces-redirect=true";
    }

    public String preparedUpdate() {
        setAccion(ACC_ACTUALIZAR);
        dto.setIdCl(dto.getIdCliente().getIdCliente());
        dto.setIdTipo(dto.getIdTipoPago().getIdTipoPago());
        return "/transaccion/transaccionForm?faces-redirect=true";
    }

    public String preparedIndex() {
        init();
        return "/transaccion/listadoTransaccionTotals?faces-redirect=true";
    }

    public boolean validate() {
        boolean valido = true;

        // nos toca todas las reglas de validacion
        if (dto.getIdTipoPago() == null) {
            valido = false;
        }
        return valido;
    }

    public String add() {
        boolean valido = validate();
        if (valido) {

            Cliente est = new  Cliente();
            est.setIdCliente(dto.getIdCl());
            est = cDao.read(est);
            dto.setIdCliente(est);

            TipoPago tipo = new  TipoPago();
            tipo.setIdTipoPago(dto.getIdTipo());
            tipo = pDao.read(tipo);
            dto.setIdTipoPago(tipo);

            String claveUsuario = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUser");
            dto.setIdUsu(Integer.parseInt(claveUsuario));

            Usuario usu = new  Usuario();
            usu.setIdUsuario(dto.getIdUsu());
            usu = usDao.read(usu);
            dto.setIdUsuario(usu);

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
            Cliente est = new  Cliente();
            est.setIdCliente(dto.getIdCl());
            est = cDao.read(est);
            dto.setIdCliente(est);

            TipoPago tipo = new  TipoPago();
            tipo.setIdTipoPago(dto.getIdTipo());
            tipo = pDao.read(tipo);
            dto.setIdTipoPago(tipo);

            String claveUsuario = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUser");
            dto.setIdUsu(Integer.parseInt(claveUsuario));

            Usuario usu = new  Usuario();
            usu.setIdUsuario(dto.getIdUsu());
            usu = usDao.read(usu);
            dto.setIdUsuario(usu);

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

    public void seleccionarTransaccionTotal(ActionEvent event) {
        String claveSeleccionada = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new TransaccionTotal();
        dto.setIdTransaccionTotal(Integer.parseInt(claveSeleccionada));

        try {
            dto = dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String obtenerGrafica(){
        JFreeChart grafica = ChartFactory.createPieChart("TransaccionTotals por tipo de pago", obtenerDatosGrafica(), true, true, Locale.getDefault());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String archivo = servletContext.getRealPath("/grafica.png");
        System.out.printf("ruta  :"+ archivo);
        try {
            ChartUtils.saveChartAsPNG(new File(archivo), grafica, 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/transaccion/graficaTransaccionTotal?faces-redirect=true";
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
            File report =  new File(servletContext.getRealPath("/reports/reportTransaccionTotal.jasper"));
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