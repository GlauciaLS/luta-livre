package io.github.glaucials.lutalivre.service;

import io.github.glaucials.lutalivre.builder.GolpeDTOBuilder;
import io.github.glaucials.lutalivre.builder.LutadorDTOBuilder;
import io.github.glaucials.lutalivre.dto.GolpeDTO;
import io.github.glaucials.lutalivre.dto.LutadorDTO;
import io.github.glaucials.lutalivre.entity.Lutador;
import io.github.glaucials.lutalivre.exception.DeadFighterException;
import io.github.glaucials.lutalivre.exception.FighterNotFoundException;
import io.github.glaucials.lutalivre.exception.OverConcentrationException;
import io.github.glaucials.lutalivre.repository.LutadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class LutadorServiceTest {

    @InjectMocks
    private LutadorService service;

    @Mock
    private LutadorRepository repository;

    @Test
    void whenListFighterIsCalledReturnAListOfFighters() {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutadorEsperado = new Lutador(lutadorDTO);
        lutadorEsperado.setId(lutadorDTO.getId());

        when(repository.findAll()).thenReturn(Collections.singletonList(lutadorEsperado));

        List<LutadorDTO> foundListLutadorDTO = service.findAll();

        assertThat(foundListLutadorDTO, is(not(empty())));
        assertThat(foundListLutadorDTO.get(0), is(equalTo(lutadorDTO)));
    }

    @Test
    void whenInsertIsCalledReturnAFighter() {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutador = new Lutador(lutadorDTO);

        when(repository.save(lutador)).thenReturn(lutador);

        LutadorDTO foundLutadorDTO = service.insert(lutadorDTO);

        assertThat(foundLutadorDTO.getNome(), is(equalTo(lutadorDTO.getNome())));
    }

    @Test
    void whenAliveFightersIsCalledReturnACount() {
        when(repository.countAliveFighters()).thenReturn(1);

        int aliveFighters = service.aliveFighters();

        assertThat(aliveFighters, is(equalTo(1)));
    }

    @Test
    void whenDeadFightersIsCalledReturnACount() {
        when(repository.countDeadFighters()).thenReturn(0);

        int deadFighters = service.deadFighters();

        assertThat(deadFighters, is(equalTo(0)));
    }

    @Test
    void whenConcentrateIsCalledReturnAFighter() throws OverConcentrationException, FighterNotFoundException {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutador = new Lutador(lutadorDTO);
        Optional<Lutador> lutadorOptional = Optional.of(lutador);

        when(repository.findById(1)).thenReturn(lutadorOptional);
        when(repository.save(lutadorOptional.get())).thenReturn(lutadorOptional.get());

        LutadorDTO foundLutadorDTO = service.concentrateFighter(lutadorDTO.getId());

        assertThat(foundLutadorDTO.getNome(), is(equalTo(lutadorDTO.getNome())));
    }

    @Test
    void whenConcentrateIsCalledReturnOverConcentrationException() throws OverConcentrationException, FighterNotFoundException {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutador = new Lutador(lutadorDTO);
        lutador.setConcentracoesRealizadas(3);
        Optional<Lutador> lutadorOptional = Optional.of(lutador);

        when(repository.findById(1)).thenReturn(lutadorOptional);

        assertThrows(OverConcentrationException.class, ()->{
            service.concentrateFighter(lutadorDTO.getId());
        });
    }

    @Test
    void whenConcentrateIsCalledReturnFighterNotFoundException() throws OverConcentrationException, FighterNotFoundException {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Optional<Lutador> lutadorOptional = Optional.empty();

        when(repository.findById(1)).thenReturn(lutadorOptional);

        assertThrows(FighterNotFoundException.class, ()->{
            service.concentrateFighter(lutadorDTO.getId());
        });
    }

    @Test
    void whenHitFighterIsCalledReturnAListOfFighters() throws DeadFighterException, FighterNotFoundException {
        GolpeDTO golpeDTO = GolpeDTOBuilder.builder().build().toGolpeDTO();
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutador = new Lutador(lutadorDTO);
        Optional<Lutador> lutadorOptional = Optional.of(lutador);

        when(repository.findById(golpeDTO.getIdLutadorApanha())).thenReturn(lutadorOptional);
        when(repository.findById(golpeDTO.getIdLutadorBate())).thenReturn(lutadorOptional);

        List<LutadorDTO> lista = service.hitFighter(golpeDTO);

        assertThat(lista.size(), is(equalTo(2)));
    }

    @Test
    void whenHitFighterIsCalledAndOneOfThemIsDeadReturnAListOfFighters() throws DeadFighterException, FighterNotFoundException {
        GolpeDTO golpeDTO = GolpeDTOBuilder.builder().build().toGolpeDTO();
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutador = new Lutador(lutadorDTO);
        lutador.setVida(0);
        Optional<Lutador> lutadorOptional = Optional.of(lutador);

        when(repository.findById(golpeDTO.getIdLutadorApanha())).thenReturn(lutadorOptional);
        when(repository.findById(golpeDTO.getIdLutadorBate())).thenReturn(lutadorOptional);
        when(repository.save(lutador)).thenReturn(lutador);

        List<LutadorDTO> lista = service.hitFighter(golpeDTO);

        assertThat(lista.size(), is(equalTo(2)));
    }

    @Test
    void whenHitFighterIsCalledReturnDeadFighterException() throws DeadFighterException, FighterNotFoundException {
        GolpeDTO golpeDTO = GolpeDTOBuilder.builder().build().toGolpeDTO();
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        Lutador lutador = new Lutador(lutadorDTO);
        lutador.setVivo(false);
        Optional<Lutador> lutadorOptional = Optional.of(lutador);

        when(repository.findById(golpeDTO.getIdLutadorApanha())).thenReturn(lutadorOptional);
        when(repository.findById(golpeDTO.getIdLutadorBate())).thenReturn(lutadorOptional);

        assertThrows(DeadFighterException.class, ()->{
            service.hitFighter(golpeDTO);
        });
    }

    @Test
    void whenHitFighterIsCalledReturnFighterNotFoundException() throws DeadFighterException, FighterNotFoundException {
        GolpeDTO golpeDTO = GolpeDTOBuilder.builder().build().toGolpeDTO();
        Optional<Lutador> lutadorOptional = Optional.empty();

        when(repository.findById(golpeDTO.getIdLutadorApanha())).thenReturn(lutadorOptional);
        when(repository.findById(golpeDTO.getIdLutadorBate())).thenReturn(lutadorOptional);

        assertThrows(FighterNotFoundException.class, ()->{
            service.hitFighter(golpeDTO);
        });
    }

}
