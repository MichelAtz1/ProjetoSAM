package br.desenvolvedor.michelatz.projetosam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import br.desenvolvedor.michelatz.projetosam.Modelo.Comentario;

public class AdapterListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Comentario> itens;

    public static String idSelecionado;

    public AdapterListView(Context context, ArrayList<Comentario> itens) {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount() {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public Comentario getItem(int position) {
        return itens.get(position);
    }

    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        //Pega o item de acordo com a posção.
        Comentario item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.list_item_mensagem, null);

        //atraves do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.txtMensagem)).setText(item.getTexto());
        ((TextView) view.findViewById(R.id.txtUsuario)).setText(item.getNome());
        ((ImageButton) view.findViewById(R.id.btnDelete)).setTag(position);


        if(!PerfilAutonomo.idUsuarioGlobal.equals(item.getIdUsuario())){
            ImageButton btn = (ImageButton) view.findViewById(R.id.btnDelete);
            btn.setEnabled(false);
        }
        return view;
    }

    public void removeItem(int positionToRemove){
        Comentario item = itens.get(positionToRemove);
        idSelecionado = item.getId();
        notifyDataSetChanged();
    }

}
