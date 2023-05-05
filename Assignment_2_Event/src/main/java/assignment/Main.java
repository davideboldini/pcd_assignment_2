package assignment;

import assignment.Agent.GUI.GuiFormAgent;
import assignment.Model.Directory;
import assignment.Utility.Analyser.SourceAnalyzer;
import assignment.Utility.Analyser.SourceAnalyzerImpl;
import assignment.Utility.Chrono;
import assignment.Utility.Pair;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args){

        SourceAnalyzer sourceAnalyzer = new SourceAnalyzerImpl();
        Chrono chrono = new Chrono();

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
            Pair<Promise<TreeSet<Pair<File, Long>>>, Promise<Map<Pair<Integer,Integer>, Integer>>> res = sourceAnalyzer.getReport(new Directory(D), MAXL, NI);

            Future<Map<Pair<Integer,Integer>, Integer>> futInterval = res.getY().future();
            Future<TreeSet<Pair<File, Long>>> futFileTree = res.getX().future();

            CompositeFuture.join(futFileTree, futInterval).onComplete(ar -> {
               if (ar.succeeded()){
                   chrono.stop();

                   TreeSet<Pair<File,Long>> fileTree = (TreeSet<Pair<File, Long>>) ar.result().list().get(0);
                   Map<Pair<Integer,Integer>, Integer> mapInterval = (Map<Pair<Integer, Integer>, Integer>) ar.result().list().get(1);
                   System.out.println(fileTree.stream().toList().subList(0, N));
                   System.out.println(mapInterval);

                   System.out.println("Tempo impiegato: " + chrono.getTime());
               } else if (ar.failed()) {
                   System.out.println("Errore durante l'esecuzione");
               }
            });

        }else if(choose == 2){
            System.out.println("Selezionato: GUI");
            GuiFormAgent window = new GuiFormAgent(sourceAnalyzer);
            window.setVisible(true);
        }else {
            System.out.println("Errore: Input errato");
        }
    }
}