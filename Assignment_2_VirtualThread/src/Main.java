
import GUI.GuiForm;
import Model.Directory;
import utility.Analyser.SourceAnalyzer;
import utility.Analyser.SourceAnalyzerImpl;
import utility.Chrono;
import utility.Pair;
import utility.Printer;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        SourceAnalyzer sourceAnalyzer = new SourceAnalyzerImpl();
        Printer printer = new Printer();

        Scanner scan = new Scanner(System.in);

        System.out.println("Seleziona modalità: \n 1) Riga di comando \n 2) GUI \n");
        int choose = Integer.parseInt(scan.nextLine());

        if (choose == 1) {
            System.out.println("Selezionato: Riga di comando");

            System.out.println("Inserisci percorso: ");
            //final String D = "C:/Users/david/Desktop/Programmazione_concorrente_Ricci/Progetti/pcd_assignment_1/TestFolder2"; //args[0] - Percorso iniziale
            String D = scan.nextLine();
            D = D.replace("\\", "/");

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


            final Chrono chrono = new Chrono();

            sourceAnalyzer.initSource(MAXL, NI);

            chrono.start();

            Pair<Map<Pair<Integer, Integer>, Integer>, List<Pair<File,Long>>> report = sourceAnalyzer.getReport(new Directory(D));

            chrono.stop();

            printer.printFileLength(report.getY(), N);
            printer.printInterval(report.getX());

            System.out.println("Tempo impiegato: " + chrono.getTime());
            System.out.println("Termine programma");
        } else if(choose == 2){
            System.out.println("Selezionato: GUI");
            GuiForm window = new GuiForm();
            window.setVisible(true);
        }else {
            System.out.println("Errore: Input errato");
        }
    }
}