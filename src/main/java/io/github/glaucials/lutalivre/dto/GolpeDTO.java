package io.github.glaucials.lutalivre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GolpeDTO {

    @Min(1)
    @NotNull
    private Integer idLutadorBate;

    @Min(1)
    @NotNull
    private Integer idLutadorApanha;
}
