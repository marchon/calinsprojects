package ro.calin.assembler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ro.calin.assembler.model.Instruction;
import ro.calin.assembler.model.instruction.PseudoInstruction;
import ro.calin.assembler.model.instruction.ainstruction.AInstructionWithSymbol;

public class ParserImpl implements Parser {

    private InstructionParser instructionParser;
    private List<Instruction> instructions = new ArrayList<Instruction>();

    public ParserImpl(InstructionParser instructionParser) {
        this.instructionParser = instructionParser;
    }

    @Override
    public void parse(Scanner input) {
        
        List<Instruction> symbolInstructions = new ArrayList<Instruction>();
        
        while(input.hasNext()) {
            String line = prepare(input.next());
            if(line != null) {
                Instruction instruction = instructionParser.parseInstruction(line);
                if(instruction instanceof PseudoInstruction) {
                    //add labels to symbol table, need counter
                } else if(instruction instanceof AInstructionWithSymbol) {
                    symbolInstructions.add(instruction);
                    instructions.add(instruction);
                } else {
                    instructions.add(instruction);
                }
            }
        }
        
        //add to symbol table
        for (Instruction instruction : symbolInstructions) {
            //do we modify them???
        }

    }

    private String prepare(String next) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getMachineCode() {
        // TODO Auto-generated method stub
        return null;
    }

}
