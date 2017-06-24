package br.desenvolvedor.michelatz.projetosam.Mensagens;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.desenvolvedor.michelatz.projetosam.Modelo.MensagemDeTexto;
import br.desenvolvedor.michelatz.projetosam.R;

/**
 * Created by Michel Atz on 12/06/2017.
 */

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.MensagensViewHolder> {

    private List<MensagemDeTexto> mensagTexto;
    private Context context;

    public MensagemAdapter(List<MensagemDeTexto> mensagTexto, Context context) {

        this.mensagTexto = mensagTexto;
        this.context = context;
    }

    @Override
    public MensagensViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensagens,parent,false);
        return new MensagensViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensagensViewHolder holder, int position) {

        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.mensagemDigitada.getLayoutParams();

        LinearLayout.LayoutParams balaoMensagem = (LinearLayout.LayoutParams) holder.TvMensagem.getLayoutParams();
        LinearLayout.LayoutParams balaoHora = (LinearLayout.LayoutParams) holder.TvHora.getLayoutParams();

        if(mensagTexto.get(position).getTipoMmensagem()==1){//Quando a mensagem for do EMISSOR
            holder.mensagemDigitada.setBackgroundResource(R.drawable.shape_mensagem_enviada);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            balaoMensagem.gravity = Gravity.RIGHT;
            balaoHora.gravity = Gravity.RIGHT;
            fl.gravity = Gravity.RIGHT;
            holder.TvMensagem.setGravity(Gravity.RIGHT);
        }else if(mensagTexto.get(position).getTipoMmensagem()==2){//Quando a mensagem for do RECEPTOR
            holder.mensagemDigitada.setBackgroundResource(R.drawable.shape_mensagem_recebida);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            balaoMensagem.gravity = Gravity.LEFT;
            balaoHora.gravity = Gravity.LEFT;
            fl.gravity = Gravity.LEFT;
            holder.TvMensagem.setGravity(Gravity.LEFT);
        }

        holder.cardView.setLayoutParams(rl);
        holder.mensagemDigitada.setLayoutParams(fl);
        holder.TvMensagem.setLayoutParams(balaoMensagem);
        holder.TvHora.setLayoutParams(balaoHora);

        holder.TvMensagem.setText(mensagTexto.get(position).getMensagem());
        holder.TvHora.setText(mensagTexto.get(position).getHoraDaMensagem());

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) holder.cardView.getBackground().setAlpha(0);
        else holder.cardView.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));

    }

    @Override
    public int getItemCount() {
        return mensagTexto.size();
    }

    static class MensagensViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        LinearLayout mensagemDigitada;
        TextView TvMensagem;
        TextView TvHora;

        MensagensViewHolder(View itemView){
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cvMensagem);
            mensagemDigitada = (LinearLayout) itemView.findViewById(R.id.balaoMensagem);
            TvMensagem = (TextView) itemView.findViewById(R.id.msTexto);
            TvHora = (TextView) itemView.findViewById(R.id.msHora);
        }
    }
}
