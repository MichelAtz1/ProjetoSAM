package br.desenvolvedor.michelatz.projetosam.Mensagens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.desenvolvedor.michelatz.projetosam.BuscarServico;
import br.desenvolvedor.michelatz.projetosam.CadastrarServico;
import br.desenvolvedor.michelatz.projetosam.Conversas.ChatLista;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.GerenciarUsuario;
import br.desenvolvedor.michelatz.projetosam.InicioUsuario;
import br.desenvolvedor.michelatz.projetosam.ListaServico;
import br.desenvolvedor.michelatz.projetosam.Login;
import br.desenvolvedor.michelatz.projetosam.Modelo.MensagemDeTexto;
import br.desenvolvedor.michelatz.projetosam.PrincipalActivity;
import br.desenvolvedor.michelatz.projetosam.R;

public class ChatConversa extends AppCompatActivity {

    public static String MENSAGEM = "MENSAGEM";
    private String id, idReceptor;

    private String JSON_STRING;

    private Toolbar mToobar;
    private Toolbar mToobarBotton;
    private EditText edtMensagemDigitada;
    private Button btEnviarMensagem;
    private int linhas_texto = 1;

    private RecyclerView rv;
    private List<MensagemDeTexto> mensagensText;
    private MensagemAdapter adapter;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversa);

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.SERVICO_ID);
        idReceptor = intent.getStringExtra(Config.ID_AUTONOMO);

        //Log.d("------> to no chat",id+" - "+idReceptor);

        mensagensText = new ArrayList<>();
        edtMensagemDigitada = (EditText) findViewById(R.id.edtMensagem);
        btEnviarMensagem = (Button) findViewById(R.id.btSalvaMensagem);

        rv = (RecyclerView) findViewById(R.id.rvMensagens);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);

        buscaMensagensChat();

        adapter = new MensagemAdapter(mensagensText, this);
        rv.setAdapter(adapter);



        edtMensagemDigitada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edtMensagemDigitada.getLayout().getLineCount() != linhas_texto){
                    setScrollbarChat();
                    linhas_texto = edtMensagemDigitada.getLayout().getLineCount();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MENSAGEM = edtMensagemDigitada.getText().toString().trim();

                MandarMensagem();
                edtMensagemDigitada.setText(" ");
            }
        });

        mToobar  = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Conversa");
        setSupportActionBar(mToobar);

        mToobarBotton = (Toolbar) findViewById(R.id.inc_tb_botton_usuario);
        mToobarBotton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()){
                    case R.id.acao_usuario:
                        it = new Intent(ChatConversa.this, GerenciarUsuario.class);
                        break;
                    case R.id.acao_buscar:
                        it = new Intent(ChatConversa.this, BuscarServico.class);
                        break;
                    case R.id.acao_adicionar:
                        it = new Intent(ChatConversa.this, CadastrarServico.class);
                        break;
                    case R.id.acao_listar:
                        it = new Intent(ChatConversa.this, ListaServico.class);
                        break;
                    case R.id.acao_chat:
                        it = new Intent(ChatConversa.this, ChatLista.class);
                        break;
                }
                startActivity(it);
                finish();
                return true;
            }
        });
        mToobarBotton.inflateMenu(R.menu.menu_botton_usuario);
        setScrollbarChat();
        temporizador();
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

    @Override
    public void onBackPressed(){
        Intent it;
        it = new Intent(this, InicioUsuario.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }

    public void criarMensagem(String mensagem,String hora,int tipoDeMensagem){
        MensagemDeTexto mensagemDeTextoAuxiliar = new MensagemDeTexto();
        mensagemDeTextoAuxiliar.setId("0");
        mensagemDeTextoAuxiliar.setMensagem(mensagem);
        mensagemDeTextoAuxiliar.setTipoMmensagem(tipoDeMensagem);
        mensagemDeTextoAuxiliar.setHoraDaMensagem(hora);
        mensagensText.add(mensagemDeTextoAuxiliar);
        adapter.notifyDataSetChanged();
        edtMensagemDigitada.setText(" ");
        setScrollbarChat();
    }

    private void MandarMensagem(){
        class MandarMensagem extends AsyncTask<Void,Void,String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                //Log.d("------>",retornoServidor);
                setScrollbarChat();
                buscaMensagensChat();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                final String idUser = sharedpreferences.getString("idKey", null);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMISSOR, idUser);
                hashMap.put(Config.KEY_RECEPTOR, idReceptor);
                hashMap.put(Config.KEY_MENSAGEM_CHAT, MENSAGEM);
                hashMap.put(Config.KEY_ID_SERVICO, id);
                //Log.d("------> to no chat",id+" - "+idReceptor);
                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_INSERIR_MENSAGEM_CHAT,hashMap);
                return retornoServidor;
            }
        }
        MandarMensagem mm = new MandarMensagem();
        mm.execute();
    }

    //metodo para aparecer a ultima mensagem do adapter
    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }

    private void buscaMensagensChat(){
        class buscaMensagensChat extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mostraChat();
            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String idUser = sharedpreferences.getString("idKey", null);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_CHAT_ID_SERVICO,id);
                hashMap.put(Config.KEY_CHAT_ID_EMISSOR,idUser);
                hashMap.put(Config.KEY_CHAT_ID_RECEPTOR,idReceptor);

                AcessoWeb acessoWeb = new AcessoWeb();
                String s = acessoWeb.sendPostRequest(Config.URL_LISTAR_CHAT,hashMap);
                JSON_STRING = s;

                return s;
            }
        }
        buscaMensagensChat buscaMensChat = new buscaMensagensChat();
        buscaMensChat.execute();
    }

    private void temporizador(){
        long TEMPO = (1000 * 3); // chama o método a cada 3 segundos
        if (timer == null) {
            Log.d("====> temporizador", "3 segundos");
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                public void run() {
                    try {
                        buscaMensagensChat();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }
    }

    private void mostraChat(){

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String idUsuario = sharedpreferences.getString("idKey", null);
        int tipoMensagem;

        //Log.d("------>Json",JSON_STRING);
        mensagensText = new ArrayList<>();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){

                JSONObject jo = result.getJSONObject(i);
                String idMensagem = jo.getString(Config.KEY_ID_MENSAGEM);
                String mensagem = jo.getString(Config.KEY_MENSAGEM_CHAT);
                String idEmissor = jo.getString(Config.KEY_CHAT_ID_EMISSOR);
                String horarioConv = jo.getString(Config.KEY_HORARIO);
                if(idUsuario.equals(idEmissor)){
                    tipoMensagem = 1;
                }else{
                    tipoMensagem = 2;
                }

                MensagemDeTexto mensagemDeTextoAuxiliar = new MensagemDeTexto();
                mensagemDeTextoAuxiliar.setId(idMensagem);
                mensagemDeTextoAuxiliar.setMensagem(mensagem);
                mensagemDeTextoAuxiliar.setTipoMmensagem(tipoMensagem);
                mensagemDeTextoAuxiliar.setHoraDaMensagem(horarioConv);
                mensagensText.add(mensagemDeTextoAuxiliar);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new MensagemAdapter(mensagensText, this);
        rv.setAdapter(adapter);
    }
}
