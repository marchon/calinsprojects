package ro.calin.assembler.model.exception;

public class ConstantTooBigException extends RuntimeException {

    private String code;

    public ConstantTooBigException(String code) {
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
