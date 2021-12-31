package com.ipn.mx.controlador.web;



import com.ipn.mx.dao.UsuarioDAO;
import com.ipn.mx.entities.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.io.Serializable;

/**
 *
 * @author zanzakigus
 */
@ManagedBean(name = "loginMB")
@SessionScoped
public class LoginMB extends BaseBean implements Serializable{


    private final UsuarioDAO dao = new UsuarioDAO();

    private Usuario dto;
    private String respuesta=null;

    /**
     * Creates a new instance of IndexMB
     */
    public LoginMB() {
    }

    public Usuario getDto() {
        return dto;
    }

    public void setDto(Usuario dto) {
        this.dto = dto;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    @PostConstruct
    public void init() {
        this.dto = new Usuario();
    }

    public String preparedIndex() {
        init();
        return "/login?faces-redirect=true";
    }

    public void login(){
/*        String email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("txtUsuarioPassword");
        String password = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");*/
        setRespuesta(null);
        try {
            if ((this.dto = dao.login(dto)) != null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                session.setAttribute("usuario", dto.getNombreUsuario());
                session.setAttribute("tipo", dto.getIdTipoRol().getDescripcionTipoRol());
            } else {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                setRespuesta("Credenciales invalidas");
                preparedIndex();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
