package io.github.glaucials.lutalivre.entity;

import io.github.glaucials.lutalivre.dto.LutadorDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_lutador")
public class Lutador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 3, max = 12)
    private String nome;

    @Min(0)
    private double forcaGolpe;

    private double vida = 100.0;
    private int concentracoesRealizadas = 0;
    private boolean vivo = true;

    public Lutador(LutadorDTO lutador) {
        nome = lutador.getNome();
        forcaGolpe = lutador.getForcaGolpe();
    }
}
