package ru.syntez.adapter.core.entities.asyncapi;

/**
 * ParseResultValue for Asyncapi with some message
 *
 * @author Skyhunter
 * @date 05.11.2020
 */
public class ValidatedResult {

    private final String resultMessage;
    private final Object resultValue;

    public ValidatedResult(String resultMessage) {
        this.resultMessage = resultMessage;
        this.resultValue = null;
    }

    public ValidatedResult(String resultMessage, Object resultValue) {
        this.resultMessage = resultMessage;
        this.resultValue = resultValue;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public Object getResultValue() {
        return resultValue;
    }

}
