/**
 * @author zwzk8
 */
public class Error {
    private String errorType;
    private int lineNumber;

    public Error(String errorType, int lineNumber) {
        this.errorType = errorType;
        this.lineNumber = lineNumber;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return  lineNumber + " " + errorType;
    }

}
