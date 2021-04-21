package io.github.glaucials.lutalivre.controller;

import io.github.glaucials.lutalivre.builder.GolpeDTOBuilder;
import io.github.glaucials.lutalivre.builder.LutadorDTOBuilder;
import io.github.glaucials.lutalivre.dto.GolpeDTO;
import io.github.glaucials.lutalivre.dto.LutadorDTO;
import io.github.glaucials.lutalivre.service.LutadorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;

import static io.github.glaucials.lutalivre.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LutadorControllerTest {

    private static final String FIGHTERS_PATH = "/lutadores";
    private static final String ALIVE_FIGHTERS = "/contagem-vivos";
    private static final String DEAD_FIGHTERS = "/mortos";
    private static final String CONCENTRATE_FIGHTER = "/1/concentrar";
    private static final String HIT_FIGHTER = "/golpe";

    private MockMvc mockMvc;

    @InjectMocks
    private LutadorController controller;

    @Mock
    private LutadorService service;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenFindAllFightersIsCalledThenReturnFighters() throws Exception {
        List<LutadorDTO> lista = new ArrayList<>();
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        lista.add(lutadorDTO);

        when(service.findAll()).thenReturn(lista);

        mockMvc.perform(MockMvcRequestBuilders.get(FIGHTERS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void whenInsertFighterIsCalledThenAFighterIsCreated() throws Exception {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();

        when(service.insert(lutadorDTO)).thenReturn(lutadorDTO);

        mockMvc.perform(post(FIGHTERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lutadorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(lutadorDTO.getNome())))
                .andExpect(jsonPath("$.forcaGolpe", is(lutadorDTO.getForcaGolpe())));
    }

    @Test
    void whenGetAliveFightersIsCalledThenReturnCount() throws Exception {
        when(service.aliveFighters()).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get(FIGHTERS_PATH + ALIVE_FIGHTERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    void whenGetDeadFightersIsCalledThenReturnCount() throws Exception {
        when(service.deadFighters()).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get(FIGHTERS_PATH + DEAD_FIGHTERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    void whenConcentrateFighterIsCalledThenDoConcentrate() throws Exception {
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();
        lutadorDTO.setConcentracoesRealizadas(1);

        when(service.concentrateFighter(1)).thenReturn(lutadorDTO);

        mockMvc.perform(post(FIGHTERS_PATH + CONCENTRATE_FIGHTER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concentracoesRealizadas", is(1)));
    }

    @Test
    void whenHitFighterIsCalledThenDoHit() throws Exception {
        GolpeDTO golpeDTO = GolpeDTOBuilder.builder().build().toGolpeDTO();
        LutadorDTO lutadorDTO = LutadorDTOBuilder.builder().build().toLutadorDTO();

        List<LutadorDTO> lista = new ArrayList<>();
        lista.add(lutadorDTO);
        lista.add(lutadorDTO);

        when(service.hitFighter(golpeDTO)).thenReturn(lista);

        mockMvc.perform(post(FIGHTERS_PATH + HIT_FIGHTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(golpeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[1].id", is(1)));
    }
}
