package br.com.amazonbots.duomath01.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.amazonbots.duomath01.R;

public class NotificacaoActivity extends AppCompatActivity {

    private Button botaoIniciar;
    private Button botaoCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);

        /*
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(R.drawable.ic_baseline_favorite_24);

         */

        botaoIniciar = findViewById(R.id.notificacaoBotaoIniciar);
        botaoCancelar = findViewById(R.id.notificacaoBotaoNao);


        botaoIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar();
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

    }

    public void cancelar(){
        finish();
    }

    public void iniciar(){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity( intent );
        finish();
    }



}