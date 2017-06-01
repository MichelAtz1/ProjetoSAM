package br.desenvolvedor.michelatz.projetosam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class InicioUsuario extends AppCompatActivity implements ListView.OnItemClickListener{

    private Toolbar mToobar;
    private Toolbar mToobarBotton;
    private ListView listServicosOferecidos;
    private ListView listServicosUtilizados;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio_simples);

        //SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        //String tipoUsuario = sharedpreferences.getString("clienteKey", null);
        //String tipoAutonomo = sharedpreferences.getString("autonomoKey", null);
/*
        if((tipoUsuario.trim().equals("0")) && (tipoAutonomo.trim().equals("0"))) {
            setContentView(R.layout.activity_inicio_simples);

        }else if((tipoUsuario.trim().equals("1")) && (tipoAutonomo.trim().equals("0"))){
            setContentView(R.layout.activity_inicio_usuario);
            listServicosUtilizados = (ListView) findViewById(R.id.listaServicosUtilizados);
            listServicosUtilizados.setOnItemClickListener(this);
            getJSONServicoUtilizado();

        }else if((tipoUsuario.trim().equals("0")) && (tipoAutonomo.trim().equals("1"))){
            setContentView(R.layout.activity_lista_servico);
            listServicosOferecidos = (ListView) findViewById(R.id.listaServicosOferecidos);
            listServicosOferecidos.setOnItemClickListener(this);
            getJSONServicoOferecido();

        }else if((tipoUsuario.trim().equals("1")) && (tipoAutonomo.trim().equals("1"))){
            setContentView(R.layout.activity_inicio_usuarioautonomo);
            listServicosOferecidos = (ListView) findViewById(R.id.listaServicosOferecidos);
            listServicosUtilizados = (ListView) findViewById(R.id.listaServicosUtilizados);
            listServicosUtilizados.setOnItemClickListener(this);
            listServicosOferecidos.setOnItemClickListener(this);
            getJSONServicoOferecido();
            getJSONServicoUtilizado();
        }
*/
        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Tela Inicial");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(InicioUsuario.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(InicioUsuario.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(InicioUsuario.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(InicioUsuario.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(InicioUsuario.this, ChatLista.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);
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
                return true;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,GerenciarListas.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String idServicoSelecionado = map.get(Config.TAG_ID).toString();
        String proximaTela = map.get(Config.TAG_TIPO_TELA).toString();
        intent.putExtra(Config.SERVICO_ID,idServicoSelecionado);
        intent.putExtra(Config.TIPO_TABELA,proximaTela);
        startActivity(intent);
        finish();
    }

    private void getJSONServicoOferecido(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InicioUsuario.this,"Buscando Serviços","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showServicosOferecidos();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUsuarioLogado = sharedpreferences.getString("idKey", null);

                HashMap<String,String> params2 = new HashMap<>();
                params2.put(Config.KEY_USUARIO_IDLOGADO,idUsuarioLogado);

                AcessoWeb rh = new AcessoWeb();
                String s = rh.sendPostRequest(Config.URL_LISTA_SERVICOS, params2);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showServicosOferecidos(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String nomeServicoBD = jo.getString(Config.TAG_NOME_SERVICO);
                String tipoTela = jo.getString(Config.TAG_TIPO_TELA);

                HashMap<String,String> servicos = new HashMap<>();
                servicos.put(Config.TAG_ID,id);
                servicos.put(Config.TAG_NOME_SERVICO,nomeServicoBD);
                servicos.put(Config.TAG_TIPO_TELA,tipoTela);

                list.add(servicos);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                InicioUsuario.this, list, R.layout.list_item_servico_oferecido,
                new String[]{Config.TAG_NOME_SERVICO},
                new int[]{R.id.enderecoDenuncia});

        listServicosOferecidos.setAdapter(adapter);
    }

    private void getJSONServicoUtilizado(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showServicosUtilizados();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUsuarioLogado = sharedpreferences.getString("idKey", null);

                HashMap<String,String> params2 = new HashMap<>();
                params2.put(Config.KEY_USUARIO_IDLOGADO,idUsuarioLogado);

                AcessoWeb rh = new AcessoWeb();
                String s = rh.sendPostRequest(Config.URL_LISTA_SERVICOS_UTILIZADOS, params2);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showServicosUtilizados(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String nomeServico = jo.getString(Config.TAG_NOME_SERVICO);

                HashMap<String,String> servicos = new HashMap<>();
                servicos.put(Config.TAG_ID,id);
                servicos.put(Config.TAG_NOME_SERVICO,nomeServico);

                list.add(servicos);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                InicioUsuario.this, list, R.layout.list_item_servico_utilizados,
                new String[]{Config.TAG_NOME_SERVICO},
                new int[]{R.id.enderecoDenuncia});

        listServicosUtilizados.setAdapter(adapter);
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
