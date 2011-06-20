/**
 *@retin informatia despre functia delta din AFD
 */
class Delta {
    int si, sf;
    char simbol;
    
    Delta() {
        si = sf = -1;
        simbol = ' ';
    }
    
    Delta(int newsi, int newsf, char simbolNou) {
        si = newsi;
        sf = newsf;
        simbol = simbolNou;
    }
}