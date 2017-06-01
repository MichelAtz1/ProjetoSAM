package br.desenvolvedor.michelatz.projetosam;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;
import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.AcessoWeb;

public class CadastrarUsuario extends AppCompatActivity {

    ObterGPS gps;
    String longitude = "inicial";
    String latitude = "inicial";
    private Address enderecoCompleto;

    private Toolbar mToobar;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtCidade;
    private EditText edtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtEstado = (EditText) findViewById(R.id.edtEstado);

        mToobar = (Toolbar) findViewById(R.id.tb_main);
        mToobar.setTitle("  SAM");
        mToobar.setLogo(R.drawable.iconsam);
        mToobar.setSubtitle("  Cadastrar Usuário");
        setSupportActionBar(mToobar);


        try {
            getLocalization();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getLocalization() throws IOException {
        gps = new ObterGPS(CadastrarUsuario.this);


        if (GetLocalization(CadastrarUsuario.this)) {
            if (gps.canGetLocation()) {

                longitude = String.valueOf(gps.getLongitude());
                latitude = String.valueOf(gps.getLatitude());

                enderecoCompleto = buscaEndereco(gps.getLatitude(),gps.getLongitude());

                edtCidade.setText(enderecoCompleto.getLocality());
                edtEstado.setText(enderecoCompleto.getAdminArea());

            } else {
                AlertDialog.Builder erroLocation = new AlertDialog.Builder(this);
                erroLocation.setTitle("Localização não encontrada");
                erroLocation.setMessage("Sua Localização não foi encontrada!! Tente novamente!");
                erroLocation.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                startActivity(new Intent(CadastrarUsuario.this, Login.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = erroLocation.create();
                alertDialog.show();

                gps.showSettingsAlert();
            }

        }
    }

    public boolean GetLocalization(Context context) {
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                res = false;
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);
            }
        }
        return res;
    }

    public Address buscaEndereco(double latitude, double longitude) throws IOException{

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext());
        addresses = geocoder.getFromLocation(latitude,longitude,1);
        if (addresses.size()>0){
            address = addresses.get(0);
        }
        return  address;
    }

    public void salvar(View v){
        final String nome = edtNome.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String senha = edtSenha.getText().toString().trim();
        final String cidade = edtCidade.getText().toString().trim();
        final String estado = edtEstado.getText().toString().trim();

        if((nome.equals("") || email.equals("") || senha.equals("") || cidade.equals(""))){
            Toast.makeText(CadastrarUsuario.this, "Todos os campos são Obrigatórios", Toast.LENGTH_SHORT).show();
        }else{
            boolean emailValido = validaEmail(email);
            if(emailValido == true) {
                adicionarUsuario(nome, email, senha, cidade, estado);
            }else{
                Toast.makeText(CadastrarUsuario.this, "Por Favor! Insira um E-mail valido!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean validaEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void adicionarUsuario(final String nome, final String email, final String senha, final String cidade, final String estado){

        class adicionarUsuario extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CadastrarUsuario.this,"Salvando Dados do Usuário...","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String retornoServidor) {
                super.onPostExecute(retornoServidor);
                loading.dismiss();

                if(retornoServidor.trim().equals("1")){

                    Toast.makeText(CadastrarUsuario.this,"E-mail já cadastrado!",Toast.LENGTH_LONG).show();

                }else {

                    Toast.makeText(CadastrarUsuario.this, retornoServidor, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CadastrarUsuario.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_USUARIO_NOME,nome);
                hashMap.put(Config.KEY_USUARIO_EMAIL,email);
                hashMap.put(Config.KEY_USUARIO_SENHA,senha);
                hashMap.put(Config.KEY_USUARIO_CIDADE,cidade);
                hashMap.put(Config.KEY_USUARIO_ESTADO,estado);

                AcessoWeb acessoWeb = new AcessoWeb();
                String retornoServidor = acessoWeb.sendPostRequest(Config.URL_INSERIR_USUARIO,hashMap);
                return retornoServidor;
            }
        }
        adicionarUsuario addUsuario = new adicionarUsuario();
        addUsuario.execute();
    }

    @Override
    public void onBackPressed(){
        Intent it;
        it = new Intent(this, Login.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }
}
