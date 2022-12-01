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

    public String getVelocidadeMedia() {
        return velocidadeMedia;
    }

    public void setVelocidadeMedia(String velocidadeMedia) {
        this.velocidadeMedia = velocidadeMedia;
    }

    private String velocidadeMedia;

    public List<Double> getMagnitudes() {
        return magnitudes;
    }

    public void setMagnitudes(List<Double> magnitudes) {
        this.magnitudes = magnitudes;
    }

    private List<Double> magnitudes;


    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    private String distancia;

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

    String distancia(String latI, String LongI, String latF, String LongF) {
        double R = 6371.0; // raio da terra

        double lat1 = Math.toRadians(Double.parseDouble(latI));
        double lon1 = Math.toRadians(Double.parseDouble(LongI));
        double lat2 = Math.toRadians(Double.parseDouble(latF));
        double lon2 = Math.toRadians(Double.parseDouble(LongF));

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a =  Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanciaEmMetros = R * c;

        return distanciaEmMetros+"";
    }


}
