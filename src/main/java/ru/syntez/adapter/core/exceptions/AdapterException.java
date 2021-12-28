package ru.syntez.adapter.core.exceptions;

/**
 * Wrapper over RuntimeException. Includes additional options for formatting message text.
 *
 * @author Skyhunter
 * @date 25.12.2021
 */
public class AdapterException extends RuntimeException {

    public AdapterException(String message) {
	    super(message);
    }
   
}
