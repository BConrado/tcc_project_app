package com.tcc.tcc_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity3 extends AppCompatActivity implements LocationListener, SensorEventListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMain3Binding binding;

    SensorManager sensorManager;
    Sensor gravitySensor;
    int notificationID = 1;
    Handler handler = new Handler();

    Timer timer;
    Timer shareTimer;
    TimerTask timerTask;
    TimerTask SHARE;
    int Delay = 1000;
    int userDelay = 1;

    boolean NOTIFICACOES = true;
    int VELMAX = 0;

    File file;

    Handler handlerLocation = new Handler();

    Runnable myRunnable = new Runnable() {
        public void run() {
            requestLocation();
            escreveArquivo();
            handlerLocation.postDelayed(this, 1000 * userDelay);
        }
    };

    LocationManager locationManager;
    private static final int GPS_TIME_INTERVAL = 1000;
    private static final int GPS_DISTANCE = 1000;


    FileUtil fileUtil;
    float x,y,z = 0;
    int id;
    Location l = new Location("");

    DataShared dataShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        file = new File(getFilesDir()+ "/data.csv");

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);


        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build());

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
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        dataShared = new ViewModelProvider(this).get(DataShared.class);
        dataShared.init();
        dataShared.sendString("0");
        dataShared.sendVelocity("0");



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
        sensorManager.unregisterListener(this);
        handlerLocation.removeCallbacks(myRunnable);

    }

    public void startAnalises(int delay, int velocidadeMaxima, boolean notificacoes){
        userDelay = delay;
        NOTIFICACOES = notificacoes;
        VELMAX = velocidadeMaxima;
        startGetLocation();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startGetLocation() {
        handlerLocation.postDelayed(myRunnable, 0);
    }

    private void escreveArquivo() {
        float [] array = new float []{x, y, z};
        double a = magnitude(array);

        dataShared.sendString(a+"");
        dataShared.sendVelocity(l.getSpeed()+"");

        float vel = (float) (l.getSpeed() * 3.6);

        if(vel > VELMAX){
            notifySpeed(vel+" LIMITE DE VELOCIDADE EXCEDIDO");
        }


        System.out.println(NOTIFICACOES);
        if(a > 9.9 && NOTIFICACOES){
            notify(a+" - CUIDADO FORCA MAIOR QUE 1G");
            System.out.println(a);
        }

        String texto = id + ";" + l.getLatitude() + ";" + l.getLongitude()+ ";" + l.getSpeed() + ";" + System.currentTimeMillis()/1000 + ";" + x + ";" + y + ";" + z + "\n";
        fileUtil.appendStringToFile(texto, file);
//        System.out.println(texto);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        l = location;
        locationManager.removeUpdates(this);
    }

    private void requestLocation() {
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            }
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}



    private void notify (String msg) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification");

        Intent ii = new Intent(this.getApplicationContext(), MainActivity3.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, PendingIntent.FLAG_IMMUTABLE);

        builder.setContentTitle("ALERT");
        builder.setContentIntent(pendingIntent);
        builder.setContentText(msg+ " m/s2");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompt = NotificationManagerCompat.from(this);

        managerCompt.notify(notificationID, builder.build());
        notificationID++;
    }

    private void notifySpeed (String msg) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification");

        Intent ii = new Intent(this.getApplicationContext(), MainActivity3.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, PendingIntent.FLAG_IMMUTABLE);

        builder.setContentTitle("ALERT");
        builder.setContentIntent(pendingIntent);
        builder.setContentText(msg+ " km/h PASSOU DO LIMITE DE VELOCIDADE");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompt = NotificationManagerCompat.from(this);

        managerCompt.notify(notificationID, builder.build());
        notificationID++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate
            float gravity[] = new float[3];
            float linear_acceleration[]= new float[3];
            final float alpha = 0.8f;




            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            x = linear_acceleration[0];
            y = linear_acceleration[1] ;
            z = linear_acceleration[2];

            //System.out.println("ARRAY = "+linear_acceleration[0] + " " + linear_acceleration[1] + " " + linear_acceleration[2]);





            //System.out.println("Aceleracao raiz = "+ acceleration);

        }
    }

    double magnitude(float[] v) {
        return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }


}