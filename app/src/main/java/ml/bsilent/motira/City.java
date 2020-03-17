package ml.bsilent.motira;

import com.google.android.gms.maps.model.LatLng;

public class City {
    private String city;
    private int num;
    private double x,y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public City(){}

    public City(String city, int num, LatLng latLng) {
        this.city = city;
        this.num = num;
        this.x = latLng.latitude;
        this.y=latLng.longitude;
    }
    public City(String city, int num, float x,float y) {
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
