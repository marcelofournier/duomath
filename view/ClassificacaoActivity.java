package br.com.amazonbots.duomath01.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.adapter.ClassificacaoAdapter;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.model.Aluno;

public class ClassificacaoActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private final DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference pontuacao;
    private RecyclerView recyclerView;
    private ClassificacaoAdapter classificacaoAdapter;
    private final List<Aluno> listaAlunos = new ArrayList<>();
    private Aluno aluno = null;
    private ValueEventListener valueEventListenerClassificacao;
    private ValueEventListener valueEventListenerAluno;

    private float x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classificacao);

        Bundle dados = getIntent().getExtras();

        int sem = dados.getInt("semana");

        getSupportActionBar().setTitle("Pontuação Semana " + String.valueOf(sem));

        recyclerView = findViewById(R.id.classificacaoLista);

        FirebaseAuth autenticacao = FirebaseAuth.getInstance();

        //String chave = Base64Custom.codificarBase64( emailAluno );

        //int semana = DataFormatada.semanaAno();
        String s = String.valueOf(sem);
        DatabaseReference alunos = referencia.child("alunos").child(s);


        Query pontuacao = alunos.orderByChild("pontosSemana");

         pontuacao.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaAlunos.clear();

                for ( DataSnapshot dados: snapshot.getChildren() ) {

                    Aluno a = dados.getValue( Aluno.class );

                    listaAlunos.add(a);
                }

                Collections.reverse(listaAlunos);
                carregaListaPontuacao();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

     }



    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregaListaPontuacao();
    }


    public void carregaListaPontuacao(){

        //configura um adapter
        classificacaoAdapter = new ClassificacaoAdapter( listaAlunos );

        //configura o recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter( classificacaoAdapter  );

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
                sair();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public void sair(){
        finish();
    }

    public boolean onTouchEvent(MotionEvent touchevent){

        switch (touchevent.getAction()){

            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if(x2 < x1) {
                    //Intent i = new Intent(ClassificacaoActivity.this, ClassificacaoActivity.class);
                    //startActivity(i);
                    finish();
                }
                break;
        }

        return false;

    }



}