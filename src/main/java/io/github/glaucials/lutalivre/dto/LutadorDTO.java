package io.github.glaucials.lutalivre.dto;

import io.github.glaucials.lutalivre.entity.Lutador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LutadorDTO {

    private Integer id;
    private String nome;
    private double forcaGolpe;
    private double vida;
    private int concentracoesRealizadas;
    private boolean vivo;

    public LutadorDTO(Lutador lutador) {
        id = lutador.getId();
        nome = lutador.getNome();
        forcaGolpe = lutador.getForcaGolpe();
        vida = lutador.getVida();
        concentracoesRealizadas = lutador.getConcentracoesRealizadas();
        vivo = lutador.isVivo();
    }
}
