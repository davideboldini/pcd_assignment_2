package assignment;

import assignment.GUI.Gui;
import assignment.Model.Directory;
import assignment.Utility.Analyser.SourceAnalyzer;
import assignment.Utility.Analyser.SourceAnalyzerImpl;
import assignment.Utility.Chrono;
import assignment.Utility.Pair;
import assignment.Utility.Printer;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;


public class Main {
    public static void main(String[] args) {
        SourceAnalyzer sourceAnalyzer = new SourceAnalyzerImpl();
        Chrono chrono = new Chrono();
        Printer printer = new Printer();

        Scanner scan = new Scanner(System.in);

        System.out.println("Seleziona modalità: \n 1) Riga di comando \n 2) GUI \n");
        int choose = Integer.parseInt(scan.nextLine());

        if (choose == 1){
            System.out.println("Selezionato: Riga di comando");

            //System.out.println("Inserisci percorso: ");
            final String D = "C:/Users/david/Desktop/TestFolder2"; //args[0] - Percorso iniziale
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

            chrono.start();

            Pair<TreeSet<Pair<File, Long>>, Map<Pair<Integer,Integer>, Integer>> res = sourceAnalyzer.getReport(new Directory(D), MAXL, NI);

            chrono.stop();

            printer.printFileLength(res.getX(), N);
            printer.printInterval(res.getY());

            System.out.println("Tempo impiegato: " + chrono.getTime());

        }else if(choose == 2){
            System.out.println("Selezionato: GUI");
            Gui window = new Gui(sourceAnalyzer);
            window.setVisible(true);
        }else {
            System.out.println("Errore: Input errato");
        }
    }
}
