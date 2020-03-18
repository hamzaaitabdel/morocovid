package ml.bsilent.motira.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;
import android.util.Log;
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
    private VerticalViewPager viewPager;
    private VPagerAdapter pagerAdapter;
    DatabaseReference databaseRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final List<Fragment> list = new ArrayList<>();

        viewPager = findViewById(R.id.pager);
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
}
