package br.desenvolvedor.michelatz.projetosam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lista);
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
