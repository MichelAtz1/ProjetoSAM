package br.desenvolvedor.michelatz.projetosam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;

public class Login extends AppCompatActivity {

    private Toolbar mToobar;
    private EditText edtEmail;
    private EditText edtSenha;

    public static final String MyPREFERENCES = "MinhasPreferencias" ;
    public static final String idUsuario = "idKey";
    public static final String nomeUsuarioPref = "nomeKey";

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmailUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenhaUsuario);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Login");
        setSupportActionBar(mToobar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void entrar(View v){

        final String email = edtEmail.getText().toString();
        final String senha = edtSenha.getText().toString();

        if(email.equals("") || senha.equals("")){
            Toast.makeText(getApplicationContext(), "Por favor, informe e-mail e senha para login!", Toast.LENGTH_SHORT).show();
        }else{
            verificaLogin(email, senha);
        }
    }

    private void verificaLogin(final String email, final String senha){

        class verificaLogin extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this,"Realizando Login...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();

                if(retornoServidor.trim().equals("0")){

                    Toast.makeText(Login.this,"E-mail ou Senha inválidos",Toast.LENGTH_LONG).show();

                }else{

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(retornoServidor);
                        JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        for(int i = 0; i<result.length(); i++) {
                            JSONObject jo = result.getJSONObject(i);
                            String id = jo.getString(Config.TAG_ID);
                            String nomeUsuario = jo.getString(Config.TAG_NOME);

                            editor.putString(idUsuario, id);
                            editor.putString(nomeUsuarioPref, nomeUsuario);

                            editor.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Login.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_USUARIO_EMAIL,email);
                hashMap.put(Config.KEY_USUARIO_SENHA,senha);

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_VERIFICA_LOGIN,hashMap);
                return retornoServidor;
            }
        }
        verificaLogin verificaLog = new verificaLogin();
        verificaLog.execute();
    }

    public void botaoCadastrarUsuario(View v){
        Intent it;
        it = new Intent(this, CadastrarUsuario.class);
        startActivity(it);
        finish();
    }
}
