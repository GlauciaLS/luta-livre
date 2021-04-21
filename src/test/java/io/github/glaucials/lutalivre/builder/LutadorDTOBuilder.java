package io.github.glaucials.lutalivre.builder;

import io.github.glaucials.lutalivre.dto.LutadorDTO;
import lombok.Builder;

@Builder
public class LutadorDTOBuilder {

    @Builder.Default
    private Integer id = 1;

    @Builder.Default
    private String nome = "Zé Karate";

    @Builder.Default
    private double forcaGolpe = 10.0;

    @Builder.Default
    private double vida = 100.0;

    @Builder.Default
    private int concentracoesRealizadas = 0;

    @Builder.Default
    private boolean vivo = true;

    public LutadorDTO toLutadorDTO() {
        return new LutadorDTO(id, nome, forcaGolpe, vida, concentracoesRealizadas, vivo);
    }
}
