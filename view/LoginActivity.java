package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
import com.google.firebase.database.ValueEventListener;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    //private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    //private FirebaseAuth usuario = FirebaseAuth.getInstance();

    private static final String ARQUIVO_LOGIN = "Login_dados";


    private FirebaseAuth autenticacao;
    private DatabaseReference referencia;

    private Switch tipoAcesso;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button   botaoAcessar;
    private TextView queroCadastrar;
    private Aluno aluno;
    private Usuario usuario;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference alunoRef;
    private String nomeField = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Acesso");

        inicializaComponentes();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        referencia = ConfiguracaoFirebase.getFirebaseDatabase();

        queroCadastrar = findViewById(R.id.loginCampoQueroCadastrar);

        queroCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });




        tipoAcesso.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    if ( tipoAcesso.isChecked() ){
                        botaoAcessar.setText("CADASTRAR");
                    }else
                    {
                        botaoAcessar.setText("ENTRAR");
                    }


                    return true;
                }
                return false;

            }
        });


        tipoAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( tipoAcesso.isChecked() ){
                    botaoAcessar.setText("CADASTRAR");
                }else
                {
                    botaoAcessar.setText("ENTRAR");
                }

            }
        });


        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailLocal = campoEmail.getText().toString();
                String senhaLocal = campoSenha.getText().toString();

                if ( !emailLocal.isEmpty() ) {

                    if ( senhaLocal.isEmpty()  ) {
                        Toast.makeText(LoginActivity.this, "Preencha o campo senha", Toast.LENGTH_LONG).show();

                    }else {

                        if ( tipoAcesso.isChecked() ) {  //cadastro

                                autenticacao.createUserWithEmailAndPassword(
                                        emailLocal,
                                        senhaLocal
                                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if ( task.isSuccessful()) {

                                            tipoAcesso.setChecked(false);
                                            botaoAcessar.setText("ENTRAR");

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

                                            Toast.makeText(LoginActivity.this, erroExcecao, Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });




                        }  //login

                            autenticacao.signInWithEmailAndPassword( //LOGIN
                                    emailLocal,
                                    senhaLocal
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if ( task.isSuccessful() ) {

                                        finishAffinity();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                        intent.putExtra("email", emailLocal);
                                        intent.putExtra("nome", nomeField);
                                        startActivity( intent );


                                    }else {

                                        Toast.makeText(LoginActivity.this, "Erro ao fazer o login "
                                                + task.getException(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });




                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Preencha o campo email", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //guardar os dados de login para a proxima sessao
        SharedPreferences dados = getSharedPreferences(ARQUIVO_LOGIN, 0);
        SharedPreferences.Editor editor = dados.edit();

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();
        String vNome  = nomeField;

        editor.putString("email", email);
        editor.putString("senha", senha);
        editor.putString("nome", senha);
        editor.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences dados = getSharedPreferences(ARQUIVO_LOGIN, 0);
        SharedPreferences.Editor editor = dados.edit();

        if (campoSenha.getText().toString().equals("")) {
            //carrega os dados de login do arquivo de preferencias

            if (dados.contains("email")) {

                String email = dados.getString("email", "");
                String senha = dados.getString("senha", "");
                String vNome = dados.getString("nome", "");

                campoEmail.setText(email);
                campoSenha.setText(senha);
                nomeField = vNome;

                /*
                autenticacao.signInWithEmailAndPassword(
                        email,
                        senha
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //finishAffinity();
                            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //startActivity(intent);

                        } else {

                            Toast.makeText(LoginActivity.this, "Erro ao fazer o login "
                                    + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    } //fim do onComplete


                });


                 */
            }

        }


    }



    public void inicializaComponentes(){

        campoEmail = findViewById(R.id.loginCampoEmail);
        campoSenha = findViewById(R.id.loginCampoSenha);
        tipoAcesso = findViewById(R.id.loginTipoAcesso);
        botaoAcessar = findViewById(R.id.loginBotaoAcessar);

        try{
            Bundle dados = getIntent().getExtras();
            String iEmail = dados.getString("email");
            String iSenha = dados.getString("senha");
            String iNome = dados.getString("nome");
            campoEmail.setText(iEmail);
            campoSenha.setText(iSenha);
            nomeField = iNome;

        }catch (Exception e){

        }

   }


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public void mensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}