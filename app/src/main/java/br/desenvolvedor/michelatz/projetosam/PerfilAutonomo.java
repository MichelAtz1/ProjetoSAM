package br.desenvolvedor.michelatz.projetosam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.Modelo.Mensagem;

public class PerfilAutonomo extends AppCompatActivity {

    private Toolbar mToobar;
    private Toolbar mToobarBotton;
    private String id, nomeServico, nomeAutonomo, recomendacao, dia, turno;

    private TextView edtNome;
    private TextView edtTurnoDisponivel;
    private TextView edtServico;
    private TextView edtDiasDisponiveis;
    private EditText edtComentario;

    private String JSON_STRING;
    private ListView listView;
    private AdapterListView adapterListView;
    private ArrayList<Mensagem> itens;
    public static String idUsuarioGlobal = "Isso é uma global";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_autonomo);

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.SERVICO_ID);
        nomeServico = intent.getStringExtra(Config.NOMESERVICO);
        nomeAutonomo = intent.getStringExtra(Config.NOME_USUARIO);
        recomendacao = intent.getStringExtra(Config.RECOMENDACAO);
        dia = intent.getStringExtra(Config.DIA);
        turno = intent.getStringExtra(Config.TURNO);

        edtNome = (TextView) findViewById(R.id.txtNomeAutonomo);
        edtServico = (TextView) findViewById(R.id.txServico);
        edtDiasDisponiveis = (TextView) findViewById(R.id.txDia);
        edtTurnoDisponivel = (TextView) findViewById(R.id.txTurno);
        edtComentario = (EditText) findViewById(R.id.edtComentario);

        showDados(id,nomeServico,nomeAutonomo,recomendacao, dia, turno);

        mToobar  = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Dados do Autônomo");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(PerfilAutonomo.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(PerfilAutonomo.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(PerfilAutonomo.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(PerfilAutonomo.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(PerfilAutonomo.this, PrincipalActivity.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);

        buscaMensagens();
        listView = (ListView) findViewById(R.id.listViewMensagens);
    }

    private void showDados(String id, String nomeServico, String nomeAutonomo, String recomendacao, String dia, String turno) {
        String diaString = " ";
        String turnoString = " ";


            if (turno.equals("0")) {
                turnoString = "Qualquer Turno";
            } else if (turno.equals("1")) {
                turnoString = "Manha";
            } else if (turno.equals("2")) {
                turnoString = "Tarde";
            } else if (turno.equals("3")) {
                turnoString = "Noite";
            }

            if (dia.equals("0")) {
                diaString = "Qualquer Dia";
            } else if (dia.equals("1")) {
                diaString = "Segunda à sexta";
            } else if (dia.equals("2")) {
                diaString = "Finais de Semana";
            }

        edtNome.setText("  Nome Autônomo:    "+nomeAutonomo);
        edtServico.setText("  Recomendações: "+recomendacao+"       Serviço: "+nomeServico);
        edtDiasDisponiveis.setText("  Dias Disponíveis: "+diaString);
        edtTurnoDisponivel.setText("  Turno Disponível: "+turnoString);
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
                startActivity(new Intent(this,PrincipalActivity.class));
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

    public void addComentario(View v){
        adicionarComentario();
    }

    private void adicionarComentario(){

        final String comentario = edtComentario.getText().toString();


        class adicionarComentario extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PerfilAutonomo.this,"Salvando Comentário...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(PerfilAutonomo.this,s,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PerfilAutonomo.this, PerfilAutonomo.class);

                intent.putExtra(Config.NOMESERVICO,nomeServico);
                intent.putExtra(Config.NOME_USUARIO,nomeAutonomo);
                intent.putExtra(Config.RECOMENDACAO,recomendacao);
                intent.putExtra(Config.SERVICO_ID,id);
                intent.putExtra(Config.DIA,dia);
                intent.putExtra(Config.TURNO,turno);

                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                final String idUser = sharedpreferences.getString("idKey", null);
                final String nomeUser = sharedpreferences.getString("nomeKey", null);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_USUARIO_NOME,nomeUser);
                hashMap.put(Config.KEY_USUARIO_IDLOGADO,idUser);
                hashMap.put(Config.KEY_SERVICO_MENSAGEM,comentario);
                hashMap.put(Config.KEY_SERVICO_IDSERVICO,id);

                AcessoWeb acesso = new AcessoWeb();
                String s = acesso.sendPostRequest(Config.URL_INSERIR_MENSAGEM,hashMap);

                return s;
            }
        }

        adicionarComentario ue = new adicionarComentario();
        ue.execute();
    }

    private void buscaMensagens(){
        class buscaMensagens extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PerfilAutonomo.this,"Buscando Dados","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                mostraListaMensagens();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUser = sharedpreferences.getString("idKey", null);
                idUsuarioGlobal = idUser;
                AcessoWeb acesso = new AcessoWeb();
                String s = acesso.sendGetRequestParam(Config.URL_BUSCA_MENSAGENS,id);
                JSON_STRING = s;
                return s;
            }
        }
        buscaMensagens buscaMens = new buscaMensagens();
        buscaMens.execute();
    }

    private void mostraListaMensagens(){

        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        itens = new ArrayList<Mensagem>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String idMensagem = jo.getString(Config.TAG_ID);
                String mensagem = jo.getString(Config.TAG_COMENTARIO);
                String nome = jo.getString(Config.TAG_NOME);
                String idUsuario = jo.getString(Config.TAG_ID_USUARIO);
                String idServico = jo.getString(Config.TAG_ID);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Config.TAG_ID,idMensagem);
                employees.put(Config.TAG_COMENTARIO,"  "+mensagem);
                employees.put(Config.TAG_NOME,"  "+nome);
                employees.put(Config.TAG_ID_USUARIO,idUsuario);
                employees.put(Config.TAG_ID_SERVICO,idServico);

                Mensagem item1 = new Mensagem(idMensagem,mensagem,nome,idUsuario,idServico);
                itens.add(item1);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapterListView = new AdapterListView(this, itens);
        listView.setAdapter(adapterListView);
        listView.setCacheColorHint(Color.TRANSPARENT);
    }

    public void deletaItem(View v) {
        adapterListView.removeItem((Integer) v.getTag());
        String idMensagem= adapterListView.idSelecionado;
        deletarMensagem(idMensagem);
    }

    private void deletarMensagem(final String idMens){
        class DeletarMensagem extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PerfilAutonomo.this, "Deletando Mensagem", "Aguarde...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(PerfilAutonomo.this,s,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PerfilAutonomo.this, PerfilAutonomo.class);

                intent.putExtra(Config.NOMESERVICO,nomeServico);
                intent.putExtra(Config.NOME_USUARIO,nomeAutonomo);
                intent.putExtra(Config.RECOMENDACAO,recomendacao);
                intent.putExtra(Config.SERVICO_ID,id);
                intent.putExtra(Config.DIA,dia);
                intent.putExtra(Config.TURNO,turno);

                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                AcessoWeb rh = new AcessoWeb();
                String s = rh.sendGetRequestParam(Config.URL_DELETAR_MENSAGEM, idMens);
                //Log.d("=====> Id Mensagem: ",idMens);
                //Log.d("==> Resposta Servidor: ",s);
                return s;
            }
        }

        DeletarMensagem de = new DeletarMensagem();
        de.execute();
    }
}
