package br.com.alura.screenmatch.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EpisodioComLombok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    @ManyToOne
    @ToString.Exclude
    private SerieComLombok serie;

    // Construtor personalizado que mantém sua lógica original
    public EpisodioComLombok(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        //➡️ A OMDb às vezes retorna "N/A" como avaliação.
        //➡️ Isso evita exception e garante que o valor será 0.0 nesses casos.
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        //➡️ Tenta converter a data no formato yyyy-MM-dd
        //➡️ Se a API retornar "N/A" ou outro formato → salva null
        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }
    }
}

