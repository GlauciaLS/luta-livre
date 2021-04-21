package io.github.glaucials.lutalivre.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OverConcentrationException extends Exception {

    public OverConcentrationException() {
        super("Lutador já se concentrou 3 vezes!");
    }
}
