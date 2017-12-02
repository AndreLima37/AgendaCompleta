package br.com.alura.agenda01.modelo;

import java.io.Serializable;

/**
 * Created by DELL on 19/09/2017.
 */
public class Aluno implements Serializable{     //ITS IMPLEMENTS IS FATAL!!!
    private long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String site;
    private double nota;
    private String foto;


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return getId() + " - " + getNome();
    }


}


