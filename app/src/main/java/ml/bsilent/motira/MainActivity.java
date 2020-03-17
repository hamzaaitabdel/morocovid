package ml.bsilent.motira;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private RecyclerView recyclerView;
    private ArrayList<City> cities = new ArrayList<>();
    private CitiesAdapter adapter;
    DatabaseReference databaseCityes;
    FirebaseDatabase database = FirebaseDatabase.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fake data
        /*
        cities.add(new City("Rabat",5,new LatLng(34.020882, -6.841650)));
        cities.add(new City("Casablanca",3,new LatLng(33.589886, -7.603869)));
        cities.add(new City("Fes",1,new LatLng(34.03715, -4.9998))); */
        //cities.add(new City("Tetouan",2,new LatLng(35.5889,-5.3626)));



        //=========================
        databaseCityes =database.getReference();
        adapter =new CitiesAdapter(this,cities);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        databaseCityes.child("Cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot city1: dataSnapshot.getChildren()){
                    City city =city1.getValue(City.class);
                    cities.add(city);
                }
                if(cities.size()==dataSnapshot.getChildrenCount()+1){

                    adapter.updateData(cities);
                    OnMapReadyCallback callback=new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map= googleMap;
                            map.clear();
                            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this,R.raw.map));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, -4),5.3f));
                            map.getUiSettings().setAllGesturesEnabled(false);
                            for(City city : cities){
                                googleMap.addCircle(new CircleOptions().radius(city.getNum()*600000/30).center(new LatLng(city.getX(),city.getY())).fillColor(Color.parseColor("#70ff4c4c")).strokeWidth(0));
                            }
                        }
                    };
                    mapFragment.getMapAsync(callback);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        OnMapReadyCallback callback=new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map= googleMap;
                map.clear();
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this,R.raw.map));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, -4),5.3f));
                map.getUiSettings().setAllGesturesEnabled(false);
                for(City city : cities){
                    googleMap.addCircle(new CircleOptions().radius(city.getNum()*600000/30).center(new LatLng(city.getX(),city.getY())).fillColor(Color.parseColor("#70ff4c4c")).strokeWidth(0));
                }
            }
        };
        mapFragment.getMapAsync(callback);
        mapFragment.getView().setClickable(false);
    }
}
