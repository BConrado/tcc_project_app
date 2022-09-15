package com.tcc.tcc_project;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements RVInterface{


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
    }

    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerView.setHasFixedSize(true);

        binding.recyclerView.setAdapter(new Adapter(this));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("POS", position);

        startActivity(intent);
    }
}