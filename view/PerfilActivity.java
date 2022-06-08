package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.amazonbots.duomath01.R;

public class PerfilActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoSalvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meu_perfil);

        campoNome = findViewById(R.id.perfilCampoNome);
        campoEmail = findViewById(R.id.perfilCampoEmail);
        campoSenha = findViewById(R.id.perfilCampoSenha);
        botaoSalvar = findViewById(R.id.perfilBotaoSalvar);

        campoNome.setEnabled(false);
        campoEmail.setEnabled(false);
        botaoSalvar.setEnabled(false);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Você tentou salvar os dados",  Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.item_voltar:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}
