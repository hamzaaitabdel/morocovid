package ml.bsilent.motira.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ml.bsilent.motira.Adapters.CitiesAdapter;
import ml.bsilent.motira.Models.City;
import ml.bsilent.motira.Models.Info;
import ml.bsilent.motira.R;

public class MainActivity extends AppCompatActivity {
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private RecyclerView recyclerView;
    private ArrayList<City> cities = new ArrayList<>();
    private TextView death,recovered,total,new_case,excluded;
    private Button akhbar;
    private long tot=0;
    private CitiesAdapter adapter;
    DatabaseReference databaseRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseRef =database.getReference();
        setContentView(R.layout.activity_main);
        death=findViewById(R.id.death);
        excluded=findViewById(R.id.new_death);
        total=findViewById(R.id.total);
        new_case=findViewById(R.id.new_cases);
        recovered=findViewById(R.id.recovered);

        akhbar=findViewById(R.id.akhbar12);
        akhbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), News.class);
                startActivity(myIntent);
            }
        });
        databaseRef.child("others").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot info1: dataSnapshot.getChildren()){
                    Info info =info1.getValue(Info.class);
                    death.setText(info.getDeath()+"");
                    excluded.setText(info.getExcluded()+"");
                    tot=info.getConfirmed();
                    total.setText(tot+"");
                    new_case.setText(info.getNews()+"");
                    recovered.setText(info.getRecoverers()+"");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //fake data
        /*
        cities.add(new City("Rabat",5,new LatLng(34.020882, -6.841650)));
        cities.add(new City("Casablanca",3,new LatLng(33.589886, -7.603869)));
        cities.add(new City("Fes",1,new LatLng(34.03715, -4.9998))); */
        //cities.add(new City("Tetouan",2,new LatLng(35.5889,-5.3626)));



        //=========================

        adapter =new CitiesAdapter(this,cities);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        databaseRef.child("Cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot city1: dataSnapshot.getChildren()){
                    City city =city1.getValue(City.class);
                    cities.add(city);
                }

                if(cities.size()==dataSnapshot.getChildrenCount()){
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
                                googleMap.addCircle(new CircleOptions().radius(city.getNum()*500000/tot).center(new LatLng(city.getX(),city.getY())).fillColor(Color.parseColor("#70ff4c4c")).strokeWidth(0));
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
                    googleMap.addCircle(new CircleOptions().radius(city.getNum()*600000/tot).center(new LatLng(city.getX(),city.getY())).fillColor(Color.parseColor("#70ff4c4c")).strokeWidth(0));
                }
            }
        };
        mapFragment.getMapAsync(callback);
        mapFragment.getView().setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/h1mza_aitabdelouahab/"));
                startActivity(browserIntent);
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
