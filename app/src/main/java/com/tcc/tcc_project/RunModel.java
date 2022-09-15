package com.tcc.tcc_project;

import java.sql.Timestamp;

public class RunModel {
    public RunModel(double latitude, double longitude, long initialTime,
                    long finalTime, double latitudeF, double longitudeF) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.initialTime = initialTime;
        this.finalTime = finalTime;
        this.latitudeF = latitudeF;
        this.longitudeF = longitudeF;
    }

    private double latitude;
    private double longitude;
    private double latitudeF;
    private double longitudeF;
    private long initialTime;
    private long finalTime;

    public double getLatitudeF() {
        return latitudeF;
    }

    public void setLatitudeF(double latitudeF) {
        this.latitudeF = latitudeF;
    }

    public double getLongitudeF() {
        return longitudeF;
    }

    public void setLongitudeF(double longitudeF) {
        this.longitudeF = longitudeF;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public long getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(long finalTime) {
        this.finalTime = finalTime;
    }

    public String calculateAvarageSpeed(){
        double velocidadeMedia;
        double time = this.finalTime - this.initialTime;
        double latitudeAbs = this.latitude - this.latitudeF;
        double longitudeAbs = this.longitude - this.longitudeF;
        latitudeAbs *= 111.1;
        longitudeAbs *= 96.2;
        double latAbsSq = latitudeAbs * latitudeAbs;
        double longAbsSq = longitudeAbs * longitudeAbs;
        double distanciaKM = Math.sqrt(latAbsSq+longAbsSq);

        long tempoMedio = this.finalTime - this.initialTime;

        Timestamp ts = new Timestamp(tempoMedio);

        velocidadeMedia =distanciaKM / ts.getHours();

        return ""+velocidadeMedia+ " KM/H";
    }

}
