package br.com.alura.agenda01.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda01.FormularioActivity;
import br.com.alura.agenda01.modelo.Aluno;

/**
 * Created by DELL on 20/09/2017.
 */

public class AlunoDAO extends SQLiteOpenHelper {
    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL," +
                "endereco_foto_perfil TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);

    }

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("endereco_foto_perfil", aluno.getFoto());

        db.insert("Alunos", null, dados);
    }

    public ArrayList<Aluno> buscaAlunos() {
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        ArrayList<Aluno> alunos = new ArrayList<Aluno>();
        if(c != null && c.getCount() > 0) {                                          //check if cursor has elements!
            while (c.moveToNext()) {
                Aluno aluno = new Aluno();
                aluno.setId(c.getLong(c.getColumnIndex("id")));
                aluno.setNome(c.getString(c.getColumnIndex("nome")));
                aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
                aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
                aluno.setSite(c.getString(c.getColumnIndex("site")));
                aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
                aluno.setFoto(c.getString((c.getColumnIndex("endereco_foto_perfil"))));

                alunos.add(aluno);
            }
        }
        c.close();

        return alunos;
    }

    public void alterar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("id", aluno.getId());
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("endereco_foto_perfil", aluno.getFoto());
        //dados.put("imagem_perfil", getBytes(aluno.getImagemPerfil()));

        db.update("Alunos",dados,"id = " + aluno.getId(),null);
    }

    public void deleta(Aluno aluno) {
        String sql = "DELETE FROM Alunos WHERE nome LIKE ?;";
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Alunos", "nome LIKE '" + aluno.getNome() + "%'", null);
    }
}
