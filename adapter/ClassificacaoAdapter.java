package br.com.amazonbots.duomath01.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.dao.ConfiguracaoFirebase;
import br.com.amazonbots.duomath01.model.Aluno;

public class ClassificacaoAdapter extends RecyclerView.Adapter<ClassificacaoAdapter.MyViewHolder> {


    private final List<Aluno> listaAlunos;
    private final int limite = 30;


    public ClassificacaoAdapter(List<Aluno> lista) {
        this.listaAlunos = lista;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.lista_classificacao_adapter, parent, false);

        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Aluno aluno = listaAlunos.get(position);

            String posicao = String.format("%02d\n", position +1);
            try {

                if (position == 0) {
                    holder.campoTrofeu.setImageResource(R.drawable.ic_trofeu_amarelo_24);
                } else if (position == 1) {
                    holder.campoTrofeu.setImageResource(R.drawable.ic_trofeu_prata_24);
                } else if (position == 2) {
                    holder.campoTrofeu.setImageResource(R.drawable.ic_trofeu_bronze_24);
                } else if ((position >= 3) && (position <= 9)) {
                    holder.campoTrofeu.setImageResource(R.drawable.ic_bandeira_turquesa_24);
                } else if ((position >= 10) && (position <= 24)) {
                    holder.campoTrofeu.setImageResource(R.drawable.ic_bandeira_preta_24);
                } else if (position >= 25)  {
                    holder.campoTrofeu.setImageResource(R.drawable.ic_baseline_gpp_maybe_24);
                }

            }catch (Exception e){

            }

            holder.campoPosicao.setText( posicao );
            holder.campoNome.setText( aluno.getNome() );
            holder.campoPontosSemana.setText( String.valueOf(aluno.getPontosSemana()));
            //holder.campoPontosHoje.setText( String.valueOf(aluno.getPontos()));
            //holder.campoMeta.setText( String.valueOf(aluno.getMeta()));

        //holder.campoTrofeu.setImageResource(R.drawable.ic_trofeu_amarelo_24);

    }

    @Override
    public int getItemCount() {

        if(listaAlunos.size() > limite){
            return limite;
        }
        else
        {
            return listaAlunos.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView campoPosicao, campoNome, campoPontosSemana, campoPontosHoje, campoMeta;
        ImageView campoTrofeu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            campoPosicao = itemView.findViewById(R.id.textAdapterPosicao);
            campoTrofeu = itemView.findViewById(R.id.classificacaoCampoTrofeu);
            campoNome = itemView.findViewById(R.id.textAdapterNome);
            campoPontosSemana = itemView.findViewById(R.id.textAdapterPontosSemana);
            //campoPontosHoje = itemView.findViewById(R.id.textAdapterPontosHoje);
            //campoMeta = itemView.findViewById(R.id.textAdapterMeta);

        }
    }




    }





