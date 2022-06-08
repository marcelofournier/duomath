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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Questao;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class ProvaActivity extends AppCompatActivity {

    private AdView mAdView;

    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();

    private Aluno aluno = null;
    private Questao questaoF;
    private ProgressBar barraProgressoX;
    //private int progresso = 0;
    private TextView energiaX;
    private TextView questaoX;
    private Button botaoOpcao1X;
    private Button botaoOpcao2X;
    private Button botaoOpcao3X;
    private Button botaoContinuarX;
    private final ArrayList<String> lista_operacoes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adViewProva);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        //getSupportActionBar().setTitle("DuoMath");

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
      // progresso = 0;

        lista_operacoes.add("Adição");
        lista_operacoes.add("Subtração");
        lista_operacoes.add("Multiplicação");
        lista_operacoes.add("Divisão");

        barraProgressoX = findViewById(R.id.licaoBarraProgresso);
        energiaX = findViewById(R.id.licaoCampoEnergia);
        questaoX = findViewById(R.id.licaoCampoQuestao);
        botaoOpcao1X = findViewById(R.id.licaoBotaoOpcao1);
        botaoOpcao2X = findViewById(R.id.licaoBotaoOpcao2);
        botaoOpcao3X = findViewById(R.id.licaoBotaoOpcao3);
        botaoContinuarX = findViewById(R.id.licaoBotaoContinuar);

        botaoOpcao1X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respondeuPrimeiro();
            }
        });


        botaoOpcao2X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respondeuSegundo();
            }
        });

        botaoOpcao3X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respondeuTerceiro();
            }
        });

        botaoContinuarX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(botaoContinuarX.getText().toString().equals("CONTINUAR")){
                    proximaQuestao();
                }else if (botaoContinuarX.getText().toString().equals("VOLTAR")){
                    finish();
                }


            }
        });

//***********************************************************************************

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );

        int semana = DataFormatada.semanaAno();
        String s = String.valueOf(semana);

        DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);

        alunos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Aluno alunoRecupedao = (Aluno) snapshot.getValue( Aluno.class);
                setAluno(alunoRecupedao);
                atualizaCampos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //***********************************************************************************

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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



    public Questao getQuestaoF() {
        return questaoF;
    }

    public void setQuestaoF(Questao questaoF) {
        this.questaoF = questaoF;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void respondeuPrimeiro(){
        respostaDada(1);
    }

    public void respondeuSegundo(){
        respostaDada(2);
    }

    public void respondeuTerceiro(){
        respostaDada(3);
    }



    //***********************************************************************************

    public void atualizaCampos(){

        if(this.getAluno().getEnergia()>0){
                 proximaQuestao();
        }else
        {
            //finishAffinity();
            Intent intent = new Intent(getApplicationContext(), EnergiaZeradaActivity.class);
            startActivity( intent );
            finish();
        }

    }

    //***********************************************************************************

    public void salvarDados(){

        int s = DataFormatada.semanaAno();
        getAluno().getToken();
        getAluno().atualizar(s);
    }

//***********************************************************************************

    public void proximaQuestao(){

        Questao questao = new Questao(getAluno().getNivel(), getAluno().getOperacao());
        habilitaBotoes();
        questaoX.setText(questao.getPergunta());

        try{
            ArrayList<Integer> questoes = new ArrayList<Integer>();
            questoes.add(questao.getRespostaCorreta() );
            questoes.add(questao.getOpcao2() );
            questoes.add(questao.getOpcao3() );
            Collections.shuffle(questoes);

            botaoOpcao1X.setText(String.valueOf(questoes.get(0)));
            botaoOpcao2X.setText(String.valueOf(questoes.get(1)));
            botaoOpcao3X.setText(String.valueOf(questoes.get(2)));
            energiaX.setText(String.valueOf(getAluno().getEnergia()));

        }catch(Exception e){
            e.printStackTrace();
            mensagem("Ocorreu um erro: " + e);
        }
        this.setQuestaoF(questao);
    }

//***********************************************************************************

    public void respostaDada(int botao){

        desabilitaBotoes();
        boolean correto = false;
        int respostaCerta;
        String valorBotao;
        respostaCerta = this.getQuestaoF().getRespostaCorreta();

        switch (botao){

            case 1:
                valorBotao = botaoOpcao1X.getText().toString();
                botaoOpcao1X.setBackgroundResource(R.drawable.background_botao_pressionado);

                if(valorBotao.equals(String.valueOf(respostaCerta))){
                    correto = true;
                }
                break;

            case 2:
                valorBotao = botaoOpcao2X.getText().toString();
                botaoOpcao2X.setBackgroundResource(R.drawable.background_botao_pressionado);

                if(valorBotao.equals(String.valueOf(respostaCerta))){
                    correto = true;
                }
                break;
            case 3:
                valorBotao = botaoOpcao3X.getText().toString();
                botaoOpcao3X.setBackgroundResource(R.drawable.background_botao_pressionado);

                if(valorBotao.equals(String.valueOf(respostaCerta))){
                    correto = true;
                }
                break;
        }

        if( correto ) {
            barraProgressoX.setProgress(barraProgressoX.getProgress() + 1);
        } else
        {
            this.getAluno().setEnergia(getAluno().getEnergia() - 1);

            salvarDados();

            if(barraProgressoX.getProgress()>0){
                barraProgressoX.setProgress(barraProgressoX.getProgress() - 1);
            }

        }

        if( getAluno().getEnergia()==0 ){
            botaoContinuarX.setText("VOLTAR");
            //finishAffinity();
            Intent intent = new Intent(ProvaActivity.this, EnergiaZeradaActivity.class);
            startActivity( intent );
            finish();

        }


        if ( barraProgressoX.getProgress() == barraProgressoX.getMax() ){

            getAluno().inicializaDia();

            if ( (getAluno().getNivel() == 10) ) {

                getAluno().setPontos(getAluno().getPontos() + 5);
                getAluno().setPontosSemana(getAluno().getPontosSemana() + 5);

            }else
            {
                getAluno().setPontos(getAluno().getPontos() + getAluno().getNivel());
                getAluno().setPontosSemana(getAluno().getPontosSemana() + getAluno().getNivel());
            }

            if ( getAluno().getPontos() >= getAluno().getMeta() ) {
                getAluno().setBateuMetaPontos(true);
                getAluno().setBateuMetaAvaliacao(true);

                int dia = DataFormatada.diaSemana();

                switch (dia){

                    case 1:
                            getAluno().setDomingo(true);
                        break;
                    case 2:
                        getAluno().setSegunda(true);
                        break;

                    case 3:
                        getAluno().setTerca(true);
                        break;

                    case 4:
                        getAluno().setQuarta(true);
                        break;

                    case 5:
                        getAluno().setQuinta(true);
                        break;

                    case 6:
                        getAluno().setSexta(true);
                        break;

                    case 7:
                        getAluno().setSabado(true);
                        break;

                }

            }

            salvarDados();
            //finishAffinity();
            Intent intent = new Intent(getApplicationContext(), BateuMetaActivity.class);
            startActivity( intent );
            finish();
        }

    }

//***********************************************************************************

    public void habilitaBotoes(){

        botaoOpcao1X.setEnabled(true);
        botaoOpcao2X.setEnabled(true);
        botaoOpcao3X.setEnabled(true);
        botaoOpcao1X.setBackgroundResource(R.drawable.background_botao_transparente);
        botaoOpcao2X.setBackgroundResource(R.drawable.background_botao_transparente);
        botaoOpcao3X.setBackgroundResource(R.drawable.background_botao_transparente);

        botaoContinuarX.setEnabled(false);
    }

//***********************************************************************************

    public void desabilitaBotoes(){

        botaoOpcao1X.setEnabled(false);
        botaoOpcao2X.setEnabled(false);
        botaoOpcao3X.setEnabled(false);
        botaoContinuarX.setEnabled(true);

    }


    //***********************************************************************************

    public ArrayList<Integer> sorteiaQuestao(){

        //ArrayList<String> cars = new ArrayList<String>();
        ArrayList<Integer> lista = new ArrayList<Integer>();
        lista.add(1);
        lista.add(2);
        lista.add(3);
        Collections.shuffle(lista);

        mensagem("Resultado = " + lista.toString());
        return lista;

    }

//***********************************************************************************

    public void mensagem(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    //***********************************************************************************
}