package br.desenvolvedor.michelatz.projetosam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PrincipalActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_simples);

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String idUsuario = sharedpreferences.getString("idKey", null);

        if(idUsuario == null){
            Intent it = new Intent(this, Login.class);
            startActivity(it);
            finish();
        }else{
            Intent it = new Intent(this, InicioUsuario.class);
            startActivity(it);
            finish();
        }
    }
}
