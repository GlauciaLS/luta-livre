package io.github.glaucials.lutalivre.builder;

import io.github.glaucials.lutalivre.dto.GolpeDTO;
import lombok.Builder;

@Builder
public class GolpeDTOBuilder {

    @Builder.Default
    private Integer idLutadorBate = 1;

    @Builder.Default
    private Integer idLutadorApanha = 1;

    public GolpeDTO toGolpeDTO() {
        return new GolpeDTO(idLutadorBate, idLutadorApanha);
    }
}
