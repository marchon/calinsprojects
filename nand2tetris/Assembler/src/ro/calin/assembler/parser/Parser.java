package ro.calin.assembler.parser;

import java.util.Scanner;


public interface Parser {
    void parse(Scanner input);
    String[] getMachineCode();
}
