package com.ipn.mx.dao;



import com.ipn.mx.entities.Grafica;
import com.ipn.mx.entities.TipoRol;
import com.ipn.mx.entities.Usuario;
import com.ipn.mx.utilities.EnviarMail;
import com.ipn.mx.utilities.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {


    public Usuario login(Usuario dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        List list = new ArrayList();

        try {
            t.begin();
            Query q = s.createQuery("from Usuario c where c.nombreUsuario=:nombreUsuario  and c.password=:claveUsuario  order by c.idUsuario");
            q.setParameter("nombreUsuario", dto.getNombreUsuario());
            q.setParameter("claveUsuario", dto.getPassword());
            for (Usuario c:(List<Usuario>) q.list()) {
                Usuario dtoR = new Usuario();
                dtoR = (c);
                list.add(dtoR);
            }
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
        if(list.size()>0){
            return (Usuario) list.get(0);
        }else
        {
            return null;
        }

    }

    public void create(Usuario dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();

        try {
            t.begin();
            s.save(dto);
            t.commit();
            EnviarMail em = new EnviarMail();
            boolean tipoR = em.enviarCorreo(dto.getEmail(), "Usuario creado", "Has sido dado de alta en la plataforma, ya puedes entrar");

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public void update(Usuario dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();

        try {
            t.begin();
            s.update(dto);
            t.commit();
            EnviarMail em = new EnviarMail();
            boolean tipoR = em.enviarCorreo(dto.getEmail(), "Usuario actualizado", "Tu usuario a sido actualizado");

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public void delete(Usuario dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        try {
            t.begin();
            dto = (s.get(dto.getClass(),dto.getIdUsuario()));
            s.delete(dto);
            t.commit();
            EnviarMail em = new EnviarMail();
            boolean tipoR = em.enviarCorreo(dto.getEmail(), "Usuario eliminado", "Tu usuario a sido elminiado");

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public Usuario read(Usuario dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        try {
            t.begin();
            dto = (s.get(dto.getClass(),dto.getIdUsuario()));
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
        return dto;
    }

    public List readAll(){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        List list = new ArrayList();

        try {
            t.begin();
            Query q = s.createQuery("from Usuario c order by c.idUsuario");
            for (Usuario c:(List<Usuario>) q.list()) {
                Usuario dto = new Usuario();
                dto = (c);
                list.add(dto);
            }
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
        return list;
    }

    public List obtenerDatosGrafica() {

        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        List list = new ArrayList();

        try {
            t.begin();
            Query q = s.createQuery("select p.idTipoRol.descripcionTipoRol as nombreCategoria , count(*) as cantidad from Usuario p  group by  p.idTipoRol.descripcionTipoRol");
            for (Object[] c: (List<Object[]>) q.list()) {
                Grafica dto = new Grafica();
                dto.setNombre(String.valueOf(c[0]));
                dto.setCantidad(Integer.parseInt(String.valueOf( c[1])));
                list.add(dto);
            }
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }

        return list;
    }

    public static void main(String[] args) {

        UsuarioDAO dao= new UsuarioDAO();
        Usuario dto = new Usuario();
       dto.setNombre("Erick");
        dto.setMaterno("Rodriguez");
        dto.setPaterno("Alcantar");
        dto.setNombreUsuario("admin");
        dto.setPassword("admin");
        dto.setEmail("erz_ra@hotmail.com");
        TipoRolDAO dao2= new TipoRolDAO();
        TipoRol dto2 = new TipoRol();
        dto2.setIdTipoRol(1);
        dto2 = dao2.read(dto2);
        dto.setIdTipoRol(dto2);
        //dto.setIdUsuario(1);
        dao.create(dto);
        //dto= dao.read(dto);
        //System.out.println(dto);


    }
}
