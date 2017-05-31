package br.desenvolvedor.michelatz.projetosam;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;

public class CadastrarServico extends AppCompatActivity {

    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    private RadioGroup radioTurno;
    private RadioGroup radioDia;
    private RadioButton btQualquer, btManha, btTarde, btNoite;
    private RadioButton btQualquerDia, btSemana, btFinalSemana;

    ArrayList<String> servicos = new ArrayList<String>();

    static Spinner comboServicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        radioTurno = (RadioGroup) findViewById(R.id.RadioGrupTurno);
        radioDia = (RadioGroup) findViewById(R.id.RadioGrupDia);

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
        mToobar.setSubtitle("  Cadastrar Serviço");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(CadastrarServico.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(CadastrarServico.this, PrincipalActivity.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(CadastrarServico.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(CadastrarServico.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(CadastrarServico.this, PrincipalActivity.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);

        comboServicos = (Spinner)findViewById(R.id.spinnerServico);
        servicos.add("Selecione um serviço");
        buscaServicos();

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,servicos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboServicos.setAdapter(adapter);

    }

    private void buscaServicos(){

        class buscaServicos extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CadastrarServico.this,"Buscando Serviços...","Aguarde...",false,false);
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
                startActivity(new Intent(this,PrincipalActivity.class));
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

                editor.commit();
                editor.clear();

                startActivity(new Intent(this,Login.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTipoServico(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Adicionar um Tipo de Serviço");
        alertDialogBuilder.setMessage("Insira um novo serviço:");

        final EditText input = new EditText(this);
        alertDialogBuilder.setView(input);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(getApplicationContext(), input.getText().toString().trim(),
                                //Toast.LENGTH_SHORT).show();
                        adicionarTipoServico(input.getText().toString().trim());
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // finaliza alertDialog
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private void adicionarTipoServico(final String novoServico) {

        class adicionarTipoServico extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CadastrarServico.this,"Salvando Dados...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();
                Toast.makeText(CadastrarServico.this, retornoServidor, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CadastrarServico.this, CadastrarServico.class);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_TIPO_SERVICO_NOME, novoServico);

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_INSERIR_TIPO_SERVICO,hashMap);
                return retornoServidor;
            }
        }
        adicionarTipoServico addTpoServico = new adicionarTipoServico();
        addTpoServico.execute();
    }

    public void salvar(View v){
        String serv = " ";
        String dia = " ";
        String turno = " ";

        int selectedIdTurno = radioTurno.getCheckedRadioButtonId();
        int selectedIdDia = radioDia.getCheckedRadioButtonId();

        String ServicoSelecionado = comboServicos.getSelectedItem().toString();
        if(ServicoSelecionado.equals("Selecione um serviço")){
            Toast.makeText(CadastrarServico.this,"Por Favor! Selecione um serviço!", Toast.LENGTH_SHORT).show();
        }else {

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

            adicionarServico(ServicoSelecionado, turno, dia);
        }
    }

    private void adicionarServico(final String servicoSelecionado, final String turno, final String dia) {

        class adicionarServico extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CadastrarServico.this,"Salvando Dados do Servico...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();

                    Toast.makeText(CadastrarServico.this, retornoServidor, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CadastrarServico.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idAutonomo = sharedpreferences.getString("idKey", null);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_SERVICO_NOME, servicoSelecionado);
                hashMap.put(Config.KEY_SERVICO_TURNO,turno);
                hashMap.put(Config.KEY_SERVICO_DIA,dia);
                hashMap.put(Config.KEY_SERVICO_IDUSUARIO,idAutonomo);
                /*
                Log.d("======> nomeserrvico: ",servicoSelecionado);
                Log.d("======> turno: ",turno);
                Log.d("======> dia: ",dia);
                Log.d("======> idUsuario: ",idAutonomo);
                */
                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_INSERIR_SERVICO,hashMap);
                return retornoServidor;
            }
        }
        adicionarServico addServico = new adicionarServico();
        addServico.execute();
    }
}
