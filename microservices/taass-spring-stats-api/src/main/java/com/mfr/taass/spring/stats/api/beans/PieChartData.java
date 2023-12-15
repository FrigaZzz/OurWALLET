/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.stats.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lucamolinaro
 */
public class PieChartData {
    private List<String> labels = new ArrayList<>();
    private List<Long> dataset = new ArrayList<>();

    public PieChartData( ) {

    }
 
    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Long> getDataset() {
        return dataset;
    }

    public void setDataset(List<Long> dataset) {
        this.dataset = dataset;
    }
    
   
    public void addData(String s,Long value){
        this.dataset.add(value);
        this.labels.add(s);
    }
    
}
