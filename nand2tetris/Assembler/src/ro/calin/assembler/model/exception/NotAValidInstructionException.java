package ro.calin.assembler.model.exception;

public class NotAValidInstructionException extends RuntimeException {

    private String code;

    public NotAValidInstructionException(String code) {
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
