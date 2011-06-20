class Reg2Afd {
	//teorie de la http://thor.info.uaic.ro/~torindan/index.php?show=teach/lab02.html
  public static void main(String args[])
  {
      String e = "(((a).(b))+((c)*))";

      Arbore arb = new Arbore();
      arb.GenerareArbore(e);
      
      System.out.println("Parcurgerea arborelui in preordine:");
      arb.RSD_etichetare(arb.root);    
      arb.RSD_parcurgere(arb.root);
      
      System.out.println("\n");
      
      System.out.println("Parcurgerea arborelui in preordine afisand etichetele:");
      arb.SDR_etichetare(arb.root);
      arb.RSD_parcurgere_etichete(arb.root);
      
      System.out.println("\n");
      
      System.out.println("Sistem tranzitiv:");
      SistTranz sT = new SistTranz(arb);
      sT.afisare();
      
      System.out.println("\n");
      
      System.out.println("Automat finit determinist rezultat:");
      AFD A = new AFD(sT);
      A.afisareAFD();
   }
}