package br.desenvolvedor.michelatz.projetosam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;

public class GerenciarServico extends AppCompatActivity {

    private String id;
    private String nomeServico;
    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    private RadioGroup radioTurno;
    private RadioGroup radioDia;
    private RadioButton btQualquer, btManha, btTarde, btNoite;
    private RadioButton btQualquerDia, btSemana, btFinalSemana;

    private EditText edtServico;

    //ArrayList<String> servicos = new ArrayList<String>();

    //static Spinner comboServicos;
   // ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_servico);

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.SERVICO_ID);

        radioTurno = (RadioGroup) findViewById(R.id.RadioGrupTurno);
        radioDia = (RadioGroup) findViewById(R.id.RadioGrupDia);

        edtServico = (EditText) findViewById(R.id.edtServico);

        btQualquer = (RadioButton) findViewById(R.id.qualquer);
        btManha = (RadioButton) findViewById(R.id.manha);
        btTarde = (RadioButton) findViewById(R.id.tarde);
        btNoite = (RadioButton) findViewById(R.id.noite);

        btQualquerDia = (RadioButton) findViewById(R.id.qualquerDia);
        btSemana = (RadioButton) findViewById(R.id.diaSemana);
        btFinalSemana = (RadioButton) findViewById(R.id.finalSemana);

        mToobar  = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Gerenciar Serviço");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(GerenciarServico.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(GerenciarServico.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(GerenciarServico.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(GerenciarServico.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(GerenciarServico.this, ChatLista.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);

        //comboServicos = (Spinner)findViewById(R.id.spinnerServico2);
        //servicos.add("Selecione um serviço");
        //buscaListaServicos();

        //adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,servicos);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //comboServicos.setAdapter(adapter);

        buscarDadosServico(id);
    }

    private void buscarDadosServico(final String id){
        class buscarDados extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarServico.this,"Buscando Dados...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                mostraDados(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendGetRequestParam(Config.URL_BUSCA_DADOS_SERVICO,id);
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
            nomeServico = c.getString(Config.TAG_NOME_SERVICO);
            String turno = c.getString(Config.TAG_TURNO);
            String dia = c.getString(Config.TAG_DIA);

            //comboServicos.setSelection(adapter.getPosition(nomeServico));

            edtServico.setText(nomeServico);

            if(turno.equals("0")){
                radioTurno.check(R.id.qualquer);
            } else if(turno.equals("1")){
                radioTurno.check(R.id.manha);
            } else if(turno.equals("2")){
                radioTurno.check(R.id.tarde);
            } else if(turno.equals("3")){
                radioTurno.check(R.id.noite);
            }

            if(dia.equals("0")){
                radioDia.check(R.id.qualquerDia);
            } else if(turno.equals("1")){
                radioDia.check(R.id.diaSemana);
            } else if(turno.equals("2")){
                radioDia.check(R.id.finalSemana);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*
    private void buscaListaServicos(){

        class buscaServicos extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarServico.this,"Buscando Serviços...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(retornoServidor);
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String id = jo.getString(Config.TAG_ID);
                        String nomeServico = jo.getString(Config.TAG_NOME_SERVICO);

                        servicos.add(nomeServico);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendGetRequest(Config.URL_BUSCA_SERVICOS);
                return retornoServidor;
            }
        }
        buscaServicos buscServ = new buscaServicos();
        buscServ.execute();
    }
*/
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
        String serv = " ";
        String dia = " ";
        String turno = " ";

        int selectedIdTurno = radioTurno.getCheckedRadioButtonId();
        int selectedIdDia = radioDia.getCheckedRadioButtonId();

        //String ServicoSelecionado = comboServicos.getSelectedItem().toString();
        //if(ServicoSelecionado.equals("Selecione um serviço")){
            //Toast.makeText(GerenciarServico.this,"Por Favor! Selecione um serviço!", Toast.LENGTH_SHORT).show();
        //}else {

            if (selectedIdTurno == btQualquer.getId()) {
                turno = "0";
            } else if (selectedIdTurno == btManha.getId()) {
                turno = "1";
            } else if (selectedIdTurno == btTarde.getId()) {
                turno = "2";
            } else if (selectedIdTurno == btNoite.getId()) {
                turno = "3";
            }

            if (selectedIdDia == btQualquerDia.getId()) {
                dia = "0";
            } else if (selectedIdDia == btSemana.getId()) {
                dia = "1";
            } else if (selectedIdDia == btFinalSemana.getId()) {
                dia = "2";
            }

            editarServico(nomeServico, turno, dia);
        //}
    }

    private void editarServico(final String servicoSelecionado, final String turno, final String dia) {

        class editarServico extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarServico.this,"Edição sendo salva...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();
                Toast.makeText(GerenciarServico.this, retornoServidor, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GerenciarServico.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_SERVICO_NOME, servicoSelecionado);
                hashMap.put(Config.KEY_SERVICO_TURNO,turno);
                hashMap.put(Config.KEY_SERVICO_DIA,dia);
                hashMap.put(Config.KEY_SERVICO_IDSERVICO,id);

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_EDITAR_SERVICO,hashMap);
                return retornoServidor;
            }
        }
        editarServico updateServico = new editarServico();
        updateServico.execute();
    }

    public void excluirServ(View v){
        confirmarDelete();
    }

    private void confirmarDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja deletar seu serviço?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletarServico();

                        startActivity(new Intent(GerenciarServico.this,ListaServico.class));
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

    private void deletarServico(){
        class deletarServico extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GerenciarServico.this, "Deletando Serviço", "Aguarde...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(GerenciarServico.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                AcessoWeb rh = new AcessoWeb();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_SERVICO, id);
                return s;
            }
        }

        deletarServico deletaservico = new deletarServico();
        deletaservico.execute();
    }

    @Override
    public void onBackPressed(){
        Intent it;
        it = new Intent(this, ListaServico.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }
}
