package ml.bsilent.motira.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import ml.bsilent.motira.R;

public class FragmentPage extends Fragment {
    private ImageView image;
    private String iUrl="";
    public FragmentPage() {
    }

    public static Fragment getInstance(String url){
        FragmentPage page = new FragmentPage();
        Bundle bundle = new Bundle();
        bundle.putString("img_url",url);
        page.setArguments(bundle);
        return page;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup)inflater.inflate(R.layout.page,container,false);
        image = rootView.findViewById(R.id.image);
        if(getArguments()!=null){
            iUrl=getArguments().getString("img_url");
            Picasso.get().load(iUrl).into(image);
        }

        return rootView;
    }

}
