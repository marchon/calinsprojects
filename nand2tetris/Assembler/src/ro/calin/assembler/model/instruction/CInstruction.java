package ro.calin.assembler.model.instruction;

import ro.calin.assembler.model.Instruction;
import ro.calin.assembler.model.InstructionBuilder;

public class CInstruction implements Instruction {

    public static class Builder implements InstructionBuilder<CInstruction> {

        @Override
        public boolean matches(String code) {
            // TODO: check for actual correctness?
            return code.contains("=") || code.contains(";");
        }

        @Override
        public CInstruction build(String code) {
            String dest = null;
            String comp = null;
            String jump = null;

            String[] splitStr = code.split("=");
            int indexToSplit = 0;

            if (splitStr.length == 2) {
                dest = splitStr[0];
                indexToSplit = 1;
            }

            splitStr = splitStr[indexToSplit].split(";");

            comp = splitStr[0];

            if (splitStr.length == 2) {
                jump = splitStr[1];
            }

            return new CInstruction(dest, comp, jump);
        }
    }

    private String dest;
    private String comp;
    private String jump;

    CInstruction(String dest, String comp, String jump) {
        super();
        this.dest = dest;
        this.comp = comp;
        this.jump = jump;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    @Override
    public String toString() {
        return "CInstruction [dest=" + dest + ", comp=" + comp + ", jump=" + jump + "]";
    }
}
