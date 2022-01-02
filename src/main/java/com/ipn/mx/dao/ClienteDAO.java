package com.ipn.mx.dao;

import com.ipn.mx.entities.Cliente;
import com.ipn.mx.entities.Grafica;
import com.ipn.mx.utilities.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void create(Cliente dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();

        try {
            t.begin();
            s.save(dto);
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public void update(Cliente dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();

        try {
            t.begin();
            s.update(dto);
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public void delete(Cliente dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        try {
            t.begin();
            dto = (s.get(dto.getClass(),dto.getIdCliente()));
            s.delete(dto);
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public Cliente read(Cliente dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        try {
            t.begin();
            dto = (s.get(dto.getClass(),dto.getIdCliente()));
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
            Query q = s.createQuery("from Cliente c order by c.idCliente");
            for (Cliente c:(List<Cliente>) q.list()) {
                Cliente dto = new Cliente();
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
            Query q = s.createQuery("select p.idCiudad.nombreCiudad as nombreCategoria , count(*) as cantidad from Cliente p  group by  p.idCiudad.nombreCiudad");
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

}
