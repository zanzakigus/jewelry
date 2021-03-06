/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ipn.mx.controlador.web;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author zanzakigus
 */
@ManagedBean(name = "indexMB")
@SessionScoped
public class IndexMB extends BaseBean implements Serializable {

    private String isIndex ="../";
    /**
     * Creates a new instance of IndexMB
     */
    public IndexMB() {
    }

    @PostConstruct
    public void init() {
        this.isIndex="";

    }

    public String getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(String isIndex) {
        this.isIndex = isIndex;
    }

    public String preparedIndex() {
        init();
        return "/index?faces-redirect=true";
    }
}
