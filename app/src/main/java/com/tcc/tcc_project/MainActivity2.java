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

    List listChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView lati = findViewById(R.id.LAT);
        TextView longi = findViewById(R.id.LON);
        TextView velM = findViewById(R.id.VelMed);


        int id = getIntent().getIntExtra("POS", 0);

        RunModel rm = bancoDeDados(id);

        lati.setText(rm.getLatitude()+"");
        longi.setText(rm.getLongitude()+"");
        velM.setText(rm.calculateAvarageSpeed());

        LineChart chart = findViewById(R.id.linearChart);
        getDataChart(rm.getVelocidades());
        LineDataSet barDataSet = new LineDataSet(listChart, "Velocity Linear Chart");
        LineData barData = new LineData(barDataSet);
        chart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        chart.getDescription().setEnabled(true);
    }

    private RunModel bancoDeDados(int pos){
        ArrayList<Integer> velocidades = new ArrayList<>();
        ArrayList<RunModel> runsHistoric = new ArrayList<>();
        velocidades.add(0);velocidades.add(20);velocidades.add(43);velocidades.add(62);velocidades.add(23);velocidades.add(23);velocidades.add(34);velocidades.add(45);velocidades.add(75);
        velocidades.add(65);velocidades.add(56);velocidades.add(43);velocidades.add(62);velocidades.add(23);velocidades.add(43);velocidades.add(34);velocidades.add(45);velocidades.add(75);
        velocidades.add(75);velocidades.add(56);velocidades.add(43);velocidades.add(62);velocidades.add(23);velocidades.add(65);velocidades.add(34);velocidades.add(45);velocidades.add(75);


        runsHistoric.add(new RunModel(-30.056652,-51.172892, 1663213479,
                1663213400, -30.061416, -51.172967, velocidades));

        runsHistoric.add(new RunModel(-30.056652,-51.172892, 1663213479,
                1663213400, -30.061416, -51.172967, velocidades));


        return runsHistoric.get(pos);
    }

    private void getDataChart(ArrayList data){
        listChart = new ArrayList();
        for (int i = 0; i<data.size(); i++){
            listChart.add(new Entry(i, Integer.parseInt(data.get(i).toString())));
        }
    }

}