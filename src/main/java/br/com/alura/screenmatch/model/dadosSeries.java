package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record dadosSeries(@JsonAlias("Title") String tilulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                          @JsonAlias("Genre") String genero,
                          @JsonAlias("Actors") String atores,
                          @JsonAlias("Poster") String poster,
                          @JsonAlias("Plot") String sinopse
                          ) {

}
