package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class EnergiaZeradaActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;


    private final DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference alunosRef;
    private final FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Aluno aluno = null;
    private Button botaoContinuar;
    private TextView comprarEnergia;
    private ValueEventListener valueEventListenerAluno;
    //private AlunoDAO_SQLite dao = null;

    //******************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energia_zerada);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                criarPersonalizadoAd();
            }
        });



        comprarEnergia = findViewById(R.id.EnergiaZeradaCampoComprar);
        botaoContinuar = findViewById(R.id.EnergiaZeradaBotaoContinuar);
        botaoContinuar.setEnabled(false);

        //dao = new AlunoDAO_SQLite(this);

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );
        String s = String.valueOf( DataFormatada.semanaAno() );
        DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);

        //******************************************************************************

        botaoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sair();
            }
        });

        //******************************************************************************

        comprarEnergia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean testa = compraEnergia();

                if ( testa == true) {

                    Snackbar.make(
                            v,
                            "Suas vidas foram recarregadas.",
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok, entendi", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finishAffinity();
                            disparaPublicidade();

                           /* finishAffinity();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            */
                        }
                    }).show();
                } else {

                }


            }
        });

    }

    //******************************************************************************

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        inicializaOuvinte();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //******************************************************************************

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    //******************************************************************************

    public void sair(){

        //referencia.removeEventListener(valueEventListenerAluno);
        finish();

    }


    public boolean compraEnergia(){

        if (getAluno().zerarDados() ) {
            salvarDados();
            botaoContinuar.setEnabled(false);
            return true;
        }else {
                mensagem("Não foi possível reiniciar suas vidas!");
                return false;
            }

    }

    //******************************************************************************
    public void salvarDados(){

        //referencia.removeEventListener(valueEventListenerAluno);
        int s = DataFormatada.semanaAno();
        getAluno().atualizar(s);
    }

    //******************************************************************************
    public void inicializaOuvinte(){

        String email = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( email );

        int semana = DataFormatada.semanaAno();
        String s = String.valueOf(semana);

        alunosRef = referencia.child("alunos").child(s).child(chave);

        valueEventListenerAluno = alunosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if (botaoContinuar.isEnabled()){
                  // finish();
                //}

                if(aluno==null) {

                    Aluno alunoRecuperado = (Aluno) snapshot.getValue(Aluno.class);

                    if (alunoRecuperado != null) {
                        setAluno(alunoRecuperado);

                    }
                    //else {
                      //  aluno.cadastrar(chave, s);
                    //}

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //******************************************************************************

    public void mensagem(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }



    public void criarPersonalizadoAd(){

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd(adRequest);

    }


    public void interstitialAd(AdRequest adRequest){

        InterstitialAd.load(this,"ca-app-pub-4715061950600102/2612488149", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("--->AdMob", "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("--->AdMob", "The ad was dismissed.");

                        Intent intent = new Intent(EnergiaZeradaActivity.this, MainActivity.class);
                        startActivity( intent );


                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("--->AdMob", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("--->AdMob", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("--->AdMob", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

    }
//********************************************************************************



    public void disparaPublicidade() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(EnergiaZeradaActivity.this);
        } else {
            Log.d("--->AdMob", "The interstitial ad wasn't ready yet.");

            Intent intent = new Intent(EnergiaZeradaActivity.this, RecarregarActivity.class);
            startActivity(intent);
        }


    }


    }