package br.com.amazonbots.duomath01.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.amazonbots.duomath01.R;

public class ConviteActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private DatabaseReference referencia;

    private Button botao;
    private TextView clicaTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_convite);

        botao = findViewById(R.id.introducaoBotaoCadastrar);
        clicaTexto = findViewById(R.id.introducaoBotaoJaTenhoConta);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity( intent );
            }
        });

        clicaTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity( intent );
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}