package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class BateuMetaActivity extends AppCompatActivity {

    private AdView mAdView;

    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Aluno aluno = null;
    private Button botaoContinuar;
    private TextView campoBonus;
    private TextView campoMensagem;
    private ImageView imagemParabens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateu_meta);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewProvaConcluida);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        botaoContinuar  = findViewById(R.id.MetaBatidaBotaoContinuar);
        campoBonus      = findViewById(R.id.MetaBatidaCampoBonus);
        campoMensagem   = findViewById(R.id.MetaBatidaCampoMensagem);
        imagemParabens = findViewById(R.id.bateuMetaFigura);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //dao = new AlunoDAO_SQLite(this);

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );
        String s = String.valueOf( DataFormatada.semanaAno() );

        DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);

        alunos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Aluno alunoRecupedao = (Aluno) snapshot.getValue( Aluno.class );
                setAluno(alunoRecupedao);
                //mensagem("ALUNO RECUPERADO = " + alunoRecupedao.getEmail() + "  " + alunoRecupedao.getPontos() );
                verificaSituacao();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        botaoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        //salvarDados();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //carregaDados();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //carregaDados();
        //aluno.setHoje(DataFormatada.dataAtual());
        //verificaSituacao();
    }



    private void habilitaBotaoContinuar(){
        botaoContinuar.setEnabled(true);
      }


//*******************************************************************************************


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void mensagem(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }



    public void verificaSituacao(){

        if ( aluno != null) {

            String msg = null;

            if ( getAluno().getBateuMetaPontos() == true) {

                imagemParabens.setImageResource(R.drawable.ic_trofeu_amarelo_24);
                msg = "Parabéns! Você atingiu sua meta diária!";

            } else {
                msg = "Parabéns! Você está progredindo!";
                imagemParabens.setImageResource(R.drawable.ic_estrela_amarela_24);
            }

            campoMensagem.setText(msg);

            String m = null;
            if (getAluno().getNivel() == 10) {

                m = "Você ganhou 5 pontos.";

            } else {
                m = "Você ganhou +" + this.getAluno().getNivel() + " pontos.";
            }

            campoBonus.setText(m);
            //salvarDados();

        }else
            //mensagem("Aluno NULL");

        habilitaBotaoContinuar();
    }


}