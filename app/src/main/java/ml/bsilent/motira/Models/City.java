package ml.bsilent.motira.Models;

import com.google.android.gms.maps.model.LatLng;

public class City {
    private String city,name;
    private int num;
    private double x,y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public City(){}

    public String getName() {
        return name;
    }

    public City(String city, int num, LatLng latLng) {
        this.city = city;
        this.num = num;
        this.x = latLng.latitude;
        this.y=latLng.longitude;
    }

    public City(String city, String name, int num, double x, double y) {
        this.city = city;
        this.name = name;
        this.num = num;
        this.x = x;
        this.y = y;
    }
    public City( String name,String city, double x, double y) {
        this.city = city;
        this.name = name;
        this.num = 0;
        this.x = x;
        this.y = y;
    }

    public City(String city, int num, float x, float y) {
        this.city = city;
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public String getCity() {
        return city;
    }

    public int getNum() {
        return num;
    }
}
