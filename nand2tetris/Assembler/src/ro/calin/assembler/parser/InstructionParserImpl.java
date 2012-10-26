package ro.calin.assembler.parser;

import java.util.List;
import ro.calin.assembler.model.Instruction;
import ro.calin.assembler.model.InstructionBuilder;
import ro.calin.assembler.model.exception.NotAValidInstructionException;

public class InstructionParserImpl implements InstructionParser {

    private List<InstructionBuilder<?>> instructionTypes;

    public InstructionParserImpl(List<InstructionBuilder<?>> instructionTypes) {
//        instructionTypes.add(new PseudoInstruction.Builder());
//        instructionTypes.add(new CInstruction.Builder());
//        instructionTypes.add(new AInstructionWithConstant.Builder());
//        instructionTypes.add(new AInstructionWithSymbol.Builder());
        
        this.instructionTypes = instructionTypes;
    }

    @Override
    public Instruction parseInstruction(String code) {
        for (InstructionBuilder<?> builder : instructionTypes) {
            if(builder.matches(code)) {
                return builder.build(code);
            }
        }
        throw new NotAValidInstructionException(code);
    }
}
