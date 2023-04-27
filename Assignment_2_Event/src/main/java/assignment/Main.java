package assignment;

import assignment.Model.Directory;
import assignment.Utility.Analyser.SourceAnalyzerImpl;

public class Main {

    public static void main(String[] args){

        final String D = "C:/Users/david/Desktop/Programmazione_concorrente_Ricci/Progetti/pcd_assignment_1/TestFolder2"; //args[0] - Percorso iniziale

        SourceAnalyzerImpl sourceAnalyzer = new SourceAnalyzerImpl();

        sourceAnalyzer.getReport(new Directory(D));
    }
}