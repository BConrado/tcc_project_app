package com.tcc.tcc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    List vels;
    List forcas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        TextView lati = findViewById(R.id.LAT);
//        TextView longi = findViewById(R.id.LON);
        TextView velM = findViewById(R.id.VelMed);
        TextView distanciaTotal = findViewById(R.id.distanciaTotal);
        int id = getIntent().getIntExtra("POS", 0);
        RunModel runModel = (RunModel)getIntent().getSerializableExtra("myRun");





        //RunModel rm = bancoDeDados(id);

//        lati.setText(rm.getLatitude()+"");
//        longi.setText(rm.getLongitude()+"");
        velM.setText(runModel.getVelocidadeMedia());
        distanciaTotal.setText(runModel.getDistancia());

        LineChart velocidadesChart = findViewById(R.id.vel);
        LineChart forcasChart = findViewById(R.id.forcaRes);

        getDataChart(runModel.getVelocidades());
        getDataChartForca(runModel.getMagnitudes());


        LineDataSet barDataSetVel = new LineDataSet(vels, "Velocity Linear Chart");
        LineDataSet barDataSetGrav = new LineDataSet(forcas, "Gravity Linear Chart");

        LineData barDataVel = new LineData(barDataSetVel);
        LineData barDataGrav = new LineData(barDataSetGrav);

        velocidadesChart.setData(barDataVel);
        forcasChart.setData(barDataGrav);

        barDataSetVel.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSetVel.setValueTextColor(Color.BLACK);
        barDataSetVel.setValueTextSize(16f);
        velocidadesChart.getDescription().setEnabled(true);

        barDataSetGrav.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSetGrav.setValueTextColor(Color.BLACK);
        barDataSetGrav.setValueTextSize(16f);
        forcasChart.getDescription().setEnabled(true);


    }



    private void getDataChart(List data){
        vels = new ArrayList();
        for (int i = 0; i<data.size(); i++){
            vels.add(new Entry(i, Float.parseFloat(data.get(i).toString())));
        }
    }

    private void getDataChartForca(List data){
        forcas = new ArrayList();
        for (int i =0; i<data.size(); i++){
            forcas.add(new Entry(i, Float.parseFloat(data.get(i).toString())));
        }
    }

}