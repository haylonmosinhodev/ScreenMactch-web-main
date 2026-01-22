package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.model.dadosSeries;
import br.com.alura.screenmatch.model.dadosTemporada;
import br.com.alura.screenmatch.service.consumoApi;
import br.com.alura.screenmatch.service.converterDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=e3963d88";

    Scanner sc = new Scanner(System.in);
    private consumoApi consumoApi = new consumoApi();
    private converterDados conversor = new converterDados();

    private List<dadosSeries> dadosSeries = new ArrayList<>();

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
        dadosSeries.add(dados);
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
        dadosSeries dadosSerie = getDadosSerie();
        List<dadosTemporada> listaTemporadas = new ArrayList<>();

        for (int i = 1; i < dadosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obterDados(ENDERECO + dadosSerie.tilulo().replace(" ", "+") + "&season=" + i + APIKEY);
            dadosTemporada dadosTemporada = conversor.obterDados(json, dadosTemporada.class);
            listaTemporadas.add(dadosTemporada);
        }

        listaTemporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        List<Serie> series = new ArrayList<>();
        series = dadosSeries.stream().map( d -> new Serie(d))
                        .collect(Collectors.toList());

        series.stream()
                        .sorted(Comparator.comparing(Serie::getGenero))
                                .forEach(System.out::println);
        System.out.println();
        System.out.println();
    }
//
//
//       listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
//
//       List<dadosEpisodios> dadosEpisodios = listaTemporadas.stream()
//               .flatMap(t -> t.episodios().stream())
//               .collect(Collectors.toList());

//        System.out.println("\n Top 5 episódios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(br.com.alura.screenmatch.model.dadosEpisodios::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);


//        List<Episodio> episodios = listaTemporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                .map(d -> new Episodio(t.numero(), d)))
//                .collect(Collectors.toList());
//
//                episodios.forEach(System.out::println);

//        System.out.println("Digite um trecho do titulo do espisodio");
//        var trechoTitulo = sc.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episodio encotrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Espisodio não encontrado");
//        }


//        System.out.println("A partir de que ano voce deseja ver os episodios? ");
//        var ano = sc.nextInt();
//        sc.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDatalancamento() != null && e.getDatalancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " - Episodio: " + e.getTitulo() +
//                                " - Data de lançamento: " + e.getDatalancamento().format(formatador)
//                ));

//        System.out.println();
//        System.out.println("Avaliações por temporada");
//        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
//        System.out.println(avaliacoesPorTemporada);
//
//
//        System.out.println();
//        System.out.println("Avaliações usando metodo DoubleSummaryStatistics");
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//        System.out.println("Média: " + est.getAverage());
//        System.out.println("Melhor episódio: " + est.getMax());
//        System.out.println("Pior episódio: " + est.getMin());
//        System.out.println("Quantidade: " + est.getCount());


}
