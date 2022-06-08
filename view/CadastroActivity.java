package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Usuario;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference referencia;

    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button botao;
    private TextView cliqueTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        referencia = ConfiguracaoFirebase.getFirebaseDatabase();

        campoNome = findViewById(R.id.cadastroCampoNome);
        campoEmail = findViewById(R.id.cadastroCampoEmail);
        campoSenha = findViewById(R.id.cadastroCampoSenha);
        botao = findViewById(R.id.cadastroBotaoCadastrar);
        cliqueTexto = findViewById(R.id.cadastroCampoTenhoConta);


        cliqueTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity( intent );
                finish();
            }
        });

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });


    }


    public void cadastrar(){

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();
        String nome = campoNome.getText().toString();

        if ( !email.isEmpty() ) {

            if ( senha.isEmpty()  ) {
                Toast.makeText(getApplicationContext(), "Preencha o campo senha", Toast.LENGTH_LONG).show();

            }else {

                    autenticacao.createUserWithEmailAndPassword(
                            email,
                            senha
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if ( task.isSuccessful()) {

                                //DatabaseReference alunos = referencia.child( "alunos" );
                                int semana = DataFormatada.semanaAno();
                                String s = String.valueOf(semana);

                                String idUsuario = Base64Custom.codificarBase64( email );
                                Aluno aluno = new Aluno(idUsuario, nome, email);
                                aluno.setIdUsuario( idUsuario );
                                aluno.recuperarToken();
                                aluno.cadastrar(idUsuario, s);

                                Usuario usuario = new Usuario();
                                usuario.setEmail(aluno.getEmail());
                                usuario.setNome(aluno.getNome());
                                usuario.setChave(aluno.getIdUsuario());
                                usuario.cadastrar();

                                Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                                intent.putExtra("email", email);
                                intent.putExtra("senha", senha);
                                intent.putExtra("nome", nome);

                                startActivity( intent );
                                finish();


                            }else {

                                String erroExcecao = "";

                                try {
                                    throw task.getException();
                                } catch ( FirebaseAuthWeakPasswordException e) {
                                    erroExcecao = "Senha muito fraca!";
                                } catch ( FirebaseAuthInvalidCredentialsException e){
                                    erroExcecao = "Informe um email válido!";

                                } catch ( FirebaseAuthUserCollisionException e){
                                    erroExcecao = "Este email já está cadastrado!";

                                } catch ( Exception e){
                                    erroExcecao = "Erro ao cadastrar usuario!"  + e.getMessage();
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(), erroExcecao, Toast.LENGTH_SHORT).show();

                            }

                        }
                    });



             }

        }else{
            Toast.makeText(getApplicationContext(), "Preencha o campo email", Toast.LENGTH_LONG).show();
        }




    }



}