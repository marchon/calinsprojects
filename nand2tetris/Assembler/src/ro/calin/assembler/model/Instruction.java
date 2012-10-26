package ro.calin.assembler.model;

public interface Instruction {
    String getMachineCode() throws NoMatchingMachineCodeException;
}
