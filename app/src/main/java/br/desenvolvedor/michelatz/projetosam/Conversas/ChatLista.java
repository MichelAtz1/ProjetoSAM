package br.desenvolvedor.michelatz.projetosam.Conversas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.desenvolvedor.michelatz.projetosam.BuscarServico;
import br.desenvolvedor.michelatz.projetosam.CadastrarServico;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.GerenciarUsuario;
import br.desenvolvedor.michelatz.projetosam.InicioUsuario;
import br.desenvolvedor.michelatz.projetosam.ListaServico;
import br.desenvolvedor.michelatz.projetosam.Login;
import br.desenvolvedor.michelatz.projetosam.Mensagens.ChatConversa;
import br.desenvolvedor.michelatz.projetosam.PrincipalActivity;
import br.desenvolvedor.michelatz.projetosam.R;


public class ChatLista extends AppCompatActivity {

    private RecyclerView rv;
    private List<ConversaAtributos> atributosList;
    private ConversaAdapter adapter;
    private String JSON_STRING;

    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lista);

        mToobar  = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Lista de Conversas");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(ChatLista.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(ChatLista.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(ChatLista.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(ChatLista.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(ChatLista.this, ChatLista.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);

        atributosList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.conversaRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new ConversaAdapter(atributosList,this);
        rv.setAdapter(adapter);
        SolicitaJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main_usuario,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.acao_usuario_inicio:
                startActivity(new Intent(this,PrincipalActivity.class));
                finish();
                return true;
            case R.id.acao_usuario_perfil:
                startActivity(new Intent(this,GerenciarUsuario.class));
                finish();
                return true;
            case R.id.acao_usuario_buscar:
                startActivity(new Intent(this,BuscarServico.class));
                finish();
                return true;
            case R.id.acao_usuario_adicionar:
                startActivity(new Intent(this,CadastrarServico.class));
                finish();
                return true;
            case R.id.acao_usuario_listar:
                startActivity(new Intent(this,ListaServico.class));
                finish();
            case R.id.acao_usuario_chat:
                startActivity(new Intent(this,ChatLista.class));
                finish();
                return true;
            case R.id.acao_usuario_sair:
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("idKey");
                editor.remove("nomeKey");

                editor.commit();
                editor.clear();

                startActivity(new Intent(this,Login.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SolicitaJSON(){
        class SolicitaJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                mostraConversas();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUser = sharedpreferences.getString("idKey", null);
                //Log.d("----------->Id User",idUser);
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_CHAT_ID_EMISSOR,idUser);



                AcessoWeb acessoWeb = new AcessoWeb();
                String s = acessoWeb.sendPostRequest(Config.URL_LISTAR_CONVERSAS,hashMap);
                //Log.d("----------->Retorno",s);
                return s;
            }
        }
        SolicitaJSON buscaConvers = new SolicitaJSON();
        buscaConvers.execute();
    }

    private void mostraConversas(){

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String idUsuario = sharedpreferences.getString("idKey", null);
        String nomeUser = sharedpreferences.getString("nomeKey", null);

        String nomeQueVaiParaLista=" ";
        String idQueVaiParaLista=" ";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);


            for(int i = 0; i<result.length(); i++){

                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.KEY_CONVERSA_ID );
                String nomeServico = jo.getString(Config.KEY_CONVERSA_NOME_SERVICO );
                String ultimaMensagem = jo.getString(Config.KEY_CONVERSA_MENSAGEM);
                String nomeEmissor = jo.getString(Config.KEY_CONVERSA_EMISSOR);
                String nomeReceptor = jo.getString(Config.KEY_CONVERSA_RECEPTOR);
                String idEmissor = jo.getString(Config.KEY_CONVERSA_ID_EMISSOR);
                String idServico = jo.getString(Config.KEY_CONVERSA_ID_SERVICO);
                String idReceptor = jo.getString(Config.KEY_CONVERSA_ID_RECEPTOR);

                if(nomeUser.equals(nomeEmissor)){
                    nomeQueVaiParaLista = nomeReceptor;
                }else{
                    nomeQueVaiParaLista = nomeEmissor;
                }

                if(idUsuario.equals(idEmissor)){
                    idQueVaiParaLista = idReceptor;
                }else{
                    idQueVaiParaLista = idEmissor;
                }

                String hora = jo.getString(Config.KEY_CONVERSA_HORARIO);

                ConversaAtributos conversAtributos = new ConversaAtributos();
                conversAtributos.setNomeServicoReceptor(nomeServico + " - "+nomeQueVaiParaLista);
                conversAtributos.setUltimaMensagem(ultimaMensagem);
                conversAtributos.setIdReceptor(idQueVaiParaLista);
                conversAtributos.setHora(hora);
                conversAtributos.setIdMensagem(id);
                conversAtributos.setIdServico(idServico);
                atributosList.add(conversAtributos);
                adapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ConversaAdapter(atributosList,this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        Intent it;
        it = new Intent(this, InicioUsuario.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }
}
