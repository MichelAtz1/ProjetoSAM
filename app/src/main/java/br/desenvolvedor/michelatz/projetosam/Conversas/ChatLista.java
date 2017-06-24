package br.desenvolvedor.michelatz.projetosam.Conversas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.desenvolvedor.michelatz.projetosam.InicioUsuario;
import br.desenvolvedor.michelatz.projetosam.R;

import static java.security.AccessController.getContext;

public class ChatLista extends AppCompatActivity {

    private RecyclerView rv;
    private List<ConversaAtributos> atributosList;
    private ConversaAdapter adapter;

    //private VolleyRP volley;
    //private RequestQ mRequest;

    private static final String URL_GET_ALL_USUARIOS = "http://kevinandroidkap.pe.hu/ArchivosPHP/Usuarios_GETALL.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lista);

        atributosList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.conversaRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new ConversaAdapter(atributosList,this);
        rv.setAdapter(adapter);
        //SolicitudJSON();
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
