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
public class BarChartData {
    private List<String> labels = new ArrayList<>();
    private List<SeriesData> dataset = new ArrayList<>();
    
    @JsonIgnore
    private Map<String, SeriesData> datasetMap = new HashMap<>();

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<SeriesData> getDataset() {
        return dataset;
    }

    @JsonIgnore
    public Map<String, SeriesData> getDatasetMap() {
        return datasetMap;
    }

    public void setDatasetMap(Map<String, SeriesData> dataset) {
        this.datasetMap = dataset;
    }
    
    public void addLabel(String... labels) {
        this.labels.addAll(Arrays.asList(labels));
    }
    
    public void addNewSeries(String label) {
        this.datasetMap.put(label, new SeriesData(label));
    }
    
    public void addNewSeries(String label, List<Integer> d) {
        this.datasetMap.put(label, new SeriesData(label, d));
    }
    
    public void addDataToSeries(String label, Integer... d) {
        this.datasetMap.get(label).getData().addAll(Arrays.asList(d));
    }
    
    public void build() {
        this.dataset.clear();
        datasetMap.values().forEach((d) -> {
            this.dataset.add(d);
        });
    }
}
