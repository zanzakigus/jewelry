package com.ipn.mx.dao;


import com.ipn.mx.entities.Categoria;
import com.ipn.mx.entities.Grafica;
import com.ipn.mx.utilities.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void create(Categoria dto){
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

    public void update(Categoria dto){
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

    public void delete(Categoria dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        try {
            t.begin();
            dto = s.get(dto.getClass(),dto.getIdCategoria());
            s.delete(dto);
            t.commit();

        }catch (HibernateException he){
            if(t != null && t.isActive()){
                t.rollback();
            }
        }
    }

    public Categoria read(Categoria dto){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.getTransaction();
        try {
            t.begin();
            dto = s.get(dto.getClass(),dto.getIdCategoria());
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
            Query q = s.createQuery("from Categoria c order by c.idCategoria");
            list = q.list();
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
            Query q = s.createQuery("select p.idCategoria.nombreCategoria as nombreCategoria , count(*) as cantidad from Producto p  group by  p.idCategoria.nombreCategoria");
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

        CategoriaDAO dao= new CategoriaDAO();
        Categoria dto2 = new Categoria();
        /*dto2.setIdCategoria(1);*/
        System.out.println(dao.readAll());;

    }
}
