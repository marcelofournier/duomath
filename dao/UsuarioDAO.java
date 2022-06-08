package br.com.amazonbots.duomath01.dao;



import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.model.Usuario;

public class UsuarioDAO {


    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth autenticacao = FirebaseAuth.getInstance();


    public UsuarioDAO() {
    }


    public boolean salvar(Usuario usuario) {

        try{
            DatabaseReference usuarios = referencia.child( "usuarios" );
            usuarios.push().setValue( usuario );

        }catch (Exception e){
            Log.e("INFO", "Erro ao salvar dados do usuario " + e.getMessage() );
            return false;
        }
        return true;
    }


    public void cadastrar( Usuario usuario ){

        //DatabaseReference alunos = referencia.child( "alunos" );
        //alunos.push().setValue( aluno );

        DatabaseReference usuarios = referencia.child( "usuarios" );
        usuarios.child(usuario.getChave())
                .setValue( usuario );
        //push()
    }


    public boolean atualizar(Usuario usuario){

        String email = autenticacao.getCurrentUser().getEmail();
        String chave = Base64Custom.codificarBase64( email );

        DatabaseReference usuarios = referencia.child("alunos").child( chave );
        usuarios.setValue( usuario );

        return true;
    }


    public boolean deletar(Usuario usuario) {
        return false;
    }

    public List<Usuario> listar() {
        return null;
    }

    public boolean logado(){
        if ( autenticacao.getCurrentUser() == null ){
            return false;
        } else
            return false;
    }


}
