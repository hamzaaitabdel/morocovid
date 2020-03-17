package ml.bsilent.motira;

import com.google.android.gms.maps.model.LatLng;

public class City {
    private String city;
    private int num;
    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public City(String city, int num, LatLng latLng) {
        this.city = city;
        this.num = num;
        this.latLng = latLng;
    }
    public City(String city, int num, float x,float y) {
        this.city = city;
        this.num = num;
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
