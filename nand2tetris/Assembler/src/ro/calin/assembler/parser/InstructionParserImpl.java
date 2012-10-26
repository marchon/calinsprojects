package ro.calin.assembler.parser;

import java.util.ArrayList;
import java.util.List;
import ro.calin.assembler.model.Instruction;
import ro.calin.assembler.model.InstructionBuilder;
import ro.calin.assembler.model.instruction.CInstruction;
import ro.calin.assembler.model.instruction.PseudoInstruction;

public class InstructionParserImpl implements InstructionParser {

    private List<InstructionBuilder<?>> instructionTypes = new ArrayList<InstructionBuilder<?>>();

    public InstructionParserImpl() {
        instructionTypes.add(new PseudoInstruction.Builder());
        instructionTypes.add(new CInstruction.Builder());
    }

    @Override
    public Instruction parseInstruction(String code) {
        for (InstructionBuilder<?> builder : instructionTypes) {
            if(builder.matches(code)) {
                return builder.build(code);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new InstructionParserImpl().parseInstruction("M=A+D"));
    }
}
