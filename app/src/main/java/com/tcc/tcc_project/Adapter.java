package com.tcc.tcc_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private final RVInterface recyclerViewI;
    Map<Integer,List<List<String>>> mapcorridas;

    public Adapter(RVInterface recyclerViewI, Map<Integer,List<List<String>>> mapcorridas){
        this.recyclerViewI = recyclerViewI;
        this.mapcorridas = mapcorridas;

    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mvh = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new MyViewHolder(mvh);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        String name = "Corrida " + position;

        holder.textName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mapcorridas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textName = itemView.findViewById(R.id.textName);

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewI != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewI.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
