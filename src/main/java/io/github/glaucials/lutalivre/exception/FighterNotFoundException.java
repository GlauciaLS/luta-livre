package io.github.glaucials.lutalivre.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FighterNotFoundException extends Exception{

    public FighterNotFoundException() {
        super("Lutador não encontrado.");
    }
}
