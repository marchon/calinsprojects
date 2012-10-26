package ro.calin.assembler.parser;

import ro.calin.assembler.model.Instruction;


public interface InstructionParser {
    public Instruction parseInstruction(String code);
}
