package ro.calin.assembler.model.instruction.ainstruction;

import ro.calin.assembler.model.InstructionBuilder;
import ro.calin.assembler.model.instruction.AInstruction;

public class AInstructionWithSymbol implements AInstruction {
    
    public static class Builder implements InstructionBuilder<AInstructionWithSymbol> {

        @Override
        public boolean matches(String code) {
            return code.matches("[a-zA-Z_\\.\\$:][a-zA-Z0-9_\\.\\$:]*");
        }

        @Override
        public AInstructionWithSymbol build(String code) {
            return new AInstructionWithSymbol(code.substring(1));
        }
    }

    private String symbol;

    public AInstructionWithSymbol(String symbol) {
        super();
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "AInstructionWithSymbol [symbol=" + symbol + "]";
    }
}
