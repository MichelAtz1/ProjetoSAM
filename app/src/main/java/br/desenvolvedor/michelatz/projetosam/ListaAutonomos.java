package br.desenvolvedor.michelatz.projetosam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.Conversas.ChatLista;

public class ListaAutonomos extends AppCompatActivity implements ListView.OnItemClickListener{

    private String dia, turno, nomeServicoSelecionado, recomendacao;
    private Toolbar mToobar;
    private Toolbar mToobarBotton;
    private ListView listAutonomos;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_autonomos);

        mToobar  = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Lista Autônomos");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(ListaAutonomos.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(ListaAutonomos.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(ListaAutonomos.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(ListaAutonomos.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(ListaAutonomos.this, ChatLista.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);

        Intent intent = getIntent();
        dia = intent.getStringExtra(Config.DIA);
        turno = intent.getStringExtra(Config.TURNO);
        recomendacao = intent.getStringExtra(Config.RECOMENDACAO);
        nomeServicoSelecionado = intent.getStringExtra(Config.NOMESERVICO);

        Log.d("=====> Dia: ",dia);
        Log.d("=====> Turno: ",turno);
        Log.d("=====> Recomendação: ",recomendacao);
        Log.d("=====> Serviço: ",nomeServicoSelecionado);

        listAutonomos = (ListView) findViewById(R.id.listaServicosOferecidos);
        listAutonomos.setOnItemClickListener(this);
        buscarListaServico(nomeServicoSelecionado, turno, dia, recomendacao);
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

    private void buscarListaServico(final String servicoSelecionado, final String turno, final String dia, final String recomendacao) {
        class buscarListaServico extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListaAutonomos.this,"Buscando Dados","Aguarde...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showListagemAutonomos();
            }
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> params2 = new HashMap<>();
                params2.put(Config.KEY_RECOMENDACAO,recomendacao);
                params2.put(Config.KEY_SERVICO_TURNO,turno);
                params2.put(Config.KEY_SERVICO_DIA,dia);
                params2.put(Config.KEY_TIPO_SERVICO_NOME,servicoSelecionado);

                AcessoWeb rh = new AcessoWeb();
                String s = rh.sendPostRequest(Config.URL_LISTA_AUTONOMOS, params2);
                //Log.d("=====> Retornoooo: ",s);
                return s;
            }
        }
        buscarListaServico buscalista = new buscarListaServico();
        buscalista.execute();
    }

    private void showListagemAutonomos(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String idAutonomo = jo.getString(Config.TAG_ID_USUARIO);
                String nomeServicoBD = jo.getString(Config.TAG_NOME_SERVICO);
                String nomeAutonomo = jo.getString(Config.TAG_NOME);
                String numeroRecomendacoes = jo.getString(Config.TAG_RECOMENDACAO);
                HashMap<String,String> autonomos = new HashMap<>();
                autonomos.put(Config.TAG_ID,id);
                autonomos.put(Config.TAG_ID_USUARIO,idAutonomo);
                autonomos.put(Config.TAG_NOME_SERVICO,nomeServicoBD);
                autonomos.put(Config.TAG_NOME,nomeAutonomo);
                autonomos.put(Config.TAG_RECOMENDACAO,numeroRecomendacoes);
                list.add(autonomos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ListaAutonomos.this, list, R.layout.list_item_servico_utilizados,
                new String[]{Config.TAG_NOME_SERVICO,Config.TAG_NOME,Config.TAG_RECOMENDACAO},
                new int[]{R.id.servicoPrestado,R.id.autonomo,R.id.recomendacao});

        listAutonomos.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this,PerfilAutonomo.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String idServicoSelecionado = map.get(Config.TAG_ID).toString();
        String idAutonomo = map.get(Config.TAG_ID_USUARIO).toString();
        String nomeServico = map.get(Config.TAG_NOME_SERVICO).toString();
        String nomeUsuario = map.get(Config.TAG_NOME).toString();
        String recomendacao = map.get(Config.TAG_RECOMENDACAO).toString();

        intent.putExtra(Config.NOMESERVICO,nomeServico);
        intent.putExtra(Config.NOME_USUARIO,nomeUsuario);
        intent.putExtra(Config.ID_AUTONOMO,idAutonomo);
        intent.putExtra(Config.RECOMENDACAO,recomendacao);
        intent.putExtra(Config.SERVICO_ID,idServicoSelecionado);
        intent.putExtra(Config.DIA,dia);
        intent.putExtra(Config.TURNO,turno);

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent it;
        it = new Intent(this, BuscarServico.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }
}
