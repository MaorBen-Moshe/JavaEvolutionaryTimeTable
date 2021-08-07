package commands;

public class CommandResult<T> {
    private T result;
    private String errorMessage;
    private boolean failed = false;


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
        failed = true;
    }

    public boolean isFailed() {
        return failed;
    }
}