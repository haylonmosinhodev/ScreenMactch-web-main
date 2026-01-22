package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;
import java.util.OptionalDouble;

public class Serie {
    private String tilulo;
    private Integer totalTeporadas;
    private Double avaliacao;
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;


    public Serie(dadosSeries dadosSeries) {
        this.tilulo = dadosSeries.tilulo();
        this.totalTeporadas = dadosSeries.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSeries.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSeries.genero().split(",")[0].trim());
        this.atores = dadosSeries.atores();
        this.poster = dadosSeries.poster();
        this.sinopse = dadosSeries.sinopse();
    }

    public String getTilulo() {
        return tilulo;
    }

    public void setTilulo(String tilulo) {
        this.tilulo = tilulo;
    }

    public Integer getTotalTeporadas() {
        return totalTeporadas;
    }

    public void setTotalTeporadas(Integer totalTeporadas) {
        this.totalTeporadas = totalTeporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }


    @Override
    public String toString() {
        return  "Genero = " + genero +
                ", tilulo = '" + tilulo + '\'' +
                ", totalTeporadas = " + totalTeporadas +
                ", avaliacao = " + avaliacao +

                ", atores = '" + atores + '\'' +
                ", poster = '" + poster + '\'' +
                ", sinopse = '" + sinopse + '\'';

    }
}
