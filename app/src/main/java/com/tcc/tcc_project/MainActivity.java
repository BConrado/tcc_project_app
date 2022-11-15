package com.tcc.tcc_project;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

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
                startActivity(intent);
            }
        });


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