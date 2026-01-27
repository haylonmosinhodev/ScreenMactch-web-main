package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaChatGPT;
import br.com.alura.screenmatch.service.traducao.ConsultaMyMemory;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;


@Entity
@Table(name = "series")
public class Serie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tilulo;
    private Integer totalTeporadas;
    private Double avaliacao;

    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;


    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie(){

    }

    public Serie(dadosSeries dadosSeries) {
        this.tilulo = dadosSeries.tilulo();
        this.totalTeporadas = dadosSeries.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSeries.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSeries.genero().split(",")[0].trim());
        this.atores = dadosSeries.atores();
        this.poster = dadosSeries.poster();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSeries.sinopse()).trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
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
                ", sinopse = '" + sinopse + '\'' +
                ", episodios = '" + episodios + '\'';

    }
}
