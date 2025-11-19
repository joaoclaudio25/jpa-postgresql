package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaChatGPT;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
@Entity //indica que a classe será armazenada no banco de dados.
@Table(name = "series") //define o nome da tabela (caso queira um nome diferente).
public class Serie {
    @Id //Define uma chave primária.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gera IDs automaticamente (AUTO_INCREMENT).
    private Long id; //PK no banco de dados.
    //Atributos simples que serão convertidos em colunas da tabela series.
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING) //salva o nome do enum como texto no banco
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    //@Transient
    //Se ativada, indica que um campo não deve ser salvo no banco (ignoraria persistência).

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //indica que a chave estrangeira está na classe Episodio
    //cascade = CascadeType.ALL tudo que acontecer na Série afeta os episódios: salvar, deletar etc.
    //fetch = FetchType.LAZY Só carrega os Episódios quando você chamar (Não funcionou)
    //FetchType.EAGER Carregue os dados relacionados imediatamente, no mesmo momento em que a entidade principal for carregada.
    private List<Episodio> episodios = new ArrayList<>(); //A lista começa vazia.

    public Serie() {} //Construtor vazio obrigatório para JPA.


    public Serie(DadosSerie dadosSerie){ //Construtor que converte os dados recebidos da API (DadosSerie) em uma entidade Serie.
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0); //Converte a avaliação da API (String ou número) para Double.
        //Usa OptionalDouble como fallback, retornando 0 caso falhe.
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim()); //A API OMDb retorna vários gêneros separados por vírgula.
        //[0] Aqui pega apenas o primeiro gênero and Converte a String para Enum usando Categoria.fromString().
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse = ConsultaChatGPT.obterTraducao(dadosSerie.sinopse()).trim();
        /*
        A sinopse original geralmente vem em inglês.➡️ Aqui acontece:
            - Chamada ao ChatGPT
            - ChatGPT traduz
            - .trim() remove espaços no início/fim.
         */
    }

    //Getters e Setters
    //Os métodos abaixo permitem acessar e modificar os atributos, seguindo o padrão JavaBeans.
    // O JPA (Hibernate) depende deles.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } //Define o ID da série
    public List<Episodio> getEpisodios() { return episodios; }
    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }
    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
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

    @Override //Sobrescreve o metodo toString() para exibir informações da série.
    //Muito útil para debug e exibir no console.
    public String toString() {
        return
                "genero=" + genero +
                        ", titulo='" + titulo + '\'' +
                        ", totalTemporadas=" + totalTemporadas +
                        ", avaliacao=" + avaliacao +

                        ", atores='" + atores + '\'' +
                        ", poster='" + poster + '\'' +
                        ", sinopse='" + sinopse + '\'' +
                        ", episódios='" +episodios + '\'';
    }
}
