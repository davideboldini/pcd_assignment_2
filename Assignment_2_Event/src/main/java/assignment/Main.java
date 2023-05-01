package assignment;

import assignment.Agent.GUI.GuiFormAgent;
import assignment.Model.Directory;
import assignment.Utility.Analyser.SourceAnalyzerImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        SourceAnalyzerImpl sourceAnalyzer = new SourceAnalyzerImpl();

        Scanner scan = new Scanner(System.in);

        System.out.println("Seleziona modalità: \n 1) Riga di comando \n 2) GUI \n");
        int choose = Integer.parseInt(scan.nextLine());

        if (choose == 1){
            System.out.println("Selezionato: Riga di comando");

            //System.out.println("Inserisci percorso: ");
            final String D = "C:/Users/david/Desktop/Programmazione_concorrente_Ricci/Progetti/pcd_assignment_1/TestFolder2"; //args[0] - Percorso iniziale
            //String D = scan.nextLine();
            //D = D.replace("\\", "/");

            System.out.println("Elementi da visualizzare della mappa: ");
            //final int N = 5; //N elementi da visualizzare della mappa
            int N = scan.nextInt();

            System.out.println("Limite massimo: ");
            //final int MAXL = 1000; //args[2] - Limite massimo
            int MAXL = scan.nextInt();

            System.out.println("Numero intervalli: ");
            //final int NI = 5; //args[1] - Numero intervalli
            int NI = scan.nextInt();

            System.out.println("Calcolo in corso...\n");

            sourceAnalyzer.getReport(new Directory(D), MAXL, NI);
        }else if(choose == 2){
            System.out.println("Selezionato: GUI");
            GuiFormAgent window = new GuiFormAgent(sourceAnalyzer);
            window.setVisible(true);
        }else {
            System.out.println("Errore: Input errato");
        }
    }
}