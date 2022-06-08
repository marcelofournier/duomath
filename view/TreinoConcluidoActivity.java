package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class TreinoConcluidoActivity extends AppCompatActivity {

    //private AlunoDAO_SQLite dao = null;

    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Aluno aluno = null;
    private Button botaoContinuar;
    private TextView campoPontosGanhos;
    private TextView campoPontosHoje;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_concluido);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adViewTreinoConcluido);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        //getSupportActionBar().setTitle("DuoMath");

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //dao = new AlunoDAO_SQLite(this);

        campoPontosGanhos = findViewById(R.id.TreinoConcluidoCampoPontosGanhos);
        campoPontosHoje = findViewById(R.id.TreinoConcluidoCampoPontosHoje);
        botaoContinuar = findViewById(R.id.TreinoConcluidoBotaoContinuar);
        botaoContinuar.setEnabled(false);


        botaoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );

        int semana = DataFormatada.semanaAno();
        //getAluno().atualizar(semana);
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


    @Override
    protected void onResume() {
        super.onResume();
        //carregaDados();
        //aguarda(3000);
        botaoContinuar.setEnabled(true);
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

     public void atualizaCampos(){

        int incremento;

        if (getAluno().getTreinosHoje() >= 6){
            incremento = 0;
        }else{
            incremento = 2;
        }

         campoPontosGanhos.setText("Treino concluído: +" + incremento + " pontos.");
         campoPontosHoje.setText("Pontos ganhos hoje: " + getAluno().getPontos());

     }



    public void mensagem(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}