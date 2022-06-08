package br.com.amazonbots.duomath01.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class AlunoDAO implements IAlunoDAO {


        private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
        private final FirebaseAuth autenticacao = FirebaseAuth.getInstance();


    public AlunoDAO() {
    }

      @Override
    public boolean salvar(Aluno aluno) {

            try{
                DatabaseReference alunos = referencia.child( "alunos" );
                alunos.push().setValue( aluno );

            }catch (Exception e){
                Log.e("INFO", "Erro ao salvar cados do aluno " + e.getMessage() );
                return false;
            }
            return true;
    }


    public void cadastrar(Aluno aluno){

        //DatabaseReference alunos = referencia.child( "alunos" );
        //alunos.push().setValue( aluno );

        DatabaseReference alunos = referencia.child( "alunos" );
        alunos.child(aluno.getIdUsuario())
                .setValue( aluno );
        //push()
    }

    public void cadastrar(Aluno aluno, String chave, String semana){

        DatabaseReference alunos = referencia.child( "alunos" );
        alunos.child(semana)
                .child(chave)
                .setValue( aluno );
        //push()
    }



    public boolean atualizar(Aluno aluno){

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );

        DatabaseReference alunos = referencia.child("alunos").child(chave);
        alunos.setValue(aluno);

            return true;
        }


    public boolean atualizar(Aluno aluno, int semana){

        String emailAluno = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( emailAluno );
        String s = String.valueOf(semana);

        DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);
        alunos.setValue(aluno);

        return true;
    }


    @Override
    public boolean deletar(Aluno aluno) {

        String chave = Base64Custom.codificarBase64( aluno.getEmail() );
        int semana = DataFormatada.semanaAno();
        String s = String.valueOf(semana);

        DatabaseReference alunosRef = referencia.child("alunos").child(s).child(chave);
        alunosRef.removeValue();

        return false;
    }

    @Override
    public List<Aluno> listar() {
        return null;
    }



    public Aluno obterAluno(String chave, String semana){

        final Aluno[] aluno = {new Aluno()};
            Aluno retorno = new Aluno();

            String s = String.valueOf(semana);
            DatabaseReference alunos = referencia.child("alunos").child(s).child(chave);


        alunos.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    aluno[0] = (Aluno) task.getResult().getValue();

                    retorno.setNome(aluno[0].getNome());
                    retorno.setMeta(aluno[0].getMeta());
                    retorno.setNivel(aluno[0].getNivel());

                }
            }
        });

            return retorno;
        }


        public boolean logado(){
            if ( autenticacao.getCurrentUser() == null ){
                return false;
            } else
                return false;
        }


}
