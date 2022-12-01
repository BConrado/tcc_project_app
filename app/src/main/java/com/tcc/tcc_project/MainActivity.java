package com.tcc.tcc_project;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.util.RunnableKt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tcc.tcc_project.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements RVInterface{


    private ActivityMainBinding binding;
    File file;
    FileUtil fileUtil;
    Map<Integer,List<List<String>>> mapcorridas = new HashMap<>();
    int id;

    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        file = new File(getFilesDir()+ "/data.csv");
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }

        if(!file.exists()) {
            fileUtil.writeStringAsFile("id;latitude;longitude;speed;timestamp;x;y;z\n", file);
            id = 0;
        }else {
            FileInputStream fis = null;
            try {
                fis = openFileInput("data.csv");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String text;

                br.readLine(); //limpando primeira linha
                while ((text = br.readLine()) != null) {
                    List<String> atual = Arrays.asList(text.split(";"));
                    int key = Integer.parseInt(atual.get(0));
                    if (mapcorridas.containsKey(key)) {
                        List<List<String>> list = mapcorridas.get(Integer.parseInt(atual.get(0)));
                        list.add(atual);
                        mapcorridas.put(Integer.parseInt(atual.get(0)), list);
                    } else {
                        List<List<String>> list = new ArrayList<>();
                        list.add(atual);
                        System.out.println(Integer.parseInt(atual.get(0)) + "#");
                        mapcorridas.put(Integer.parseInt(atual.get(0)), list);
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            id = mapcorridas.size();
        }


//        for (Map.Entry<Integer, List<List<String>>> entry : mapcorridas.entrySet()) {
//            System.out.print(entry.getKey() + " / ");
//            entry.getValue().forEach(p-> System.out.println(p));
//        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

        Button newRun = findViewById(R.id.bottom_nav);
        newRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                intent.putExtra("id", id);

                RunModel runModel = readRunFromFile(id+"");

                intent.putExtra("myRun", runModel);


                startActivity(intent);
            }
        });


    }

    private RunModel readRunFromFile(String id){
        RunModel rn = new RunModel();

        // read from file
        List<List<String>> corrida = mapcorridas.get(id);


        // Latitude Inicial e Final
        // Longitude Inicial e Final
        // Array de Velocidades
        // Array com x, array com y e array com z

        // "id;latitude;longitude;speed;timestamp;x;y;z\n"
        //  0    1         2         3     4      5,6,7
        String latI = corrida.get(0).get(1);
        String lngI = corrida.get(0).get(2);
        String latF = corrida.get(corrida.size()-1).get(1);
        String lngF = corrida.get(corrida.size()-1).get(2);

        rn.setDistancia(rn.distancia(latI, lngI, latF, lngF));


        ArrayList<String> velocidades = new ArrayList<>();

        for (int i = 0; i < corrida.size(); i++){
            velocidades.add(corrida.get(i).get(3));
        }
        rn.setVelocidades(velocidades);


        ArrayList<Double> magnitudes = new ArrayList<>();

        for (int i = 0; i < corrida.size(); i++){
            float x = Float.parseFloat(corrida.get(i).get(5));
            float y = Float.parseFloat(corrida.get(i).get(6));
            float z = Float.parseFloat(corrida.get(i).get(7));

            float [] xyz = new float []{x,y,z};
            magnitudes.add(rn.magnitude(xyz));
        }

        String velMed;

        String tempoI = corrida.get(0).get(4);
        String tempoF = corrida.get(corrida.size()-1).get(4);

        Timestamp a = new Timestamp(Long.parseLong(tempoI));
        Timestamp b = new Timestamp(Long.parseLong(tempoF));

        int aSec = a.getSeconds();
        int aMin = a.getMinutes()*60;
        int aHor = a.getHours()*60*60;

        int bSec = b.getSeconds();
        int bMin = b.getMinutes()*60;
        int bHor = b.getHours()*60*60;

        int aTotalSec = aSec+aMin+aHor;
        int bTotalSec = bSec+bMin+bHor;

        String mediaVel;


        //rn.setVelocidadeMedia((velMed*3.6)+"");

        rn.setMagnitudes(magnitudes);

        return rn;
    }

    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerView.setHasFixedSize(true);

        binding.recyclerView.setAdapter(new Adapter(this, mapcorridas));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("POS", position);

        startActivity(intent);
    }
}