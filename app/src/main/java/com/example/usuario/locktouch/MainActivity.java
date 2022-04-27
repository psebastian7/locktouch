package com.example.usuario.locktouch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String PREFNAME = "MisPreferencias1";
    public static final String Tiempo = "SeguroHorario";
    public static final String estate="estado";
    public static final String img ="img";
    MqttHelper mqttHelper;
    TextView tvEstado;
    String sHora,sMinutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings1 = getSharedPreferences(MainActivity.estate, MODE_PRIVATE);
        String estado2 = settings1.getString("ESTADO",  "");

        TextView estado3 = (TextView)findViewById(R.id.estado);
        estado3.setText(estado2);


        tvEstado = findViewById(R.id.tvEstado);
        TextView est =(TextView)findViewById(R.id.estado);
        est.getText();
        String esta=est.getText().toString();
        startMqtt();

       if(esta.equals("La puerta esta abierta")){
            ImageView image = (ImageView)findViewById(R.id.image);
            image.setImageResource(R.drawable.open);

        }


        if(esta.equals("La puerta esta asegurada")){
            ImageView image = (ImageView)findViewById(R.id.image);
            image.setImageResource(R.drawable.close);
        }




/*
        SharedPreferences settings5 = getSharedPreferences(MainActivity.img, MODE_PRIVATE);
        int estado5 = settings5.getInt("IMAGEN",  0);

        ImageView estado6 = (ImageView) findViewById(R.id.image);
        estado6.setImageResource(estado5);


*/
        SharedPreferences settings = getSharedPreferences(Tiempo, MODE_PRIVATE);
        int Hora  = settings.getInt("Hora", 00);
        int Minutos = settings.getInt("Minutos", 00);

        if(Hora==0){
            sHora = "00";
        }else{
            if(Hora<10){
                sHora = "0"+String.valueOf(Hora);
            }else{
            sHora = String.valueOf(Hora);}}
        if(Minutos==0){
            sMinutos = "00";
        }else{
            if(Minutos<10){
                sMinutos  = "0"+String.valueOf(Minutos);
            }else{
        sMinutos = String.valueOf(Minutos);}}
        TextView tvTiempo = findViewById(R.id.tvTiempo);
        tvTiempo.setText(sHora + ":" + sMinutos);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        int Horaactual = c.getTime().getHours();
        int minutosactual = c.getTime().getMinutes();

    }



    public void Ponerhorario(View view){
        Intent intent = new Intent(MainActivity.this, HorarioSeguro.class);
        startActivity(intent);
    }

    public void abrirPuerta(View view) {
        Toast.makeText(getApplication(), "La puerta se ha abierto", Toast.LENGTH_SHORT).show();
        TextView estado =findViewById(R.id.estado);
        estado.setText("La puerta esta abierta");


        String topic = "output/led";
        String msg = "ON";

        try {
            mqttHelper.mqttAndroidClient.publish(topic, new MqttMessage(msg.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();

        }
        String estado1 = estado.getText().toString();

        SharedPreferences settings = getSharedPreferences(MainActivity.estate, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ESTADO", estado1);
        editor.commit();


        ImageView image = findViewById(R.id.image);
        image.setImageResource(R.drawable.open);

        /*
        int imagen = image.getImageAlpha();

        SharedPreferences settings4 = getSharedPreferences(MainActivity.img, MODE_PRIVATE);
        SharedPreferences.Editor editor4 = settings4.edit();
        editor4.putInt("IMAGEN", 0);
        editor4.commit();


*/



    }

    public void cerrarPuerta(View view) {
        Toast.makeText(getApplication(), "La puerta se ha asegurado", Toast.LENGTH_SHORT).show();
        TextView estado =findViewById(R.id.estado);
        estado.setText("La puerta esta asegurada");
        String topic = "output/led";
        String msg = "OFF";

        try {
            mqttHelper.mqttAndroidClient.publish(topic, new MqttMessage(msg.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();

        }

        String estado1 = estado.getText().toString();

        SharedPreferences settings = getSharedPreferences(MainActivity.estate, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ESTADO", estado1);
        editor.commit();

        ImageView image = (ImageView)findViewById(R.id.image);
        image.setImageResource(R.drawable.close);


    }
    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                if (topic.equalsIgnoreCase("sensor/golpe"))  tvEstado.setText("Estado: " + mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
