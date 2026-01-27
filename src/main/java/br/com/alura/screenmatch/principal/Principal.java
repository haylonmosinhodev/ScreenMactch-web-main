package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.model.dadosSeries;
import br.com.alura.screenmatch.model.dadosTemporada;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.consumoApi;
import br.com.alura.screenmatch.service.converterDados;

import java.util.*;
import java.util.stream.Collectors;


public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=e3963d88";

    Scanner sc = new Scanner(System.in);
    private consumoApi consumoApi = new consumoApi();
    private converterDados conversor = new converterDados();

    private List<dadosSeries> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {



        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Lista Séries Buscadas
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção Invalida");
            }
        }
    }




    private void buscarSerieWeb() {
        dadosSeries dados = getDadosSerie();
        //dadosSeries.add(dados);
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
        System.out.println();
        System.out.println();
    }

    private dadosSeries getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);
        dadosSeries dados = conversor.obterDados(json, dadosSeries.class);
        return (dados);
    }

    private void buscarEpisodioPorSerie() {

        listarSeriesBuscadas();
        System.out.println("Escolha uma sério pelo nome: ");

        var nomeSerie = sc.nextLine();
        Optional<Serie> serie = series.stream().filter(s -> s.getTilulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<dadosTemporada> listaTemporadas = new ArrayList<>();

            for (int i = 1; i < serieEncontrada.getTotalTeporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTilulo().replace(" ", "+") + "&season=" + i + APIKEY);
                dadosTemporada dadosTemporada = conversor.obterDados(json, dadosTemporada.class);
                listaTemporadas.add(dadosTemporada);
            }
            listaTemporadas.forEach(System.out::println);

            List<Episodio> episodios = listaTemporadas.stream().flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        } else{
            System.out.println("Série nao encontrada!");
        }


        }




    private void listarSeriesBuscadas() {

        series = repositorio.findAll(); // pegando do banco de dados

        series.stream()
                        .sorted(Comparator.comparing(Serie::getGenero))
                                .forEach(System.out::println);
        System.out.println();
        System.out.println();
    }



}
