/**
 *@pastrez informatia despre functia delta a unui ST
 */
class Elem {
    int next1;
    int next2;
    char simbol;
    Elem(){
        this.next1 = this.next2 = 0;
        this.simbol = ' ';
    }
    Elem(char sim,int n1,int n2) {
        this.next1 = n1;
        this.next2 =n2;
        this.simbol = sim;
    }
}