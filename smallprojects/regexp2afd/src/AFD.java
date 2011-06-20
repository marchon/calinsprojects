import java.util.ArrayList;

class AFD
{
    ArrayList<Character> sigma;
    ArrayList<Stare> S;
    ArrayList<Stare> F;
    Stare q0;
    ArrayList<Delta> tab_delta;
    boolean apartine(ArrayList<Stare> S, Stare x)//daca lista de stari S contine starea x
    {
        for (int i = 0; i< S.size(); i++)
            if(S.get(i).egal(x)) return true;
        return false;
    }
    
    int primaAp(ArrayList<Stare> S, Stare x)
    {
        for (int i = 0; i< S.size(); i++)
            if(S.get(i).egal(x)) return i;
        return -1;
    }
       
    AFD()
    {
        sigma = null;
        S = null;
        q0 = null;
        F = null;
        tab_delta = null;
    }
    
    int nextPozStare(ArrayList<Stare> S, boolean marcat[] )
    {
        for (int i=0; i<S.size(); i++)
            if (!marcat[i]) 
                return i;
        return -1;
    }
    
    AFD(SistTranz st)
    {
        S = new ArrayList<Stare>();
        q0 = new Stare();
        F = new ArrayList<Stare>();
        tab_delta = new ArrayList<Delta>();
        
        int i = 0;
        boolean marcat[] = new boolean[st.tabela.size()];     
        this.sigma = st.sigma;
        
        int stareFinala = st.stareFinala();        
        q0.add(1);
        this.lambda(st, 1, q0);
        S.add(q0);
        marcat[0] = false;
        if (q0.S.contains(stareFinala))
            F.add(q0);
        while ( (i = nextPozStare(S,marcat)) != -1)//mai am stari nemarcate
        {   
            for (int j = 0; j < sigma.size(); j++)
            {
                Stare aux = new Stare();
                if (aux.S.size() > 0)
                    aux.removeAll();
                this.lambdaStare(st, this.delta(st, S.get(i), sigma.get(j)),aux);
                if(aux.S.size() > 0)
                    if (!apartine(S,aux)/*!S.contains(aux)*/)
                    {
                        S.add(aux);
                        marcat[S.size()-1] = false;
                        if (aux.S.contains(stareFinala))
                            F.add(aux);
                        tab_delta.add(new Delta(i,(S.size()-1),sigma.get(j)));
                      } else{
                        tab_delta.add(new Delta(i,this.primaAp(S,aux),sigma.get(j)));
                        }
            }
            marcat[i] = true;
        }
    }
        
   void afisareDelta()
        {	
	    System.out.println("Functia de tranzitie : ");
            for (int i = 0; i< this.tab_delta.size(); i++)
            {
                System.out.print("  delta(");
                this.S.get(this.tab_delta.get(i).si).afisare();
                System.out.print(" ,"+this.tab_delta.get(i).simbol+")=");
                this.S.get(this.tab_delta.get(i).sf).afisare();
                System.out.println();
            }
        }
   
   void afisareAFD()
   {
       System.out.print("Starea initiala : ");
       this.q0.afisare();//din clasa Stare
       System.out.println();
       System.out.print("Stari finale : { ");
       for (int i=0; i<this.F.size()-1; i++)
       {
           this.F.get(i).afisare();
           System.out.print(" , ");
       }
           this.F.get(this.F.size()-1).afisare();
           System.out.println(" }");
        this.afisareDelta();
       
   }
   
    void lambda(SistTranz st, int x, Stare aux)
    {
      if (st.tabela.get(x).simbol==' ')
            {
                if ((st.tabela.get(x).next1!=0)&&(!aux.S.contains(st.tabela.get(x).next1)))
                {
                aux.add(st.tabela.get(x).next1);
                lambda(st,st.tabela.get(x).next1,aux);
                }
                if ((st.tabela.get(x).next2!=0)&&(!aux.S.contains(st.tabela.get(x).next2)))
                {
                aux.add(st.tabela.get(x).next2);
                lambda(st,st.tabela.get(x).next2,aux);
                }
            }
    }

    void lambdaStare(SistTranz st, Stare x, Stare aux)
    {
        for (int i = 0; i<x.S.size(); i++)
        if (!aux.S.contains(x.S.get(i)))
        {
            aux.add(x.S.get(i));
            this.lambda(st,x.S.get(i),aux);
        }
    }

    Stare delta(SistTranz st, Stare s, char simb)
    {
        Stare aux = new Stare();
        for (int i = 0; i < s.S.size(); i++)
            if (st.tabela.get(s.S.get(i)).simbol == simb)
            {    
                if (st.tabela.get(s.S.get(i)).next1!=0)
                {
                    aux.add(st.tabela.get(s.S.get(i)).next1);
                    this.lambda(st, st.tabela.get(s.S.get(i)).next1 ,aux);
                }
                if (st.tabela.get(s.S.get(i)).next2!=0)
                {
                    aux.add(st.tabela.get(s.S.get(i)).next2);
                    this.lambda(st, st.tabela.get(s.S.get(i)).next2 ,aux);
                }
            }
        return aux;
    }
}