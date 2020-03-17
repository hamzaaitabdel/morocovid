package ml.bsilent.motira;

import com.google.android.gms.maps.model.LatLng;

public class City {
    private String city;
    private int num;
    private LatLng latLng;
    private double x,y;

    public City(){}
    public LatLng getLatLng() {
        return new LatLng(x,y);
    }

    public City(String city, int num, LatLng latLng) {
        this.city = city;
        this.num = num;
        this.latLng = latLng;
    }
    public City(String city, int num, float x,float y) {
        this.city = city;
        this.num = num;
        this.x = x;
        this.y = y;
        LatLng latLng=new LatLng(x,y);
        this.latLng = latLng;
    }

    public String getCity() {
        return city;
    }

    public int getNum() {
        return num;
    }
}
