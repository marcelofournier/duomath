package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;

public class DuoActivity extends AppCompatActivity {

    private static final String ARQUIVO_LOGIN = "Login_dados";
    private FirebaseAuth autenticacao;

    private ProgressBar pgsBar;
    private int i = 0;
    private TextView txtView;
    private Handler hdlr = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        pgsBar = (ProgressBar) findViewById(R.id.progressBar3);
        //txtView = (TextView) findViewById(R.id.campoTexto);
       // Button btn = (Button)findViewById(R.id.botao);


        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = pgsBar.getProgress();
                new Thread(new Runnable() {
                    public void run() {
                        while (i < 100) {
                            i += 1;
                            // Update the progress bar and display the current value in text view
                            hdlr.post(new Runnable() {
                                public void run() {
                                    pgsBar.setProgress(i);
                                    txtView.setText(i+"/"+pgsBar.getMax());
                                }
                            });
                            try {
                                // Sleep for 100 milliseconds to show the progress slowly.
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });



         */

    }



    @Override
    protected void onStart() {
        super.onStart();
        loga();

    }



    public void loga(){
        SharedPreferences dados = getSharedPreferences(ARQUIVO_LOGIN, 0);
        SharedPreferences.Editor editor = dados.edit();

        //carrega os dados de login do arquivo de preferencias

        if (dados.contains("email") ) {

            String email = dados.getString("email", "");
            String senha = dados.getString("senha", "");


            if (!email.isEmpty()) {

                autenticacao.signInWithEmailAndPassword(
                        email,
                        senha
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            finishAffinity();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }else
                        {
                            vaiLogin();
                        }

                    } //fim do onComplete

                });

            } else {

                vaiLogin();

            }


        }else{
            vaiLogin();

        }


    }


    public void vaiLogin(){
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }



}

