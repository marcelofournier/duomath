package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Questao;
import br.com.amazonbots.duomath01.tools.DataFormatada;


public class TreinoActivity extends AppCompatActivity {

    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();

    private AdView mAdView;


    private static final int TREINOSMAX = 6;
    private Aluno aluno = null;
    private Aluno robo = null;
    private final Questao questao = null;
    private ProgressBar barraProgressoX;
    private int Progresso = 0;
    private Button botaoContinuar;
    private TextView campoQuestaoX;
    private TextView campoTituloX;
    private TextView campoMensagemPontos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino);

        //getSupportActionBar().setTitle("DuoMath");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adViewTreino);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


        campoQuestaoX = findViewById(R.id.campoQuestaoTreino);
        campoTituloX = findViewById(R.id.treinoCampoTitulo);
        barraProgressoX = findViewById(R.id.progressBar);
        botaoContinuar = findViewById(R.id.botaoContinuar);
        campoMensagemPontos = findViewById(R.id.treinoMensagemPontos);

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );
        String semana = String.valueOf( DataFormatada.semanaAno() );
        DatabaseReference alunos = referencia.child("alunos").child(semana).child(chave);

        alunos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Aluno alunoRecuperado = snapshot.getValue( Aluno.class);
                setAluno(alunoRecuperado);
                atualizaCampos();
                //defineMensagemPontos();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        botaoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   if(botaoContinuar.getText().equals("CONTINUAR")){
                    //atualizaProgresso();

                    int incremento = 2; //this.getAluno().getNivel();

                    Questao questao = new Questao(getAluno().getNivel(), getAluno().getOperacao());

                    campoQuestaoX.setText(questao.getDescricao());

                    setProgresso(getProgresso() + 1);
                    barraProgressoX.setProgress(getProgresso());

                    if(getProgresso()==barraProgressoX.getMax()){

                        if (getAluno().getTreinosHoje() == 0 ) {
                            getAluno().setTreinosHoje(1);
                        }else{
                            getAluno().setTreinosHoje(getAluno().getTreinosHoje() + 1);
                        }


                        getAluno().inicializaDia();

                        if ( getAluno().getPontos() > (getAluno().getMeta() / 2) ) {

                            incrementaPontos(2);
                            //getAluno().setPontos(getAluno().getPontos() + incremento);
                            //getAluno().setPontosSemana(getAluno().getPontosSemana() + incremento);

                            int s = DataFormatada.semanaAno();
                            getAluno().atualizar(s);

                            Snackbar.make(
                                        v,
                                        "Você precisar fazer uma prova agora.",
                                        Snackbar.LENGTH_INDEFINITE
                                ).setAction("Ok, entendi", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ProvaActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).show();
                                botaoContinuar.setEnabled(false);

                        } else{

                            incrementaPontos(2);
                            //getAluno().setPontos(getAluno().getPontos() + incremento);
                            //getAluno().setPontosSemana(getAluno().getPontosSemana() + incremento);
                            //campoPontosHojeX.setText(String.valueOf(getAluno().getPontos()));
                            botaoContinuar.setText("Voltar");
                            botaoContinuar.setEnabled(false);

                            int s = DataFormatada.semanaAno();
                            //getAluno().atualizar(s);

                            if(getAluno().atualizar(s)){

                            }else
                            {
                                mensagem("Erro ao atualizaar dados do aluno");
                            }

                            Intent intent = new Intent(getApplicationContext(), TreinoConcluidoActivity.class);
                            startActivity( intent );
                            finish();
                        }

                    }



                } else {

                    finish();
                }

            }
        });

    }

    //**************************************************************************

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

    //**************************************************************************

    public void incrementaPontos(int incremento){

        int i = getAluno().getTreinosHoje();

        if ( i >= TREINOSMAX ) {
            //mensagem("Você não receberá pontos por este treino!");

        }else
        {
            getAluno().setPontos(getAluno().getPontos() + incremento);
            getAluno().setPontosSemana(getAluno().getPontosSemana() + incremento);
        }


    }

    //**************************************************************************


    @Override
    protected void onPause() {
        super.onPause();
        //salvarDados();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //defineMensagemPontos();
        botaoContinuar.setEnabled(true);
    }


    public int getProgresso() {
        return Progresso;
    }

    public void setProgresso(int progresso) {
        Progresso = progresso;
    }

    public void atualizaCampos(){

        try{
            defineMensagemPontos();
            //campoPontosHojeX.setText(String.valueOf(getAluno().getPontos()));


        }catch (Exception e){

        }

    }

    //**************************************************************************


    public void defineMensagemPontos(){

        int i = getAluno().getTreinosHoje();
        if ( i >= TREINOSMAX ) {

            campoMensagemPontos.setText("Você não receberá pontos por este treino!");

        }else
            campoMensagemPontos.setText("Atenção! Este treino vale 2 pontos.");


    }

    //**************************************************************************


    public void atualizaProgresso(){

        int incremento = 2; //this.getAluno().getNivel();



        Questao questao = new Questao(getAluno().getNivel(), getAluno().getOperacao());
        //Random r = new Random();
        //int indice = r.nextInt(frases.length) + 1;

        //campoTituloX.setText(frases[indice-1]);
        campoQuestaoX.setText(questao.getDescricao());

        setProgresso(getProgresso() + 1);
        barraProgressoX.setProgress(getProgresso());

        if(getProgresso()==barraProgressoX.getMax()){


            getAluno().inicializaDia();

            if ( getAluno().getPontos() > (getAluno().getMeta() / 2) ) {

                getAluno().setPontos(getAluno().getPontos() + incremento);
                getAluno().setPontosSemana(getAluno().getPontosSemana() + incremento);

                int s = DataFormatada.semanaAno();
                getAluno().atualizar(s);

                mensagem("Você precisa testar o que aprendeu para ganhar novos pontos!");
                Intent intent = new Intent(getApplicationContext(), ProvaActivity.class);
                startActivity( intent );
                finish();

            } else{

                getAluno().setPontos(getAluno().getPontos() + incremento);
                getAluno().setPontosSemana(getAluno().getPontosSemana() + incremento);
                //campoPontosHojeX.setText(String.valueOf(getAluno().getPontos()));
                botaoContinuar.setText("Voltar");
                botaoContinuar.setEnabled(false);

                //int i = dao.atualizar(getAluno());
                //int s = DataFormatada.semanaAno();
                //getAluno().atualizar(s);

                int s = DataFormatada.semanaAno();
                //getAluno().atualizar(s);

                if(getAluno().atualizar(s)){

                }else
                {
                    mensagem("Erro ao atualizaar dados do aluno");
                }

                Intent intent = new Intent(getApplicationContext(), TreinoConcluidoActivity.class);
                startActivity( intent );
                finish();
            }

        }

    }
    //*******************************************************************************************

    public void testaMeta(){

        if(this.getAluno().getPontos() >= this.getAluno().getMeta()){
            this.getAluno().setOfensiva(this.getAluno().getOfensiva() + 1);
        }

    }

    //**************************************************************************


    public void salvarDados(){
        getAluno().atualizar();
    }


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }



    public void mensagem(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}