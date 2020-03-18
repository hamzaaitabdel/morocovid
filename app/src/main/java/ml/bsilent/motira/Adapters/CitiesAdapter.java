package ml.bsilent.motira.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ml.bsilent.motira.Models.City;
import ml.bsilent.motira.R;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CViewHolder> {
    private ArrayList<City> cities;
    private Context context;

    public CitiesAdapter(Context context,ArrayList<City> cities) {
        this.cities = cities;
        this.context=context;
    }
    public void updateData(ArrayList<City> cities){
        this.cities = cities;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CViewHolder(LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder cViewHolder, int i) {
        City city = cities.get(i);
        cViewHolder.textView.setText(city.getCity()+" :");
        cViewHolder.textView1.setText(city.getNum()+"");
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView1;
        public CViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv);
            textView1=itemView.findViewById(R.id.tv1);
        }
    }
}
