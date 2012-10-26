package ro.calin.assembler.model.instruction.ainstruction;

import ro.calin.assembler.model.InstructionBuilder;
import ro.calin.assembler.model.exception.ConstantTooBigException;
import ro.calin.assembler.model.instruction.AInstruction;

public class AInstructionWithConstant implements AInstruction {
    
    public static class Builder implements InstructionBuilder<AInstructionWithConstant> {

        @Override
        public boolean matches(String code) {
            return code.matches("@[0-9]*)");
        }

        @Override
        public AInstructionWithConstant build(String code) {
            try {
                return new AInstructionWithConstant(Integer.parseInt(code.substring(1)));
            }
            catch (NumberFormatException e) {
                throw new ConstantTooBigException(code);
            }
            
        }
    }

    private int constant;

    public AInstructionWithConstant(int constant) {
        super();
        this.constant = constant;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "AInstructionWithConstant [constant=" + constant + "]";
    }
}
