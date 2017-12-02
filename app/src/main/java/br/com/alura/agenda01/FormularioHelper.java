package br.com.alura.agenda01;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.alura.agenda01.modelo.Aluno;

/**
 * Created by DELL on 19/09/2017.
 */

public class FormularioHelper  {

    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoSite;
    private RatingBar campoNota;
    private ImageView campoImagem;

    public Aluno aluno;

    public FormularioHelper(FormularioActivity activity)  {
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        campoImagem = (ImageView) activity.findViewById(R.id.formulario_foto);
        this.aluno = new Aluno();
    }


    public Aluno pegaAluno()  {
        boolean alunoIncompleto = false;
        //Aluno aluno = new Aluno ();
        if (!campoNome.getText().toString().equals("")){
            aluno.setNome(campoNome.getText().toString());
        } else {
            campoNome.setError("Este campo não pode ficar vazio");
            alunoIncompleto = true;
        }
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        if (alunoIncompleto == true){
            return null;
        }



        else {
            return aluno;
        }
    }

    public void setAluno(Aluno a){
        this.aluno = aluno;
    }

    //Add image bitmap
    public void adicionaImagemPerfil(Bitmap b){
        this.campoImagem.setImageBitmap(b);
    }

    //Get image bitmap
    public Bitmap obterImagemPerfil(){
        return ((BitmapDrawable)this.campoImagem.getDrawable()).getBitmap();
    }

    public void preencheFormulario(Aluno aluno) {
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress((int) aluno.getNota());
        this.aluno = aluno;
    }
}


