package br.com.amazonbots.duomath01.tools;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.model.Aluno;

public class AjustaRobos implements Runnable {

    private final DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference alunosRef;
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private Aluno aluno;
    private List<Aluno> listaRobos = new ArrayList<Aluno>();



//***********************************************************************


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

//***********************************************************************

    public void ajustaPontuacao(String chave){

        int semana = DataFormatada.semanaAno();
        String s = String.valueOf(semana);
        DatabaseReference alunosRobosRef = referencia.child("alunos").child(s);

        Query query;
        query = referencia.child("alunos").child(s).orderByChild("pontosSemana");

        listaRobos.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for ( DataSnapshot s : snapshot.getChildren() ) {
                    int i = 1;
                    Aluno a = s.getValue(Aluno.class);
                    if ( a.getRobo() == true ){

                        //if (a.getEmail().equals("renata.lima@teste.com")){
                            listaRobos.add(a);
                            System.out.println(i + " ROBO lançado = " + a.toString());
                        //}

                    }
                    i++;
                }

                if (listaRobos.size() >= 1) {
                    ajustaPontuacao(listaRobos);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


/*
        alunosRobosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Aluno roboAluno = snapshot.getValue(Aluno.class);

                if( roboAluno != null){
                    System.out.println("*******************************************");
                    System.out.println("Aluno snapshot lançado: " + roboAluno.getNome() + " " + roboAluno.getEmail() + " " + roboAluno.getPontosSemana() );

                    if (roboAluno.getEmail().equals(autenticacao.getCurrentUser().getEmail())){
                        System.out.println("*******************************************");
                        System.out.println("Usuario do sistema alterou pontuacao");
                        System.out.println("*******************************************");

                    }

                    //Random random = new Random();
                    //int pontos = random.nextInt( 9 );
                    //roboAluno.setPontosSemana(roboAluno.getPontosSemana() + pontos);
                    //roboAluno.atualizar(semana);

                    System.out.println("*******************************************");
                }else
                {
                    System.out.println("*******************************************");
                    System.out.println("Aluno NULO");
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


/*

        alunosRobos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Aluno roboAluno = snapshot.getValue(Aluno.class);

                if( roboAluno != null){
                    System.out.println("*******************************************");
                    System.out.println("Aluno snapshot lançado: " + roboAluno.getPontosSemana() + " " + roboAluno.getNome());

                    Random random = new Random();
                    int pontos = random.nextInt( 9 );
                    roboAluno.setPontosSemana(roboAluno.getPontosSemana() + pontos);
                    //roboAluno.atualizar(semana);

                    System.out.println("*******************************************");
                }else
                {
                    System.out.println("*******************************************");
                    System.out.println("Aluno NULO");
                    System.out.println("*******************************************");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

    }


    public void ajustaPontuacao(List<Aluno> robos){

        try {

            for (int i = 0; i <= robos.size(); i++) {

                int pontos = robos.get(i).getPontosSemana();
                int incremento = 6;
                robos.get(i).setPontosSemana(pontos + incremento);

                try{
                    //robos.get(i).atualizar(14);
                }catch (Exception e){

                }

                System.out.println(i + " lançado pós " + robos.get(i).getNome() + " " + pontos + " " + robos.get(i).getPontosSemana());

            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("Thread", "Robo lançado ERRO LISTA VAZIA");

        }

    }



    @Override
    public void run() {

    }

}

