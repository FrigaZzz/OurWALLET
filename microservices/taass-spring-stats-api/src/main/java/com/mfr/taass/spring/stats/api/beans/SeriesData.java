/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author lucamolinaro
 */
public class SeriesData {
    private String label;
    private List<Integer> data;

    public SeriesData(String label, List<Integer> data) {
        this.label = label;
        this.data = data;
    }

    public SeriesData(String label) {
        this.label = label;
        this.data = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
    
    public void addDataPoint(Integer d) {
        this.data.add(d);
    }
    
}
