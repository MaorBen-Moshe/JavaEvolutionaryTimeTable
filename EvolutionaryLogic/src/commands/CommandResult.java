package commands;

public class CommandResult<T> {
    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isFailed() {
        return !errorMessage.replace(" ", "").isEmpty();
    }

    private T result;
    private String errorMessage;
}
