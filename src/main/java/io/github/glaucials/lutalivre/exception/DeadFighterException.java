package io.github.glaucials.lutalivre.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeadFighterException extends Exception {

    public DeadFighterException() {
        super("Ambos os lutadores devem estar vivos!");
    }
}
