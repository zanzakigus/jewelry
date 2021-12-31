package com.ipn.mx.controlador.web;


import com.ipn.mx.dao.UsuarioDAO;
import com.ipn.mx.entities.Producto;
import com.ipn.mx.entities.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.io.Serializable;

/**
 * @author zanzakigus
 */
@ManagedBean(name = "loginMB")
@RequestScoped
public class LoginMB extends BaseBean implements Serializable {


    private final UsuarioDAO dao = new UsuarioDAO();

    private Usuario dto;
    private boolean respuesta;
    private boolean logged;

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

    public boolean getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean getLogged() {
        return logged;
    }



    @PostConstruct
    public void init() {
        this.dto = new Usuario();

    }

    public String preparedIndex() {
        init();
        return "/login?faces-redirect=true";
    }
    public String preparedLogged() {
        return "/index.xhtml?faces-redirect=true";
    }
    public void login() {
/*        String email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("txtUsuarioPassword");
        String password = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");*/
        try {
            if ((this.dto = dao.login(dto)) != null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                session.setAttribute("usuario", dto.getNombreUsuario());
                session.setAttribute("tipo", dto.getIdTipoRol().getDescripcionTipoRol());
                logged = true;
                preparedLogged();
            } else {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                System.out.println("so esyou aqui");
                respuesta = true;
                logged = false;
                preparedIndex();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
