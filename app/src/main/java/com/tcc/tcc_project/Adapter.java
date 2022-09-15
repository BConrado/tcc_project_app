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

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private final RVInterface recyclerViewI;
    private List<String> list = List.of("Run 14/09/22 16:56", "Run 15/09/22 9:54", "Run 14/09/22 16:56", "Run 15/09/22 9:54" , "Run 14/09/22 16:56", "Run 15/09/22 9:54",
            "Run 14/09/22 16:56", "Run 15/09/22 9:54", "Run 14/09/22 16:56", "Run 15/09/22 9:54", "Run 14/09/22 16:56", "Run 15/09/22 9:54");

    public Adapter(RVInterface recyclerViewI){
        this.recyclerViewI = recyclerViewI;
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
        String name = list.get(position);

        holder.textName.setText(name);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
