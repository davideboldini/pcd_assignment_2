import Controller.GuiController;
import GUI.GuiForm;
import Monitor.FileMonitor;
import Monitor.IntervalMonitor;
import Monitor.ThreadMonitor;
import Threads.ControllerThread;
import Threads.DirectoryThread;
import utility.Chrono;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        final FileMonitor fileMonitor;
        final IntervalMonitor intervalMonitor;
        final ThreadMonitor threadMonitor;

        Scanner scan = new Scanner(System.in);

        System.out.println("Seleziona modalità: \n 1) Riga di comando \n 2) GUI \n");
        int choose = Integer.parseInt(scan.nextLine());

        if (choose == 1) {
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

            fileMonitor = new FileMonitor();
            intervalMonitor = new IntervalMonitor(MAXL, NI);
            threadMonitor = new ThreadMonitor();

            final ControllerThread controller = new ControllerThread(threadMonitor);
            final Thread tController = new Thread(controller);
            final Chrono chrono = new Chrono();

            chrono.start();

            Runnable r = new DirectoryThread(D, fileMonitor, intervalMonitor, threadMonitor, controller);
            Thread t = new Thread(r);
            controller.addThread(t);

            tController.start();
            tController.join();

            chrono.stop();

            System.out.println(fileMonitor.getFileLengthMap().stream().toList().subList(0, N));
            System.out.println(intervalMonitor.getIntervalMap());
            System.out.println("Tempo impiegato: " + chrono.getTime());
            System.out.println("Termine programma");
        } else if(choose == 2){
            System.out.println("Selezionato: GUI");
            fileMonitor = new FileMonitor();
            intervalMonitor = new IntervalMonitor();
            threadMonitor = new ThreadMonitor();
            GuiController guiController = new GuiController(fileMonitor, intervalMonitor, threadMonitor);
            GuiForm window = new GuiForm(guiController);

            guiController.addObserver(window);
            window.setVisible(true);
        }else {
            System.out.println("Errore: Input errato");
        }
    }
}