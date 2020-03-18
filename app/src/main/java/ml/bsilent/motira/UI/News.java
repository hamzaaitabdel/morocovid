package ml.bsilent.motira.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ml.bsilent.motira.Models.Documents;
import ml.bsilent.motira.R;
import ml.bsilent.motira.Adapters.VPagerAdapter;

public class News extends AppCompatActivity {
    private ViewPager viewPager;
    private VPagerAdapter pagerAdapter;
    private Button down;
    DatabaseReference databaseRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final List<Fragment> list = new ArrayList<>();
        down = findViewById(R.id.down_bt);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        viewPager = findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==list.size()-1){
                    down.setVisibility(View.INVISIBLE);
                }else{
                    down.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter =new VPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(pagerAdapter);

        databaseRef =database.getReference();
        databaseRef.child("doc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot doc1: dataSnapshot.getChildren()){
                    Documents doc =doc1.getValue(Documents.class);
                    list.add(FragmentPage.getInstance(doc.getLink()));
                }
                if(list.size()==dataSnapshot.getChildrenCount()){
                    pagerAdapter.updateList(list);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(News.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
