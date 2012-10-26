package ro.calin.assembler.model;


public interface InstructionBuilder<T extends Instruction> {
    boolean matches(String code);
    T build(String code);
}
