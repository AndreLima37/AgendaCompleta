package br.com.alura.agenda01;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda01.dao.AlunoDAO;
import br.com.alura.agenda01.modelo.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;
    private Button btnNovoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentVaiProFormulario.putExtra("aluno", aluno);
                startActivity(intentVaiProFormulario);
            }
        });

        btnNovoAluno = (Button) findViewById(R.id.novo_aluno);
        btnNovoAluno.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Executar caso de uso  aluno
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        //registerForContextMenu(listaAlunos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlunoDAO alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = new ArrayList<Aluno>();
        alunoDAO = new AlunoDAO(this);
        alunos = alunoDAO.buscaAlunos();
        alunoDAO.close();

        for (Aluno a : alunos) {
            AlertDialog.Builder janela = new AlertDialog.Builder(this);
            janela.setTitle("Informaçoes do aluno");
            janela.setMessage("Os campos do aluno são:\n Nome: " + a.getNome()
                    + "\n Endereço: " + a.getEndereco()
                    + "\n Telefone: " + a.getTelefone()
                    + "\n E-mail: " + a.getSite()
                    + "\n Nota: " + a.getNota()
                    + "\n Endereco foto perfil: " + a.getFoto());
            janela.setNeutralButton("OK", null);
            janela.create();
            janela.show();
        }

        carregalista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);
                Toast.makeText(ListaAlunosActivity.this, "Deletar o aluno " + aluno.getNome(), Toast.LENGTH_SHORT).show();

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();

                carregalista();
                return false;
            }
        });
    }

    private void carregalista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        //ListView listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        //AdapterView<Adapter> listaAlunos = null;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem itemSite = menu.add("Visitar site");
        MenuItem itemLigar = menu.add("Ligar");
        MenuItem irParaSite = menu.add("Ir Para Site");
        MenuItem sms = menu.add("Sms");
        MenuItem vaiProMapa = menu.add("Vai Para Mapa");
        MenuItem deletar = menu.add("Deletar");
        MenuItem onMenuItemClickListener = deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()}
        itemligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
        @Override
        public boolean onMenuItemClick (MenuItem item){
        if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 123);
        } else {
            Intent intentLigar = new Intent(Intent.ACTION_CALL);
            intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
            startActivity(intentLigar);
//                itemLigar.setIntent(intentLigar);
        }
        return false;
    }    }
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();

                //carregaLista();
              //return false;

            }
    @Override
    public boolean onMenuItemClick (MenuItem Object) {
    if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE))
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                new String[]{Manifest.permission.CALL_PHONE}, 123);
    }

        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        //Intent intentSMS = null;
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Visualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        IntentMapa.setData(Uri.parse("geo:0,0?z=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        MenuItem itemSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

        String site = aluno.getSite();
        if (!site.startsWith("http://")) {
            site = "http://" + site;
        }

        intentSite.setData(Uri.parse(aluno.getSite()));
        Intent itentSite = null;
        final MenuItem menuItem = itemSite.setIntent(itentSite);
    }*/



    /*@Override
    public void onRequestPermissionResult(int requestCode, @NonNull String) ;
            )
     }; */
}