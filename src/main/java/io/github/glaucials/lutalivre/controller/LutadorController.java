package io.github.glaucials.lutalivre.controller;

import io.github.glaucials.lutalivre.dto.GolpeDTO;
import io.github.glaucials.lutalivre.dto.LutadorDTO;
import io.github.glaucials.lutalivre.exception.DeadFighterException;
import io.github.glaucials.lutalivre.exception.FighterNotFoundException;
import io.github.glaucials.lutalivre.exception.OverConcentrationException;
import io.github.glaucials.lutalivre.service.LutadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lutadores")
public class LutadorController {

    @Autowired
    private LutadorService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LutadorDTO> obterLutadores() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LutadorDTO inserirLutador(@RequestBody @Valid LutadorDTO lutador) {
        return service.insert(lutador);
    }

    @GetMapping("/contagem-vivos")
    @ResponseStatus(HttpStatus.OK)
    public int obterLutadoresVivos() {
        return service.aliveFighters();
    }

    @GetMapping("/mortos")
    @ResponseStatus(HttpStatus.OK)
    public int obterContagemMortos() {
        return service.deadFighters();
    }

    @PostMapping("/{id}/concentrar")
    @ResponseStatus(HttpStatus.OK)
    public LutadorDTO concentrarLutador(@PathVariable Integer id) throws OverConcentrationException, FighterNotFoundException {
        return service.concentrateFighter(id);
    }

    @PostMapping("/golpe")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LutadorDTO> golpearLutador(@RequestBody @Valid GolpeDTO golpe) throws FighterNotFoundException, DeadFighterException {
        return service.hitFighter(golpe);
    }

}
