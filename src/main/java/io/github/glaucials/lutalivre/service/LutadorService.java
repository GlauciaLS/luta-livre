package io.github.glaucials.lutalivre.service;

import io.github.glaucials.lutalivre.dto.GolpeDTO;
import io.github.glaucials.lutalivre.dto.LutadorDTO;
import io.github.glaucials.lutalivre.entity.Lutador;
import io.github.glaucials.lutalivre.exception.DeadFighterException;
import io.github.glaucials.lutalivre.exception.FighterNotFoundException;
import io.github.glaucials.lutalivre.exception.OverConcentrationException;
import io.github.glaucials.lutalivre.repository.LutadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LutadorService {

    @Autowired
    private LutadorRepository repository;

    @Transactional(readOnly = true)
    public List<LutadorDTO> findAll() {
        List<Lutador> lutadores = repository.findAll();
        return lutadores.stream()
                .map(LutadorDTO::new)
                .sorted(Comparator.comparing(LutadorDTO::getForcaGolpe).reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public LutadorDTO insert(LutadorDTO lutadorDTO) {
        Lutador lutador = new Lutador(lutadorDTO);
        lutador = repository.save(lutador);
        return new LutadorDTO(lutador);
    }

    @Transactional(readOnly = true)
    public int aliveFighters() {
        return repository.countAliveFighters();
    }

    @Transactional(readOnly = true)
    public int deadFighters() {
        return repository.countDeadFighters();
    }

    @Transactional
    public LutadorDTO concentrateFighter(Integer id) throws FighterNotFoundException, OverConcentrationException {
        Optional<Lutador> lutadorOptional = repository.findById(id);

        if(lutadorOptional.isPresent()) {
            Lutador lutador = lutadorOptional.get();
            int concentracoes = lutador.getConcentracoesRealizadas();

            if(concentracoes < 3) {
                concentracoes++;
                lutador.setConcentracoesRealizadas(concentracoes);
                lutador.setVida(lutador.getVida() * 1.15);
                lutador = repository.save(lutador);
                return new LutadorDTO(lutador);
            }
            else {
                throw new OverConcentrationException();
            }
        }
        else {
            throw new FighterNotFoundException();
        }
    }

    @Transactional
    public List<LutadorDTO> hitFighter(GolpeDTO golpe) throws FighterNotFoundException, DeadFighterException {
        Optional<Lutador> lutadorApanhaOptional = repository.findById(golpe.getIdLutadorApanha());
        Optional<Lutador> lutadorBateOptional = repository.findById(golpe.getIdLutadorBate());

        if (lutadorBateOptional.isPresent() && lutadorApanhaOptional.isPresent()) {
            Lutador lutadorApanha = lutadorApanhaOptional.get();
            Lutador lutadorBate = lutadorBateOptional.get();

            if(lutadorBate.isVivo() && lutadorApanha.isVivo()) {
                double vidaLutadorApanha = lutadorApanha.getVida() - lutadorBateOptional.get().getForcaGolpe();

                if(vidaLutadorApanha <= 0) {
                    lutadorApanha.setVida(0);
                    lutadorApanha.setVivo(false);
                    repository.save(lutadorApanha);
                }
                else {
                    lutadorApanha.setVida(vidaLutadorApanha);
                }

                List<LutadorDTO> lista = new ArrayList<>();
                lista.add(new LutadorDTO(lutadorApanha));
                lista.add(new LutadorDTO(lutadorBate));
                return lista;
            }
            else {
                throw new DeadFighterException();
            }
        }
        else {
            throw new FighterNotFoundException();
        }
    }

}
