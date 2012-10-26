package ro.calin.assembler.model.instruction;

import ro.calin.assembler.model.Instruction;
import ro.calin.assembler.model.InstructionBuilder;

public class PseudoInstruction implements Instruction {

    public static class Builder implements InstructionBuilder<PseudoInstruction> {

        @Override
        public boolean matches(String code) {
            return code.matches("\\([a-zA-Z_\\.\\$:][a-zA-Z0-9_\\.\\$:]*\\)");
        }

        @Override
        public PseudoInstruction build(String line) {
            return new PseudoInstruction(line.substring(1, line.length() - 1));
        }
    }

    private String labelName;

    PseudoInstruction(String labelName) {
        super();
        this.labelName = labelName;
    }

    @Override
    public String getMachineCode() {
        return null;
    }

    @Override
    public String toString() {
        return "PseudoInstruction [labelName=" + labelName + "]";
    }
}
