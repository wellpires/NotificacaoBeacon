package br.com.everis.notificacaobeacon.exception;

/**
 * Created by wgoncalv on 20/10/2017.
 */

public class RestException extends Exception {

    public RestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(Throwable cause) {
        super(cause);
    }
}
