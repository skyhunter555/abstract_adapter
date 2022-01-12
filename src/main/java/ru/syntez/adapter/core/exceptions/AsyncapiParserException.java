package ru.syntez.adapter.core.exceptions;
/**
 * Wrapper over RuntimeException. Includes additional options for formatting message text.
 *
 * @author Skyhunter
 * @date 24.12.2021
 */
public class AsyncapiParserException extends RuntimeException {

    public AsyncapiParserException(String message) {
	super(message);
    }

    public AsyncapiParserException(String messageFormat, Object... messageArgs) {
	    super(String.format(messageFormat, messageArgs));
    }

    public AsyncapiParserException(Throwable throwable) {
	super(throwable);
    }    
   
}
