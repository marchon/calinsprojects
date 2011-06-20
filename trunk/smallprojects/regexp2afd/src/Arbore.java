import java.util.Stack;
import java.util.ArrayList;

/**
 * @generarea unui arbore corespunzator unei expresii regulate
 */
class Arbore {
    Nod root;
    int contor = 0;
    ArrayList<Character> sigma;
    public void BinaryTree() {
        root = null;
    }
    /**
     *@constructia arborelui atasat expresiei regulate
stiva1-stiva operatorilor, stiva 2-stiva operanzilor(care va contine arborii partiali construiti)
     */
    public void GenerareArbore(String e) {
        Stack<Nod> Stiva1 = new Stack<Nod>();
        Stack<Nod> Stiva2 = new Stack<Nod>();
        sigma = new ArrayList<Character>();
        int i = 0;
        char c;
    //    Nod t;
        while (i < e.length()) {//i<lungimea expresiei regulate e
            c = e.charAt(i);//public char charAt(int index)
    //Returns the character at the specified index. An index ranges from 0 to
            //length() - 1. The first character of the sequence is at index 0,
            //the next at index 1, and so on, as for array indexing.
            if (c=='(')
                Stiva1.push(new Nod(c,null,null));
            if ((c>='a')&&(c<='z')) {
                Stiva2.push(new Nod(c,null,null));
                if (!this.sigma.contains(c)) this.sigma.add(c);
            }
            
            if (c == ')') {
                while(Stiva1.peek().info != '(')
                    build_tree(Stiva1,Stiva2);
                Stiva1.pop();
            }
            if ((c == '*')||(c == '+')||(c == '.')) {
                
                while((Stiva1.empty()==false)&&(prec(Stiva1.peek().info)>=prec(c)))
                    build_tree(Stiva1,Stiva2);
                
                Stiva1.push(new Nod(c,null,null));
                
            }
            i++;
        }//endwhile
        while(Stiva1.empty()==false)
            build_tree(Stiva1,Stiva2);
        root = Stiva2.pop();
    }
    /**
     *@parcuregerea arborelui in preordine si etichetarea nodurilor in afara de cele care contin ca info "."
     */
    void RSD_etichetare(Nod n) {
        if (n!=null) {
            if (n.info != '.') {
                contor++;
                n.eticheta = contor;
            }
            RSD_etichetare(n.st);
            RSD_etichetare(n.dr);
        }
    }
    void RSD_parcurgere(Nod n) {
        if (n!=null) {
            System.out.print(n.info+" ");
            RSD_parcurgere(n.st);
            RSD_parcurgere(n.dr);
        }
    }
    /**
     *@ parcurgerea arborelui in postordine si etichetarea nodurilor cu (i,f)
     *  care reprezinta starea initiala si cea finala a automatului corespunzator
     */
    void SDR_etichetare(Nod n) {
        if (n!=null) {
            SDR_etichetare(n.st);
            SDR_etichetare(n.dr);
            if (n.eticheta > 0) {
                n.Ni = 2*n.eticheta - 1;
                n.Nf = 2*n.eticheta;
            } else {
                n.Ni = n.st.Ni;
                n.Nf = n.dr.Nf;
            }
        }
    }
    /**
     *@afisarea continutului arborelui etichetat la pasul precedent printr-o parcurgere in preordine
     */
    void RSD_parcurgere_etichete(Nod n) {
        if (n!=null) {
            System.out.print(n.info+" "+n.eticheta+"("+n.Ni+" "+n.Nf+") ");
            RSD_parcurgere_etichete(n.st);
            RSD_parcurgere_etichete(n.dr);
        }
    }
    /**
     *@stabilirea precedentei operatorilor
     */
    int prec(char sim) {
        if (sim == '+') return 1;
        if (sim == '*') return 3;
        if (sim == '.') return 2;
        return -1;
    }

    void build_tree(Stack<Nod> Stiva1, Stack<Nod> Stiva2) {
//stiva1=stiva operatorilor, stiva 2-stiva operanzilor ce va contine arborii partiali construiti
        Nod t,S;
        Nod op = Stiva1.pop();//retin in op elementul din vf stivei si il sterg de acolo
        Nod D = Stiva2.pop();//retin in D primul operand din vf stivei si il sterg de acolo
        switch(op.info) {
            case '*':
            {
                t =new  Nod(op.info,D,null);
                Stiva2.push(t);
                break;
            }
            case '+':
            case '.':
            {
                S = Stiva2.pop();
                t = new Nod(op.info,S,D);
                Stiva2.push(t);
                break;
            }
        }
    }
}