package com.example.mygram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author s.melekhin
 * @since 04 май 2022 г.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String msg) {
        super(msg);
    }

}
