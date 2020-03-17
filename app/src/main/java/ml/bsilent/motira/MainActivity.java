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
    DatabaseReference databaseCityes;
    FirebaseDatabase database = FirebaseDatabase.getInstance();





    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fake data
        /*
        cities.add(new City("Rabat",5,new LatLng(34.020882, -6.841650)));
        cities.add(new City("Casablanca",3,new LatLng(33.589886, -7.603869)));
        cities.add(new City("Fes",1,new LatLng(34.03715, -4.9998))); */
        cities.add(new City("Tetouan",2,new LatLng(35.5889,-5.3626)));



        //=========================
        databaseCityes =database.getReference("cities");
        databaseCityes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot city: dataSnapshot.getChildren()){
                    City stadt =dataSnapshot.getValue(City.class);
                    cities.add(stadt);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CitiesAdapter(this,cities));
        OnMapReadyCallback callback=new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map= googleMap;
                map.clear();
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this,R.raw.map));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, -4),5.3f));
                map.getUiSettings().setAllGesturesEnabled(false);
                //map.addCircle(new CircleOptions().fillColor(getResources().getColor(R.color.colorAccent)).radius(100000).center(new LatLng(30.589886, -7.603869)).strokeWidth(0));
                for(City city : cities){
                    googleMap.addCircle(new CircleOptions().radius(city.getNum()*600000/30).center(city.getLatLng()).fillColor(Color.parseColor("#70ff4c4c")).strokeWidth(0));
                }
            }
        };
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(callback);
        mapFragment.getView().setClickable(false);
    }
}
