package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
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

    private Optional<Serie> serieBusca;

    public void exibeMenu() {


        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Lista Séries Buscadas
                    4 - Buscar Série por título
                    5 - Buscar Série por Ator
                    6 - Buscar Top 5 Séries
                    7 - Buscar Séries por Categoria
                    8 - Buscar Séries Por quantidade de temporadas e avaliação mínima
                    9 - Buscar Episódios por trecho
                    10 - Buscar Top 5 episódios 
                    11 - Buscar episódios apartir de uma data
                    
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
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorQuatTempoAvaliMinima();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodioPorData();
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

        if (serie.isPresent()) {
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

        } else {
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

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = sc.nextLine();
        serieBusca = repositorio.findBytiluloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());

        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite nome do ator para busca?: ");
        var nomeAtor = sc.nextLine();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTilulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriestop = repositorio.findTop5SeriesByOrderByAvaliacaoDesc();
        seriestop.forEach(System.out::println);
    }

    private void buscarSeriesPorCategoria() {

        System.out.println("Buscar Série por categoria, digite o nome da série: ");
        var nomeGenero = sc.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries por categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }


    private void buscarSeriesPorQuatTempoAvaliMinima() {
        System.out.println("Digite a quantidade máxima de temporadas: ");
        var quatMaximaTeporadas = sc.nextInt();
        sc.nextLine();
        System.out.println("Digite a avaliação mínima: ");
        var avaliacaoMinina = sc.nextDouble();
        List<Serie> seriesPorTemporadaAva = repositorio.findByTotalTeporadasLessThanEqualAndAvaliacaoGreaterThanEqual(
                quatMaximaTeporadas, avaliacaoMinina);
        sc.nextLine();

        System.out.println("Séries encontradas");
        seriesPorTemporadaAva.forEach(System.out::println);
    }


    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = sc.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTilulo(), e.getTemporada(),
                        e.getNumeroEp(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTilulo(), e.getTemporada(),
                            e.getNumeroEp(), e.getTitulo(), e.getAvaliacao() ));
        }
    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = sc.nextInt();
            sc.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }
}



