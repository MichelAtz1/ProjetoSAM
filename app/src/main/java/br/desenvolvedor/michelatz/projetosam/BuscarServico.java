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
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.Conversas.ChatLista;

public class BuscarServico extends AppCompatActivity {
    private Toolbar mToobar;
    private Toolbar mToobarBotton;

    private RadioGroup radioTurno;
    private RadioGroup radioDia;
    private RadioGroup radioRecomendacao;
    private RadioButton btQualquer, btManha, btTarde, btNoite;
    private RadioButton btQualquerDia, btSemana, btFinalSemana;
    private RadioButton btSim, btNao;

    ArrayList<String> servicos = new ArrayList<String>();

    static Spinner comboServicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_servico);

        radioTurno = (RadioGroup) findViewById(R.id.RadioGrupTurno);
        radioDia = (RadioGroup) findViewById(R.id.RadioGrupDia);
        radioRecomendacao = (RadioGroup) findViewById(R.id.RadioGrupRecomendacoes);

        btQualquer = (RadioButton) findViewById(R.id.qualquer);
        btManha = (RadioButton) findViewById(R.id.manha);
        btTarde = (RadioButton) findViewById(R.id.tarde);
        btNoite = (RadioButton) findViewById(R.id.noite);

        btQualquerDia = (RadioButton) findViewById(R.id.qualquerDia);
        btSemana = (RadioButton) findViewById(R.id.diaSemana);
        btFinalSemana = (RadioButton) findViewById(R.id.finalSemana);

        btSim = (RadioButton) findViewById(R.id.sim);
        btNao = (RadioButton) findViewById(R.id.nao);

        mToobar  = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Buscar Serviço");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(BuscarServico.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(BuscarServico.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(BuscarServico.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(BuscarServico.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(BuscarServico.this, ChatLista.class);
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

    private void buscaServicos(){

        class buscaServicos extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BuscarServico.this,"Buscando Serviços...","Aguarde...",false,false);
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

    public void buscarServico(View v){
        String serv = " ";
        String dia = " ";
        String turno = " ";
        String recomendacao = " ";

        int selectedIdTurno = radioTurno.getCheckedRadioButtonId();
        int selectedIdDia = radioDia.getCheckedRadioButtonId();
        int selectedIdRecomendacao = radioRecomendacao.getCheckedRadioButtonId();

        String ServicoSelecionado = comboServicos.getSelectedItem().toString();
        if(ServicoSelecionado.equals("Selecione um serviço")){
            Toast.makeText(BuscarServico.this,"Por Favor! Selecione um serviço!", Toast.LENGTH_SHORT).show();
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

            if (selectedIdRecomendacao == btNao.getId()) {
                recomendacao = "0";
            } else if (selectedIdRecomendacao == btSim.getId()) {
                recomendacao = "1";
            }

            Intent intent = new Intent(this,ListaAutonomos.class);
            intent.putExtra(Config.NOMESERVICO,ServicoSelecionado);
            intent.putExtra(Config.DIA,dia);
            intent.putExtra(Config.TURNO,turno);
            intent.putExtra(Config.RECOMENDACAO,recomendacao);
            /*
            Log.d("=====> Dia: ",dia);
            Log.d("=====> Turno: ",turno);
            Log.d("=====> Recomendação: ",recomendacao);
            Log.d("=====> Serviço: ",ServicoSelecionado);
            */
            startActivity(intent);
            finish();
        }
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
