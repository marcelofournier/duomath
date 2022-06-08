package br.com.amazonbots.duomath01.tools;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.model.Aluno;

public class RoboCria implements Runnable {

    private final DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference alunosRef;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Aluno aluno;
    private ValueEventListener valueEventListenerAluno;
    private int numeroRobos = 0;
    private int pontuacaoLimite;


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void setaNumeroRobos(int numRobos) {
        this.numeroRobos = numRobos;
    }

    public int getPontuacaoLimite() {
        return pontuacaoLimite;
    }

    public void setPontuacaoLimite(int pontuacaoLimite) {
        this.pontuacaoLimite = pontuacaoLimite;
    }

    //*********************************************************************

    @Override
    public void run() {

        ArrayList<String> robos = new ArrayList<String>();

        robos.add("carlos.costa@teste.com");
        robos.add("julia.almeira@teste.com");
        robos.add("urano.solar@teste.com");
        robos.add("plutao.god@teste.com");
        robos.add("teles.santos@teste.com");
        robos.add("cintia.amadeus@teste.com");
        robos.add("aluno10.escola10@teste.com");
        robos.add("miguel.tadeu@teste.com");
        robos.add("netuno.solar@teste.com");
        robos.add("frank.silva@teste.com");
        robos.add("luiza.dias@teste.com");
        robos.add("saulo.peixoto@teste.com");
        robos.add("kanoa.sheres@teste.com");
        robos.add("silmara.amaro@teste.com");
        robos.add("saturno.789@teste.com");
        robos.add("brabo.dois@teste.com");
        robos.add("celso.antunes@teste.com");
        robos.add("regina.matos@teste.com");
        robos.add("abreu85740.dias@teste.com");
        robos.add("ouro.detolo@teste.com");
        robos.add("jessica.sampaio@teste.com");
        robos.add("assuncao.david@teste.com");
        robos.add("carlos.santana@teste.com");
        robos.add("lucas.dias@teste.com");
        robos.add("maia.david@teste.com");
        robos.add("alex.nogueira2388@teste.com");
        robos.add("andreia.nunes@teste.com");
        robos.add("lica.manaus@teste.com");
        robos.add("santos.zezinho@teste.com");
        robos.add("renata.lima@teste.com");
        robos.add("jorge.tadeu@teste.com");
        robos.add("carlos.almeida@teste.com");
        robos.add("suzane.pereira@teste.com");
        robos.add("tatiane.brito@teste.com");
        robos.add("bruna.nogueira@teste.com");
        robos.add("zeca.matos@teste.com");
        robos.add("andre.dias@teste.com");
        robos.add("angelo.marco@teste.com");
        robos.add("angela.maria@teste.com");
        robos.add("cininha.silva@teste.com");
        robos.add("renan.carvalhaes@teste.com");
        robos.add("paulo.pimenta@teste.com");
        robos.add("isabel.ferreira@teste.com");
        robos.add("carlos.meireles@teste.com");
        robos.add("almeira.juca@teste.com");
        robos.add("junior.isaias@teste.com");
        robos.add("cristiano.santos@teste.com");
        robos.add("marcelo.saraiva@teste.com");
        robos.add("vinicius.costa@teste.com");
        robos.add("adriana.sarmento@teste.com");
        robos.add("isidora.dias@teste.com");
        robos.add("bianca.almeida@teste.com");
        robos.add("zacarias.zenao@teste.com");
        robos.add("moises.lima@teste.com");
        robos.add("monica.tavares@teste.com");
        robos.add("susiney.costa@teste.com");
        robos.add("frank.marlon@teste.com");
        robos.add("arlete.oliveira@teste.com");
        robos.add("teresa.torres@teste.com");
        robos.add("tabata.amaral@teste.com");
        robos.add("monaliza.silva@teste.com");
        robos.add("castro.silva@teste.com");
        robos.add("raimundo.alves@teste.com");
        robos.add("maria.isabel@teste.com");
        robos.add("raimundo.nonato@teste.com");
        robos.add("cintia.castro@teste.com");
        robos.add("janaina.nascimento@teste.com");
        robos.add("miqueias.lima@teste.com");
        robos.add("oseias.durval@teste.com");
        robos.add("joao.miguel@teste.com");
        robos.add("joao.souza@teste.com");


        for (int i = 1; i <= numeroRobos; i++) {

            Random x = new Random();
            int n = x.nextInt(robos.size());

            String email = robos.get(n);
            String chave = Base64Custom.codificarBase64(email);
            String semana = String.valueOf(DataFormatada.semanaAno());

            String vNome = StringTools.retornaNome(email);
            vNome = vNome.replace(".", " ");
            vNome = (String) StringTools.primeiraMaiuscula(vNome);

            Aluno robo = new Aluno(chave, vNome, email);
            robo.setDiaSemana(DataFormatada.diaSemana());
            robo.setRobo(true);

            Random random = new Random();
            int pontos = random.nextInt( pontuacaoLimite );

            robo.setPontosSemana(pontos);
            robo.cadastrar(chave, semana);
            Log.d("Thread", "Robo lançado: " + robo.getNome() + " " + robo.getPontosSemana() + " " + robo.getRobo() + " " + i);


            try {
                Thread.sleep(10000);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    //*************************************************************************************

    public void removeRobo(String chave, String semana){

        //DatabaseReference robosRef = referencia.child("alunos").child(semana);
        //robo.remover(chave, semana);

    }

    //*************************************************************************************


    public int obterMaiorPontuacao(String semana){

        Aluno aluno = null;
        ArrayList<Aluno> lista = new ArrayList<>();

        String chave = "Y2FybG9zLmNvc3RhQHRlc3RlLmNvbQ==0";
        //DatabaseReference robosRef = referencia.child("alunos").child(semana).child(chave);

        DatabaseReference robosRef = referencia.child("alunos").child(semana);

        //Query query1 = robosRef.orderByChild("pontosSemana").limitToFirst(1);
        //Query query1 = robosRef.orderByChild("pontosSemana").limitToFirst(1);

        Query query = robosRef.child("alunos").orderByChild("pontosSemana").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"

                        Aluno a = dataSnapshot.getValue(Aluno.class);
                        if (a != null){
                            setAluno(a);
                            System.out.println("*******************************************");
                            System.out.println("Robo lançado " + a.toString());
                            System.out.println("*******************************************");
                        }else{
                            System.out.println("*******************************************");
                            System.out.println("Robo lançado é nulo");
                            System.out.println("*******************************************");
                        }

                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /*
        robosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Aluno a = snapshot.getValue(Aluno.class);

                if (a != null){
                    setAluno(a);
                    System.out.println("*******************************************");
                    System.out.println("Robo lançado " + a.toString());
                    System.out.println("*******************************************");
                }else{
                    System.out.println("*******************************************");
                    System.out.println("Robo lançado é nulo");
                    System.out.println("*******************************************");
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        return 330;
    }


}
