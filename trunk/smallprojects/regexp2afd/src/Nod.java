/**
 *@pastrez inforamtia dintr-un nod al arborelui
 */
class Nod {
    Nod st;
    Nod dr;
    char info;
    int eticheta;
    int Ni,Nf;
    Nod(char pinfo, Nod pst, Nod pdr){
        st = pst;
        dr = pdr;
        info = pinfo;
    }
}
