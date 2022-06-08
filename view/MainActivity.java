package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.service.MyFirebaseMessagingService;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Usuario;
import br.com.amazonbots.duomath01.tools.DataFormatada;
import br.com.amazonbots.duomath01.tools.RoboCria;

import static br.com.amazonbots.duomath01.R.drawable.ic_estrela_cheia_24;
import static br.com.amazonbots.duomath01.R.drawable.ic_estrela_vazia_24;

public class MainActivity extends AppCompatActivity {

    private static final String ARQUIVO_LOGIN = "Login_dados";

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    private final DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference alunosRef;
    private DatabaseReference usuarioRef;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private ValueEventListener valueEventListenerAluno;
    private ValueEventListener valueEventListenerUsuario;
    private Aluno aluno = null;
    private Aluno robo = null;
    private Usuario usuario = null;
    private String chave = null;
    private Button botaoTreinoX;
    private Button botaoProva;
    private TextView botaoSelecionaNivel;
    private TextView campoPontosHoje, campoPontosSemanaX, campoNivelX, campoVidasX, campoMetaDiariaX, campoUsuarioX, campoSemana;
    private TextView campoSelecionaNivel, campoSelecionaOperador, campoRelogio;
    private ImageView campoEstrela;
    private AlertDialog alerta;
    private int nivel;
    private TextView campoOperacaoX;
    private final String[] sinais = {"Nulo", "(+)", "(-)", "(x)", "(/)"};
    private final String[] operacoes = {"Nulo", "Adição", "Subtração", "Multiplicação", "Divisão"};
    private boolean bloqueiaRobo = true;
    private float x1, x2, y1, y2;
    private boolean bloqueado = false;
    private int roda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializa adMob

        //MobileAds.initialize(this, "@string/admob_appid");

        inicializaFirebaseMensageiro();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                criarPersonalizadoAd();
            }
        });




        mAdView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Thread t = new Thread(){

            @Override
            public void run(){

                while ( !isInterrupted() ) {

                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int contagemRegressivaHoras     = 23 - DataFormatada.hora();
                                int contagemRegressivaDias      = 7 - DataFormatada.diaSemana();
                                int contagemRegressivaMinutos   = 60 - DataFormatada.minuto();
                                int dia = DataFormatada.diaSemana();
                                String m = "";

                                if (dia == 7 ){

                                        if (DataFormatada.hora() == 23){

                                            m = "Faltam " + contagemRegressivaMinutos + " minutos";

                                        }else if (DataFormatada.hora() == 22){

                                            m = "Faltam " + contagemRegressivaHoras + " hora" + " e " + contagemRegressivaMinutos + " minutos";
                                        }else{
                                            m = "Faltam " + contagemRegressivaHoras + " horas" + " e " + contagemRegressivaMinutos + " minutos";
                                        }


                                }else {

                                        if (contagemRegressivaDias == 1){
                                            m =  "Falta " + contagemRegressivaDias + " dia";
                                        }else
                                        {
                                            m =  "Faltam " + contagemRegressivaDias + " dias";
                                        }


                                }

                                campoRelogio.setText(m);


                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        };

        t.start();



        inicializaComponentes();
        //inicializaOuvintes();

        String email = autenticacao.getCurrentUser().getEmail();
        String chaveId = Base64Custom.codificarBase64(email);
        setChave(chaveId);
        int semana = DataFormatada.semanaAno();
        String s = String.valueOf(semana);
        ArrayList<Aluno> listaRobos = null;

        DatabaseReference robosRef = referencia.child("alunos").child(s);

        usuarioRef = referencia.child("usuarios").child(chaveId);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuarioRecuperado = snapshot.getValue(Usuario.class);

                if (usuarioRecuperado != null){
                    setUsuario(usuarioRecuperado);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);
        alunosRef = referencia.child("alunos").child(s).child(chave);

        valueEventListenerAluno = alunosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Aluno alunoRecupedao = snapshot.getValue(Aluno.class);

                if (alunoRecupedao != null) {
                    setAluno(alunoRecupedao);
                    testaNovaOfensiva();
                    atualizaCampos();
                    //System.out.println("lançado: " + alunoRecupedao.getEmail() + " " + alunoRecupedao.getPontosSemana());
/*
                    if (!bloqueiaRobo){
                        AjustaRobos ajustaRobos = new AjustaRobos();
                        ajustaRobos.ajustaPontuacao("Y2Vsc28uYW50dW5lc0B0ZXN0ZS5jb20=");
                        new Thread( ajustaRobos ).start();
                    }

 */

                } else {

                    try{
                        String n = getUsuario().getNome();
                        Aluno aluno = new Aluno(chaveId, n, email);
                        aluno.cadastrar(chaveId, s);
                    }catch (Exception e){
                        mensagem("Usuário não identificado!");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //mensagem("Não foi possível carregar dados do usuário!");
            }
        });



        campoNivelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionaNivel();
            }
        });


        botaoTreinoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                testaZerarDia(v);
            }

        });



        botaoProva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getAluno().getDiaSemana() != DataFormatada.diaSemana()) {

                    Snackbar.make(
                            v,
                            "Você está iniciando uma nova ofensiva.",
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok, entendi", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ProvaActivity.class);
                            startActivity(intent);
                        }
                    }).show();

                } else {

                    Intent intent = new Intent(getApplicationContext(), ProvaActivity.class);
                    startActivity(intent);

                }


            }
        });


    }


//************************************************************************
/*
        GETTERS ANS SETTERS

 */
//************************************************************************

    public Aluno getRobo() { return robo; }

    public void setRobo(Aluno robo) {
        this.robo = robo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }


//************************************************************************
/*
        METODOS ON

 */
//************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_usuario:
                perfil();
                break;

            case R.id.item_resetar:
                resetar();
                break;

            case R.id.item_selecionar_meta:
                selecionarMeta();
                break;

            case R.id.item_selecionar_nivel:
                selecionaNivel();
                break;

            case R.id.item_selecionar_operador:
                selecionaOperador();
                break;

            case R.id.item_classificacao:
                mostrarClassificacao(DataFormatada.semanaAno());
                break;
            case R.id.item_ranking_anterior:
                mostrarClassificacao(DataFormatada.semanaAno() -1);
                break;

            case R.id.item_compartilhar:
                naoLiberado();
                break;

            case R.id.item_avaliacao:
                avalie();
                break;

            case R.id.item_sobre:
                naoLiberado();
                break;

            case R.id.item_sair:
                sair();
                break;

            case R.id.item_logoff:
                logoff();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        bloqueado = false;
        //new Thread( ajustaRobos ).start();
        //MeuRelogio meuRelogio = new MeuRelogio();
        //new Thread(meuRelogio).start();
        //meuRelogio.run();

        paraTudo();



    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        paraTudo();
        bloqueado = true;
        //alunosRef.removeEventListener(valueEventListenerAluno);
        //usuarioRef.removeEventListener(valueEventListenerUsuario);
    }

    @Override
    protected void onResume() {

        super.onResume();

        //inicializaRelogio();

        if (bloqueiaRobo){
            bloqueiaRobo=false;
        }else
            bloqueiaRobo = true;

        if (paraTudo()){
            botaoTreinoX.setEnabled(false);
            botaoProva.setEnabled(false);
        }else
        {
            botaoTreinoX.setEnabled(true);
            botaoProva.setEnabled(true);
        }


    }


//************************************************************************
/*
        METODOS OUVINTES

 */
//************************************************************************

    public void inicializaOuvintes() {

        String email = autenticacao.getCurrentUser().getEmail();
        String chaveId = Base64Custom.codificarBase64(email);
        setChave(chaveId);
        int semana = DataFormatada.semanaAno();
        String s = String.valueOf(semana);
        ArrayList<Aluno> listaRobos = null;

        DatabaseReference robosRef = referencia.child("alunos").child(s);



        usuarioRef = referencia.child("usuarios").child(chaveId);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuarioRecuperado = snapshot.getValue(Usuario.class);

                if (usuarioRecuperado != null){
                    setUsuario(usuarioRecuperado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);
        alunosRef = referencia.child("alunos").child(s).child(chave);

        valueEventListenerAluno = alunosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Aluno alunoRecupedao = snapshot.getValue(Aluno.class);

                if (alunoRecupedao != null) {
                    setAluno(alunoRecupedao);

                    testaNovaOfensiva();
                    //inicializaPontosDia();
                    atualizaCampos();

                } else {

                    try{
                        String n = getUsuario().getNome();
                        String e = getUsuario().getEmail();
                        String c = Base64Custom.codificarBase64(n);

                        Aluno aluno = new Aluno(chaveId, n, email);
                        aluno.inicializarSemana(n, e);
                        //aluno.recuperarToken();

                        //aluno.cadastrar(chaveId, s);

                    }catch (Exception e){
                        mensagem("Usuário não identificado!");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mensagem("Não foi possível carregar dados do usuário!");
            }
        });



        campoNivelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionaNivel();
            }
        });


        botaoTreinoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                testaZerarDia(v);
            }

        });



        botaoProva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getAluno().getDiaSemana() != DataFormatada.diaSemana()) {

                    Snackbar.make(
                            v,
                            "Você está iniciando uma nova ofensiva.",
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok, entendi", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ProvaActivity.class);
                            startActivity(intent);
                        }
                    }).show();

                } else {

                    Intent intent = new Intent(getApplicationContext(), ProvaActivity.class);
                    startActivity(intent);

                }


            }
        });

}

//************************************************************************
/*
        METODOS PUBLICOS

 */
//************************************************************************

    public void criarPersonalizadoAd(){

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd(adRequest);

    }


    public void interstitialAd(AdRequest adRequest){

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
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

                        Intent intent = new Intent(MainActivity.this, RecarregarActivity.class);
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



    //************************************************************************
     public void inicializaComponentes(){

        //getSupportActionBar().setTitle("Duo");
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        botaoTreinoX = findViewById(R.id.botaoTreino);
        botaoProva = findViewById(R.id.botaoProva);
        campoPontosHoje = findViewById(R.id.mainCampoPontosHoje);
        campoPontosSemanaX = findViewById(R.id.mainCampoPontosSemana);
        campoNivelX = findViewById(R.id.mainCampoNivel);
        campoVidasX = findViewById(R.id.mainCampoVidas);
        campoMetaDiariaX = findViewById(R.id.mainCampoMetaDiaria);
        campoOperacaoX = findViewById(R.id.mainCampoOperador);
        campoEstrela = findViewById(R.id.mainCampoEstrela);
        campoSelecionaNivel = findViewById(R.id.campoAlerta);
        campoSelecionaOperador = findViewById(R.id.campoAlerta);
        campoUsuarioX = findViewById(R.id.mainCampoUsuario);
        campoSemana = findViewById(R.id.mainCampoSemana);
        campoRelogio = findViewById(R.id.mainCampoRelogio);

    }

    //************************************************************************

    public void inicializaMenus() {


    }

    //************************************************************************



    public void testaZerarDia(View v) {
        if (getAluno().getDiaSemana() != DataFormatada.diaSemana()) {

            Snackbar.make(
                    v,
                    "Você está iniciando uma nova ofensiva.",
                    Snackbar.LENGTH_INDEFINITE
            ).setAction("Ok, entendi", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TreinoActivity.class);
                    startActivity(intent);
                }
            }).show();

        } else {

            Intent intent = new Intent(getApplicationContext(), TreinoActivity.class);
            startActivity(intent);

        }

    }


//************************************************************************

    public void logoff() {

        autenticacao.signOut();
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    //************************************************************************

    public void testaNovaOfensiva() {

        if (getAluno().getDiaSemana() != DataFormatada.diaSemana()) {

            getAluno().zerarDados();
            getAluno().setDiaSemana(DataFormatada.diaSemana());

            // mensagem("NOVA OFENSIVA");
        }

    }

//************************************************************************

    public void verificaUsuarioLogado() {

        if (autenticacao.getCurrentUser() == null) {
            mensagem("Usuario não esta logado!");
            finish();
        } else {
            String email = autenticacao.getCurrentUser().getEmail();
            mensagem("Dados do usuario logado: " + email);
        }


    }

    //************************************************************************

    public void progressoBarra() {

        Intent intent = new Intent(getApplicationContext(), DuoActivity.class);
        startActivity(intent);

    }


//************************************************************************

    public void mostrarClassificacao(int semana) {
        Intent intent = new Intent(getApplicationContext(), ClassificacaoActivity.class);
        intent.putExtra("semana", semana);
        startActivity(intent);
    }

    /*
    **********************************************************************
    Metodo resetar
    Objetivo:
    **********************************************************************
     */
    public void resetar() {

        String titulo = "Alerta";
        String msg = "Deseja recarregar vidas e reiniciar pontos?";
        AlertDialog alertaX;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_interrogacao_verde_24);

        builder.setPositiveButton("Não", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                getAluno().zerarDados();
                getAluno().atualizar(DataFormatada.semanaAno());
                //mensagem("Pontuação reiniciada com sucesso!");
                disparaPublicidade();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    /*
**********************************************************************
Metodo
Objetivo:
**********************************************************************
 */
    public void selecionaOperador() {

        //Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("( + ) Adição ");
        itens.add("( - ) Subtração");
        itens.add("( x ) Multiplicação");
        itens.add("( / ) Divisão ");

        //adapter utilizando um layout customizado (TextView)
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o operador desejado: ");

        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                switch (arg1) {
                    case 0:
                        getAluno().setOperacao(1);
                        break;
                    case 1:
                        getAluno().setOperacao(2);
                        break;
                    case 2:
                        getAluno().setOperacao(3);
                        break;
                    case 3:
                        getAluno().setOperacao(4);
                        break;
                }

                //getAluno().zerarDados();

                getAluno().atualizar(DataFormatada.semanaAno());
                alerta.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();

    }


    /*
    **********************************************************************
    Metodo atualizaCampos
    Objetivo:
    **********************************************************************
     */
    public void atualizaCampos() {

        campoMetaDiariaX.setText(String.valueOf(getAluno().getMeta()));
        campoPontosSemanaX.setText(String.valueOf(getAluno().getPontosSemana()));
        campoPontosHoje.setText(String.valueOf(getAluno().getPontos()));
        campoNivelX.setText(String.valueOf(getAluno().getNivel()));
        campoVidasX.setText(String.valueOf(getAluno().getEnergia()));
        campoOperacaoX.setText(sinais[getAluno().getOperacao()]);
        campoUsuarioX.setText(getAluno().getNome());

        String semana;
        String domingo;
        String segunda;
        String terca;
        String quarta;
        String quinta;
        String sexta;
        String sabado;

        if (getAluno().isDomingo() == true) {
            domingo = "D*";
        } else
            domingo = "D_";

        if (getAluno().isSegunda() == true) {
            segunda = "S*";
        } else
            segunda = "S_";

        if (getAluno().isTerca() == true) {
            terca = "T*";
        } else
            terca = "T_";

        if (getAluno().isQuarta() == true) {
            quarta = "Q*";
        } else
            quarta = "Q_";

        if (getAluno().isQuinta() == true) {
            quinta = "Q*";
        } else
            quinta = "Q_";

        if (getAluno().isSexta() == true) {
            sexta = "S*";
        } else
            sexta = "S_";

        if (getAluno().isSabado() == true) {
            sabado = "S*";
        } else
            sabado = "S_";

        semana = domingo + segunda + terca + quarta + quinta + sexta + sabado;

        campoSemana.setText(semana);

        if (getAluno().getPontos() >= getAluno().getMeta()) {

            campoEstrela.setBackgroundResource(ic_estrela_cheia_24);

        } else
            campoEstrela.setBackgroundResource(ic_estrela_vazia_24);

    }


    /*
    **********************************************************************
    Metodo
    Objetivo:
    **********************************************************************
     */
    public void naoLiberado() {

        //mensagem("Recurso não liberado pelo desenvolvedor no momento.");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alerta do Aplicativo");
        alert.setMessage("Recurso não liberado pelo desenvolvedor.");
        alert.setIcon(R.drawable.ic_nao_liberado_24);
        alert.setPositiveButton("OK", null);
        alert.show();

        //getAluno().recuperarToken();
        //getAluno().atualizar(DataFormatada.semanaAno());

        //getAluno().inicializarSemana(getAluno().getNome(), getAluno().getEmail());


        //AjustaRobos ajustaRobos = new AjustaRobos();
        //ajustaRobos.ajustaPontuacao("cmVuYXRhLmxpbWFAdGVzdGUuY29t");
        //new Thread(ajustaRobos);

        /*
        RoboCria criaRobos = new RoboCria();
        criaRobos.setaNumeroRobos(3);
        criaRobos.setPontuacaoLimite(DataFormatada.diaSemana() * 80);
        new Thread( criaRobos ).start();
         */

        //disparaPublicidade();


    }


    /*
    **********************************************************************
    Metodo
    Objetivo:
    **********************************************************************
     */
    public void mensagem(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /*
    **********************************************************************
    Metodo
    Objetivo:
    **********************************************************************
     */
    public void selecionarMeta() {
        alteraMeta();
    }


    /*
    **********************************************************************
    Metodo
    Objetivo:
    **********************************************************************
     */
    public void selecionaNivel() {

        //Lista de itens
        ArrayList<Integer> itens = new ArrayList<Integer>();
        itens.add(1);
        itens.add(2);
        itens.add(3);
        itens.add(4);
        itens.add(5);
        itens.add(6);
        itens.add(7);
        itens.add(8);
        itens.add(9);
        itens.add(10);

        //adapter utilizando um layout customizado (TextView)
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o nivel desejado: ");

        builder.setIcon(R.drawable.ic_nao_liberado_24);

        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                nivel = itens.get(arg1);

                getAluno().setNivel(nivel);
                //getAluno().zerarDados();
                getAluno().atualizar(DataFormatada.semanaAno());
                alerta.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();

    }


    /*
    **********************************************************************
    Metodo
    Objetivo:
    **********************************************************************
     */

    public void sair() {

        AlertDialog alertaX;
        int respostaAlerta = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alerta");
        builder.setMessage("Deseja encerrar o App?");
        builder.setIcon(R.drawable.ic_interrogacao_verde_24);//ic_nao_liberado_24

        builder.setPositiveButton("Não", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                autenticacao.signOut();
                finish();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    /*
    **********************************************************************
    Metodo
    Objetivo:
    **********************************************************************

     */

    public void alteraMeta() {

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(50);
        seek.setKeyProgressIncrement(1);
        seek.setProgress(Integer.parseInt(String.valueOf(campoMetaDiariaX.getText())));

        popDialog.setIcon(R.drawable.ic_baseline_tour_24);
        popDialog.setTitle("Selecione nova meta diária.");
        popDialog.setView(seek);
        popDialog.show();

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                campoMetaDiariaX.setText(String.valueOf(seek.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                getAluno().setMeta(seek.getProgress());
                //getAluno().zerarDados();
                int s = DataFormatada.semanaAno();
                getAluno().atualizar(s);
                mensagem("Você alterou sua meta diária de pontos");
            }
        });

    }

    //************************************************************************


    public void inicializaPontosDia() {
        getAluno().inicializaDia();
    }

    //************************************************************************

    public void avalie() {
        Intent intent = new Intent(getApplicationContext(), AvaliacaoActivity.class);
        intent.putExtra("email", getAluno().getEmail());
        intent.putExtra("nome", getAluno().getNome());
        startActivity(intent);
    }

    //************************************************************************

    public void perfil() {
        Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
        startActivity(intent);
    }


    //************************************************************************

    public boolean onTouchEvent(MotionEvent touchevent) {

        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if (x2 < x1) {
                    Intent i = new Intent(MainActivity.this, ClassificacaoActivity.class);
                    i.putExtra("email", getAluno().getEmail());
                    i.putExtra("nome", getAluno().getNome());
                    i.putExtra("semana", DataFormatada.semanaAno());
                    startActivity(i);
                }
                break;
        }

        return false;

    }

//************************************************************************

    public void inicializaRelogio(){

        int contagemRegressivaHoras = 23 - DataFormatada.hora();
        int contagemRegressivaDias = 7 - DataFormatada.diaSemana();
        int contagemRegressivaMinutos = 59 - DataFormatada.minuto();
        int dia = DataFormatada.diaSemana();
        String m = "";

        if (dia == 5 ){

            if (24 - DataFormatada.hora() < 1){

                m = "Faltam " + contagemRegressivaMinutos + " minutos";

            }else{
                m = "Faltam " + contagemRegressivaHoras + " hora(s)" + " e " + contagemRegressivaMinutos + " minutos";
            }


        }else {

           m =  "Faltam " + contagemRegressivaDias + " dia(s)";
        }

        campoRelogio.setText(m);

    }

    //************************************************************************


    public boolean paraTudo() {

        if (DataFormatada.diaSemana() == 7) {

            if (DataFormatada.hora() == 23) {

                if ((DataFormatada.minuto() >= 55) && (DataFormatada.minuto() <= 59)) {

                    mensagem("Sistema bloqueado para fechamento semanal");

                    return true;
                }
            }
        }

            return false;

    }

//************************************************************************

    public void atualizaToken(){
        try{
            if(getAluno().getToken().equals("")) {
                getAluno().recuperarToken();
                getAluno().atualizar(DataFormatada.semanaAno());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //************************************************************************


    public void inicializaFirebaseMensageiro(){

    }

    //************************************************************************

    public void disparaPublicidade(){

        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("--->AdMob", "The interstitial ad wasn't ready yet.");

            Intent intent = new Intent(MainActivity.this, RecarregarActivity.class);
            startActivity( intent );
        }


    }


}
