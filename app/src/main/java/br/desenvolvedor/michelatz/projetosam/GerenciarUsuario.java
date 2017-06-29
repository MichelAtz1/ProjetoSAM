package br.desenvolvedor.michelatz.projetosam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.Conversas.ChatLista;

public class GerenciarUsuario extends AppCompatActivity {

    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    private EditText edtNome;
    private EditText edtCidade;
    private EditText edtEstado;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_usuario);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtEstado = (EditText) findViewById(R.id.edtEstado);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Gerenciar Usuário");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(GerenciarUsuario.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(GerenciarUsuario.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(GerenciarUsuario.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(GerenciarUsuario.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(GerenciarUsuario.this, ChatLista.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);
        buscarDados();

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        idUsuarioLogado = sharedpreferences.getString("idKey", null);
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

    public void editar(View v){
        final String nome = edtNome.getText().toString().trim();
        final String cidade = edtCidade.getText().toString().trim();
        final String estado = edtEstado.getText().toString().trim();

        if((nome.equals("") ||  cidade.equals(""))){
            Toast.makeText(GerenciarUsuario.this, "Todos os campos precisam star preenchidos", Toast.LENGTH_SHORT).show();
        }else{
                editarUsuario(nome, cidade, estado);
        }
    }

    private void editarUsuario(final String nome, final String cidade, final String estado){

        class editarUsuario extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarUsuario.this,"Editando Dados do Usuário...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();

                    Toast.makeText(GerenciarUsuario.this, retornoServidor, Toast.LENGTH_LONG).show();
                    SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("nomeKey", nome);

                        editor.commit();

                    Intent intent = new Intent(GerenciarUsuario.this, InicioUsuario.class);
                    startActivity(intent);
                    finish();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUsuarioLogado = sharedpreferences.getString("idKey", null);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_USUARIO_NOME,nome);
                hashMap.put(Config.KEY_USUARIO_CIDADE,cidade);
                hashMap.put(Config.KEY_USUARIO_ESTADO,estado);
                hashMap.put(Config.KEY_USUARIO_IDLOGADO,idUsuarioLogado);

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_EDITAR_USUARIO,hashMap);
                return retornoServidor;
            }
        }
        editarUsuario editarUsuario = new editarUsuario();
        editarUsuario.execute();
    }

    private void buscarDados(){
        class buscarDados extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarUsuario.this,"Buscando Dados...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                mostraDados(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUsuarioLogado = sharedpreferences.getString("idKey", null);

                //Log.d("=====> Id usuario: ",idUsuarioLogado);

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendGetRequestParam(Config.URL_BUSCA_USUARIO,idUsuarioLogado);
                return retornoServidor;
            }
        }
        buscarDados buscaDado = new buscarDados();
        buscaDado.execute();
    }

    private void mostraDados(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nome = c.getString(Config.TAG_NOME);
            String cidade = c.getString(Config.TAG_CIDADE);
            String estado = c.getString(Config.TAG_ESTADO);

            edtNome.setText(nome);
            edtCidade.setText(cidade);
            edtEstado.setText(estado);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void excluir(View v){
        confirmarDelete();
    }

    private void confirmarDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja deletar seu perfil?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletarUsuario();

                        startActivity(new Intent(GerenciarUsuario.this,Login.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deletarUsuario(){
        class deletarUsuario extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarUsuario.this, "Deletando Dados", "Aguarde...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("idKey");
                editor.remove("nomeKey");

                editor.commit();
                editor.clear();
                Toast.makeText(GerenciarUsuario.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                AcessoWeb rh = new AcessoWeb();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_USUARIO, idUsuarioLogado);
                return s;
            }
        }

        deletarUsuario deletaUser = new deletarUsuario();
        deletaUser.execute();
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
