package com.ipn.mx.controlador.web;

import com.ipn.mx.dao.ProductoDAO;
import com.ipn.mx.entities.Cliente;
import com.ipn.mx.entities.Producto;
import com.ipn.mx.entities.TransaccionTotal;
import com.ipn.mx.entities.TransaccionUnitaria;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "transaccionUnitariaMB")
@SessionScoped
public class TransaccionUnitarioMB extends BaseBean implements Serializable {

    private List<TransaccionUnitaria> uniListTransaccionUnitaria = new ArrayList<>();
    private List<Producto> uniListProducto;

    private ProductoDAO uniPDao = new ProductoDAO();
    private TransaccionUnitaria uniDto;
    private TransaccionTotal ttDto = new TransaccionTotal();



    /**
     * Creates a new instance of TransaccionMB
     */
    public TransaccionUnitarioMB() {

    }

    public List<TransaccionUnitaria> getListTransaccionUnitaria() {
        return uniListTransaccionUnitaria;
    }

    public void setListTransaccionUnitaria(List<TransaccionUnitaria> uniListTransaccionUnitaria) {
        this.uniListTransaccionUnitaria = uniListTransaccionUnitaria;
    }

    public void init(){
        uniListTransaccionUnitaria = new ArrayList<>();
    }

    public List<Producto> getListProducto() {
        return uniListProducto;
    }

    public void setListProducto(List<Producto> uniListProducto) {
        this.uniListProducto = uniListProducto;
    }

    public TransaccionUnitaria getDto() {
        return uniDto;
    }

    public void setDto(TransaccionUnitaria uniDto) {
        this.uniDto = uniDto;
    }

    public TransaccionTotal getTtDto() {
        return ttDto;
    }

    public void setTtDto(TransaccionTotal ttDto) {
        this.ttDto = ttDto;
    }

    public String preparedAdd() {
        uniDto = new TransaccionUnitaria();
        uniListProducto = new ArrayList<>();
        uniListProducto = uniPDao.readAll();
        uniDto.setPeso(uniListProducto.get(0).getPesoProducto());
        uniDto.setDescuento(uniListProducto.get(0).getDescuentoProducto()*uniDto.getCantidad());
        uniDto.setPrecioUnitario(uniListProducto.get(0).getPrecioProducto());
        uniDto.setMonto(uniListProducto.get(0).getPrecioProducto()*uniDto.getCantidad());
        uniDto.setIdPro(uniListProducto.get(0).getIdProducto());
        uniDto.setIdProducto(uniListProducto.get(0));
        setAccion(ACC_CREAR);
        return "/transaccion/transaccionUnitariaForm?faces-redirect=true";
    }

    public void actualizaValor(ValueChangeEvent event) {
        Producto est = new  Producto();
        est.setIdProducto((int) event.getNewValue());
        est = uniPDao.read(est);
        uniDto.setPeso(est.getPesoProducto());
        uniDto.setDescuento(est.getDescuentoProducto()*uniDto.getCantidad());
        uniDto.setPrecioUnitario(est.getPrecioProducto());
        uniDto.setMonto(est.getPrecioProducto()*uniDto.getCantidad());
        uniDto.setIdProducto(est);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void actualizaValorCantidad(ValueChangeEvent event) {
        uniDto.setCantidad((int) event.getNewValue());
        Producto est = new  Producto();
        est.setIdProducto(uniDto.getIdPro());
        est = uniPDao.read(est);
        uniDto.setPeso(est.getPesoProducto());
        uniDto.setDescuento(est.getDescuentoProducto()*uniDto.getCantidad());
        uniDto.setPrecioUnitario(est.getPrecioProducto());
        uniDto.setMonto(est.getPrecioProducto()*uniDto.getCantidad());
        uniDto.setIdProducto(est);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public String add(){
        this.uniListTransaccionUnitaria.add(uniDto);

        ttDto.setTotal(ttDto.getTotal()+uniDto.getMonto());
        ttDto.setDescuentoTotal(ttDto.getDescuentoTotal()+uniDto.getDescuento());
        ttDto.setNetoTotal(ttDto.getTotal()-ttDto.getDescuentoTotal());
        return "/transaccion/transaccionForm?faces-redirect=true";
    }

}

