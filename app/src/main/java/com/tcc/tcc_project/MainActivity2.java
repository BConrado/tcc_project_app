package com.tcc.tcc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

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
    }

    private RunModel bancoDeDados(int pos){
        ArrayList<RunModel> runsHistoric = new ArrayList<>();

        runsHistoric.add(new RunModel(-30.056652,-51.172892, 1663213479,
                1663213400, -30.061416, -51.172967));

        runsHistoric.add(new RunModel(-30.056652,-51.172892, 1663213479,
                1663213400, -30.061416, -51.172967));


        return runsHistoric.get(pos);
    }
}