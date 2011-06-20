import java.util.ArrayList;

class SistTranz {
    ArrayList<Elem> tabela = new ArrayList<Elem>();
    int nrStari;
    ArrayList<Character> sigma = new ArrayList<Character>();
    SistTranz() {
        this.nrStari = 0;
        this.tabela = null;
        this.sigma = null;
    }
    SistTranz(Arbore a) {
        this.tabela = new ArrayList<Elem>();
//pt aparitiile opratorilor + si *, simbolurile din alfabet cat si pentru landa daca apare explicit in expre reg
//introduc 2 noi stari in automatul construit
        this.nrStari = 2*(a.contor);
        this.sigma = a.sigma;
        for (int i=0; i<=this.nrStari; i++)
            this.tabela.add(new Elem());
        this.creare_vect(a.root);
    }
    /**
     *@constructia matricii de tranzitii corespunzatore expresiei regulate
     */
    public void creare_vect(Nod n) {
        if (n!=null) {
            switch (n.info) {
                case '+' :
                {
                    tabela.get(n.Ni).next1= n.st.Ni;
                    tabela.get(n.Ni).next2 = n.dr.Ni;
                    tabela.get(n.st.Nf).next1 = n.Nf;
                    tabela.get(n.dr.Nf).next1 = n.Nf;
                    break;
                }
                case '.':
                {
                    tabela.get(n.st.Nf).next1 = n.dr.Ni;
                    break;
                }
                case '*':
                {
                    tabela.get(n.Ni).next1 = n.st.Ni;
                    tabela.get(n.Ni).next2 = n.Nf;
                    tabela.get(n.st.Nf).next1 = n.st.Ni;
                    tabela.get(n.st.Nf).next2 = n.Nf;
                    break;
                }
                default:
                {
                    tabela.get(n.Ni).simbol = n.info;
                    tabela.get(n.Ni).next1 = n.Nf;
                    break;
                }
            }
            creare_vect(n.st);
            creare_vect(n.dr);
        }
    }
    /**
     *@afisarea matricii de tranzitie
     */
    public void afisare() {
        System.out.println("Stare "+"Simbol "+"Next1"+" Next2");
        for (int i = 1; i < tabela.size(); i++) {
            System.out.println("  "+i+"      "+tabela.get(i).simbol+"     "+tabela.get(i).next1+"      "+tabela.get(i).next2);
        }
    }
   
    int stareFinala()
    {
        for (int i = 1; i < this.tabela.size();i++)
            if ((this.tabela.get(i).next1 == 0) &&(this.tabela.get(i).next2 == 0))
                return i;
        return 0;
    }
}