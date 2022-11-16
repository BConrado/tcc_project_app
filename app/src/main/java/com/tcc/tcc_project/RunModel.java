package com.tcc.tcc_project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RunModel implements Serializable {

    private String LatitudeInicial;
    private String LatitudeFinal;

    private String LongitudeInicial;
    private String LongitudeFinal;

    private List<String> velocidades;
    private List<String> x;
    private List<String> y;
    private List<String> z;

    public RunModel(){}

    public RunModel(String latitudeInicial, String latitudeFinal, String longitudeInicial, String longitudeFinal, List<String> velocidades, List<String> x, List<String> y, List<String> z) {
        LatitudeInicial = latitudeInicial;
        LatitudeFinal = latitudeFinal;
        LongitudeInicial = longitudeInicial;
        LongitudeFinal = longitudeFinal;
        this.velocidades = velocidades;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getLatitudeInicial() {
        return LatitudeInicial;
    }

    public void setLatitudeInicial(String latitudeInicial) {
        LatitudeInicial = latitudeInicial;
    }

    public String getLatitudeFinal() {
        return LatitudeFinal;
    }

    public void setLatitudeFinal(String latitudeFinal) {
        LatitudeFinal = latitudeFinal;
    }

    public String getLongitudeInicial() {
        return LongitudeInicial;
    }

    public void setLongitudeInicial(String longitudeInicial) {
        LongitudeInicial = longitudeInicial;
    }

    public List<String> getVelocidades() {
        return velocidades;
    }

    public void setVelocidades(List<String> velocidades) {
        this.velocidades = velocidades;
    }

    public String getLongitudeFinal() {
        return LongitudeFinal;
    }

    public void setLongitudeFinal(String longitudeFinal) {
        LongitudeFinal = longitudeFinal;
    }

    public List<String> getX() {
        return x;
    }

    public void setX(List<String> x) {
        this.x = x;
    }

    public List<String> getY() {
        return y;
    }

    public void setY(List<String> y) {
        this.y = y;
    }

    public List<String> getZ() {
        return z;
    }

    public void setZ(List<String> z) {
        this.z = z;
    }

    public ArrayList<Double> magnitudes() {
        ArrayList<Double> lst = new ArrayList<>();
        for(int i =0; i<x.size();i++){
            float l = Float.parseFloat(x.get(i));
            float k = Float.parseFloat(y.get(i));
            float j = Float.parseFloat(z.get(i));

            float [] array = new float []{l, k, j};
            double a = magnitude(array);
            lst.add(a);
        }
        return lst;
    }

    double magnitude(float[] v) {
        return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }

}
