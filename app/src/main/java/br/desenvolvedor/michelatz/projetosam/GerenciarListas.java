package br.desenvolvedor.michelatz.projetosam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import br.desenvolvedor.michelatz.projetosam.ConexaoWEB.Config;

public class GerenciarListas extends AppCompatActivity {

    private String id;
    private String proximaTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.SERVICO_ID);
        proximaTela = intent.getStringExtra(Config.TIPO_TABELA);
        /*
        Log.d("=========> id: ",id);
        Log.d("=========> id: ",proximaTela);
        */
        if((proximaTela.trim().equals("1"))) {
            intent = new Intent(this,GerenciarServico.class);
            intent.putExtra(Config.SERVICO_ID,id);
            startActivity(intent);
            finish();

        }else{


        }
    }
}
