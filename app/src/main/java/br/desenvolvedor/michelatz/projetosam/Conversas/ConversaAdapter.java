package br.desenvolvedor.michelatz.projetosam.Conversas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.desenvolvedor.michelatz.projetosam.Mensagens.ChatConversa;
import br.desenvolvedor.michelatz.projetosam.R;

/**
 * Created by Michel Atz on 23/06/2017.
 */

public class ConversaAdapter extends RecyclerView.Adapter<ConversaAdapter.HolderConversas>{

    private List<ConversaAtributos> atributosList;
    private Context context;

    public ConversaAdapter(List<ConversaAtributos> atributosList,Context context){
        this.atributosList = atributosList;
        this.context = context;
    }

    @Override
    public HolderConversas onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_conversas,parent,false);
        return new ConversaAdapter.HolderConversas(v);
    }

    @Override
    public void onBindViewHolder(HolderConversas holder, final int position) {
        holder.nome.setText(atributosList.get(position).getNome());
        holder.mensagem.setText(atributosList.get(position).getUltimaMensagem());
        holder.hora.setText(atributosList.get(position).getHora());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatConversa.class);
                i.putExtra("key_receptor",atributosList.get(position).getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class HolderConversas extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView nome;
        TextView mensagem;
        TextView hora;

        public HolderConversas(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAmigos);
            nome = (TextView) itemView.findViewById(R.id.nomeReceptor);
            mensagem = (TextView) itemView.findViewById(R.id.ultimaMensagem);
            hora = (TextView) itemView.findViewById(R.id.horaConversa);
        }
    }

}
