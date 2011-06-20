import java.util.ArrayList;
/**
 *@retin informatia despre multimea starilor ST ce compun o stare a AFD
 */
class Stare {
    ArrayList<Integer> S;
    Stare() {
        this.S = new ArrayList<Integer>();
    }
    
    void add(int a) {
        S.add(a);
    }
    void removeAll() {
        for (int i=0;i<S.size();i++)
            S.remove(i);
    }
    
    void afisare() {
        if (S.size() > 0){
            int i;
            for ( i = 0; i < S.size()-1; i++)
                System.out.print(S.get(i)+" ");
            System.out.print(S.get(i));
        }
    }
    
    boolean egal(Stare s) {
        if (this.S.size() != s.S.size()) return false;
        
        for (int i = 0; i< this.S.size(); i++)
            if (!this.S.get(i).equals(s.S.get(i))) return false;
        
        return true;
        
    }
}