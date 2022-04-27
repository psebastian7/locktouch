package com.example.usuario.locktouch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class HorarioSeguro extends AppCompatActivity {

    TimePicker tm;
    Button Asignar,Cancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_seguro);

        tm = findViewById(R.id.timePicker);
        Asignar = findViewById(R.id.btnOk);
        Cancelar = findViewById(R.id.btnCancelar);



        Asignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Hour = tm.getCurrentHour();
                int Minute = tm.getCurrentMinute();

                SharedPreferences settings = getSharedPreferences(MainActivity.Tiempo, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("Hora",Hour);
                editor.putInt("Minutos",Minute);
                editor.commit();
                Toast.makeText(getApplication(), "La puerta se cerrara a las "+ Hour+":"+Minute, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HorarioSeguro.this,MainActivity.class);
                startActivity(i);
            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
