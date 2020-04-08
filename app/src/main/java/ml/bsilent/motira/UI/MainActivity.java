package ml.bsilent.motira.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ml.bsilent.motira.Adapters.CitiesAdapter;
import ml.bsilent.motira.Models.City;
import ml.bsilent.motira.Models.Info;
import ml.bsilent.motira.R;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<City>> {
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private RecyclerView recyclerView;
    private static ArrayList<City> cities = new ArrayList<>();
    private static TextView death,recovered,total,new_case,excluded;
    private Button akhbar;
    private static long tot=0;
    private String Url="https://moroccostats.herokuapp.com/stats/coronavirus/countries/morocco/regions";
    private SwipeRefreshLayout refreshLayout;
    private CitiesAdapter adapter;
    private FrameLayout progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCities();
        setContentView(R.layout.activity_main);
        death=findViewById(R.id.death);
        progress=findViewById(R.id.progress);
        refreshLayout = findViewById(R.id.refresh);
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
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));

        adapter =new CitiesAdapter(this,cities);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        final Bundle bundle= new Bundle();
        bundle.putString("aa", Url);
        final LoaderManager loaderManager = getSupportLoaderManager();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(21,bundle,MainActivity.this);
            }
        });
        if(loaderManager==null) {
            loaderManager.initLoader(21, bundle, this);
        }else {
            loaderManager.restartLoader(21,bundle,this);
        }
                OnMapReadyCallback callback=new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map= googleMap;
                map.clear();
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this,R.raw.map));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, -4),5.3f));
                map.getUiSettings().setAllGesturesEnabled(false);
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
                        Uri.parse("http://morocovid.rf.gd/"));
                startActivity(browserIntent);
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    private static URL createUrl(String url){
        URL Url = null;
        try{
            Url = new URL(url);
        }catch (IOException e){
        }
        return Url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String json="";
        if(url == null){
            return json;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                json = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream !=null){
                inputStream.close();
            }
            return json;
        }
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream , Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                sb.append(line);
                line = bufferedReader.readLine();
            }
        }
        return sb.toString();
    }
    private static ArrayList<City> getCitiesInfo(String json) throws JSONException {
        ArrayList<City> c=cities;
        tot=0;
        JSONObject object = new JSONObject(json);
        Iterator<String> iterator = object.keys();
        while (iterator.hasNext()){
            String region = iterator.next();
            int cases=Integer.parseInt(object.getString(region));
            for(City city : c){
                if(city.getName().equals(region)){
                    city.setNum(cases);
                    tot+=cases;
                }
            }
        }
        return c;
    }

    @NonNull
    @Override
    public androidx.loader.content.Loader<List<City>> onCreateLoader(int id, @Nullable Bundle args) {
        return new Loader(this,args.getString("aa"));
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<List<City>> loader, List<City> data) {
        adapter.updateData((ArrayList<City>) data);
        cities= (ArrayList<City>) data;
        progress.setVisibility(View.GONE);
        total.setText(tot+"");
        if(refreshLayout.isRefreshing()){
        refreshLayout.setRefreshing(false);}
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

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<List<City>> loader) {
    adapter.updateData(null);
    initCities();
    }

    static class Loader extends androidx.loader.content.AsyncTaskLoader<List<City>>{
        String url;
        public Loader(Context context,String url) {
            super(context);
            this.url=url;
        }
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
        @Override
        public List<City> loadInBackground() {
            if(url==null){
                return null;
            }else {
                try {
                    ArrayList<City> c = getRegionsDetails(url);
                    URL url = createUrl("https://moroccostats.herokuapp.com/stats/coronavirus/countries/morocco/");
                    String json = makeHttpRequest(url);
                    updateInfo(json);
                    return c;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }
    private static void updateInfo(String json) throws JSONException {
        JSONObject object=new JSONObject(json);
        new_case.setText(object.getString("newcases"));
        excluded.setText(object.getString("TotalTests"));
        death.setText(object.getString("totaldeaths"));
        recovered.setText(object.getString("recovered"));
    }
    public void initCities(){
        cities.clear();
        cities.add(new City("BeniMellalKhnifra","BeniMellal-Khénifra", 32.33725, -6.34983));
        cities.add(new City("Daraatafilalet","Drâa-Tafilalet", 31.61596594,-5.1965332 ));
        cities.add(new City("Fsmeknes","Fes-Meknes",34.03715 ,-4.9998 ));
        cities.add(new City("LayouneSakiaElHamra","Layoune-SakiaElHamra",26.09625491 ,-12.05200195 ));
        cities.add(new City("GuelmimOuedNoun","Guelmim-Oued Noun",28.29470743 ,-9.88220215 ));
        cities.add(new City("MarrakechSafi","Marrakech-safi",31.63 , -9.12689209));
        cities.add(new City("Oriental","Oriental",33.52307881 ,-2.3840332 ));
        cities.add(new City("SoussMassa","Souss-Massa",31.120018,-8.067919 ));
        cities.add(new City("RabatSalKenitra","Rabat-Sale-Kenitra",34.020882 , -6.84165));
        cities.add(new City("TangerTetouanAlHoceima","Tanger-Tetouan",35.5889 ,-5.3626 ));
        cities.add(new City("CasaSettat","Casa-settat",33.5883100,-7.6113800));
        cities.add(new City("DakhlaOuedEdDahab","Dakhla-Oued-EdDahab",23.6847700, -15.9579800));
    }
    private static ArrayList<City> getRegionsDetails(String link) throws IOException, JSONException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(link);
        String json = makeHttpRequest(url);
        ArrayList<City> arrayList = getCitiesInfo(json);
        return arrayList;
    }
}
