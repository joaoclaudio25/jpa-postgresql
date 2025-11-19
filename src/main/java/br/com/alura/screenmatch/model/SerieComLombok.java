package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaChatGPT;
import jakarta.persistence.*; //JPA → para mapear a entidade no banco de dados
import lombok.*; //Lombok → para remover getters, setters, construtores etc.
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
@Getter //cria automaticamente getters para todos os campos
@Setter //cria automaticamente setters
@NoArgsConstructor //cria o construtor vazio exigido pelo Hibernate
@AllArgsConstructor //cria um construtor com todos os atributos
@ToString //cria automaticamente o metodo toString()
public class SerieComLombok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String atores;
    private String poster;
    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL)
    @ToString.Exclude  // Evita loop infinito (recursão) ao imprimir objetos bidirecionais:
    private List<Episodio> episodios = new ArrayList<>();


    // Construtor manual que transforma DadosSerie (da API OMDb) em uma entidade Serie.
    public SerieComLombok(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        /*
        A API OMDb retorna gêneros assim: "Drama, Crime, Thriller"
        Aqui:
            - split(",") → divide por vírgula
            - [0] → pega apenas o primeiro gênero
            - trim() → remove espaços
            - Categoria.fromString() → converte para enum
         */
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse = ConsultaChatGPT.obterTraducao(dadosSerie.sinopse()).trim();
    }
}
