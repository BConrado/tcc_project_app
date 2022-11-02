package com.tcc.tcc_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;

import android.widget.Toast;

import com.tcc.tcc_project.databinding.ActivityMain3Binding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity3 extends AppCompatActivity implements SensorEventListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMain3Binding binding;
    Timer timer;
    TimerTask timerTask;
    int Delay = 1000;
    SensorManager sensorManager;
    Sensor gravitySensor;
    int notificationID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void goBack(){
        Intent intent = new Intent(MainActivity3.this, MainActivity.class);
        startActivity(intent);
    }

    public void stopTimer(){
        timer.cancel();
        timer.purge();
        sensorManager.unregisterListener(this);
    }

    public void startAnalises(int delay, int velocidadeMaxima, boolean notificacoes){
        startTimer(delay, velocidadeMaxima, notificacoes);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startTimer(int delay, int velocidadeMaxima, boolean notificacoes) {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buscarInformacoesGPS();

                    }
                });
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, Delay*delay);
    }

    public void buscarInformacoesGPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;
        }

        LocationManager mLocManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        LocationListener mLocListener = new MinhaLocalizacaoListener();

        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);

        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(MinhaLocalizacaoListener.longitude != 0 && MinhaLocalizacaoListener.latitude != 0){
                System.out.println(MinhaLocalizacaoListener.longitude+" / "+MinhaLocalizacaoListener.latitude);
                Toast.makeText(this, MinhaLocalizacaoListener.longitude+" / "+MinhaLocalizacaoListener.latitude, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "CARREGANDO.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "GPS DESABILITADO.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        double acceleration = magnitude(event.values);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification");
        builder.setContentTitle("ALERT");
        builder.setContentText(acceleration+ " m/s2");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        builder.setAutoCancel(true);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        NotificationManagerCompat managerCompt = NotificationManagerCompat.from(this);

        managerCompt.notify(notificationID, builder.build());
        notificationID++;


    }

    double magnitude(float[] v) {
        return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}