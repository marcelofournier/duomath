package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.AlunoDAO;
import br.com.amazonbots.duomath01.dao.AvaliacaoDAO;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Avaliacao;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class AvaliacaoActivity extends AppCompatActivity {

    private final DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference alunosRef;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Button botaoEnviar;
    private EditText campoComentario;
    private RatingBar campoAvaliacao;
    private TextView campoAvaliador, campoNota;
    private String nome = null;
    private String email = null;
    private ValueEventListener valueEventListenerAluno;
    private Aluno aluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        Bundle dados = getIntent().getExtras();
        nome = dados.getString("nome");
        email = dados.getString("email");

        botaoEnviar = findViewById(R.id.avaliacaoBotaoEnviar);
        campoComentario = findViewById(R.id.avaliacaoComentario);
        campoAvaliacao = findViewById(R.id.avaliacaoBarraEstrelas);
        campoAvaliador = findViewById(R.id.avaliacaoUsuario);
        campoNota = findViewById(R.id.avaliacaoNota);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //campoAvaliacao.setOnRatingBarChangeListener();

        addListenerOnRatingBar();


        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (campoAvaliacao.getNumStars() == 0) {

                    Snackbar.make(
                            v,
                            "Por favor, dê uma nota ao App.",
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok, entendi", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Intent intent = new Intent(getApplicationContext(), TreinoActivity.class);
                            //startActivity(intent);
                        }
                    }).show();

                } else {

                    String comentario = campoComentario.getText().toString();
                    float n = campoAvaliacao.getRating();
                    //String.valueOf(campoAvaliacao.getRating());

                    Avaliacao avaliacao = new Avaliacao(email, nome, n, comentario);
                    AvaliacaoDAO daoAvaliacao = new AvaliacaoDAO();
                    daoAvaliacao.salvar(avaliacao);

                    if (aluno.getAvaliacoesSemana() == 0) {

                        getAluno().setPontosSemana(getAluno().getPontosSemana() + 10);
                        AlunoDAO daoAluno = new AlunoDAO();

                        aluno.setAvaliacoesSemana(aluno.getAvaliacoesSemana() + 1);
                        int s = DataFormatada.semanaAno();
                        daoAluno.atualizar(aluno, s);

                        Snackbar.make(
                                v,
                                "Você ganhou 10 pontos de bônus!",
                                Snackbar.LENGTH_INDEFINITE
                        ).setAction("Ok, entendi", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();

                    } else {

                        Snackbar.make(
                                v,
                                "Você já recebeu 10 pontos!.",
                                Snackbar.LENGTH_INDEFINITE
                        ).setAction("Ok, entendi", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Intent intent = new Intent(getApplicationContext(), TreinoActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        }).show();

                    }

                }


            }
        });


    }


    public void addListenerOnRatingBar() {
        campoAvaliacao = (RatingBar) findViewById(R.id.avaliacaoBarraEstrelas);
        //txtValorAvaliacao = (TextView) findViewById(R.id.txtValorAvaliacao);

        //se o valor de avaliação mudar,
        //exiba o valor de avaliação atual no resultado (textview) automaticamente
        campoAvaliacao.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float avaliacao, boolean fromUser) {
                campoNota.setText(String.valueOf(avaliacao));
                campoComentario.requestFocus();
            }
        });
    }


    //**************************************************************************************

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }


    //**************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        inicializaOuvinte();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                sair();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sair() {
        finish();
    }


    //********************************************************************

    public void inicializaOuvinte() {

        String email = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64(email);

        int semana = DataFormatada.semanaAno();
        //getAluno().atualizar(semana);
        String s = String.valueOf(semana);

        //DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);
        alunosRef = referencia.child("alunos").child(s).child(chave);

        valueEventListenerAluno = alunosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Aluno alunoRecupedao = (Aluno) snapshot.getValue(Aluno.class);

                if (alunoRecupedao != null) {
                    setAluno(alunoRecupedao);
                    campoAvaliador.setText(aluno.getNome());

                } else {
                    aluno.cadastrar(chave, s);
                    setAluno(alunoRecupedao);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}