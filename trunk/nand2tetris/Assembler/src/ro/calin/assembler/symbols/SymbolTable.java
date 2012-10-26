package ro.calin.assembler.symbols;

public class SymbolTable {

    private SymbolTable() {
    }

//    public void registerRamSymbol(String name) {
//
//    }
//
//    public void registerRamSymbol(String name) {
//
//    }

    private static SymbolTable instance;

    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }
}
