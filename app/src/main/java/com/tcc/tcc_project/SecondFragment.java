package com.tcc.tcc_project;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tcc.tcc_project.databinding.FragmentSecondBinding;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    LineChart chart;
    LineChart chart1;
    int x = 0;
    int x1 =0;

    List grafico1;
    List grafico2;

    DataShared dataShared;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);





        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
                        ((MainActivity3)getActivity()).stopTimer();
            }
        });
        chart = getView().findViewById(R.id.InfoChat1);

        chart1 = getView().findViewById(R.id.InfoChat2);
        createGrafico();
        dataShared = new ViewModelProvider(getActivity()).get(DataShared.class);

        LiveData<String> s = dataShared.getMsn();
        LiveData<String> vel = dataShared.getVel();

        s.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                grafico2.add(new Entry(x, Float.parseFloat(s)));
                x++;

                LineDataSet barDataSet = new LineDataSet(grafico2, "Force");
                LineData barData = new LineData(barDataSet);

                chart1.setData(barData);
                chart1.notifyDataSetChanged();
                chart1.invalidate();

                if(grafico2.size()>300){
                    grafico2.clear();
                }
            }
        });

        vel.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                grafico1.add(new Entry(x1, Float.parseFloat(s)));
                x1++;

                LineDataSet barDataSet1 = new LineDataSet(grafico1, "Velocity Linear Chart");
                LineData barData1 = new LineData(barDataSet1);

                chart.setData(barData1);
                chart.notifyDataSetChanged();
                chart.invalidate();

                if(grafico1.size()>300){
                    grafico1.clear();
                }
            }
        });
    }


    public void createGrafico() {


        grafico1 = new ArrayList();
        grafico2 = new ArrayList();


        // velocidade
        grafico1.add(new Entry(x1,0));


        LineDataSet barDataSet = new LineDataSet(grafico1, "Velocity Linear Chart");
        LineData barData = new LineData(barDataSet);
        chart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        chart.getDescription().setEnabled(true);



        grafico2.add(new Entry(0,0));

        LineDataSet barDataSet1 = new LineDataSet(grafico2, "Force");
        LineData barData1 = new LineData(barDataSet1);
        chart1.setData(barData1);
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet1.setValueTextColor(Color.BLACK);
        barDataSet1.setValueTextSize(16f);
        chart1.getDescription().setEnabled(true);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}