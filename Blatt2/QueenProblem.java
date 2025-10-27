import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QueenProblem {
    Random r = new Random();
    private List<Schachfeld> population;
    private List<Schachfeld> matingpool;
    private int populationsGroesse, matingpoolGroesse, mutationsRate, crossOverRate, durchlaeufe;
    
    public void StarteSimulation(int populationsGroesse, int mutationsRate, int crossOverRate, int durchlaeufe) {
        this.populationsGroesse = populationsGroesse;
        this.mutationsRate = mutationsRate;
        this.crossOverRate = crossOverRate;
        this.durchlaeufe = durchlaeufe;
        this.matingpoolGroesse = matingpoolGroesse;

        population = new LinkedList<>();
        matingpool = new LinkedList<Schachfeld>();

        erstellePopulation();
    }

    private void erstellePopulation() {
        // zufaellige population erstellen
        for (int i = 0; i < populationsGroesse; i++){
            population.add(erstelleFeld());
        }
        
        for (int d = 0; d < durchlaeufe; d++){
            rouletteSelektion();

            // alle aus dem Matingpool hinzufuegen und Kinder hinzufuegen, bis man wieder genug hat
            int wirBrauchen = populationsGroesse - matingpool.size();
            List<Schachfeld> kinder = new LinkedList<>();
            for (int i = 0; i < populationsGroesse; i += 2) {
                Schachfeld s1 = matingpool.get(new Random().nextInt(matingpool.size()));
                Schachfeld s2 = matingpool.get(new Random().nextInt(matingpool.size()));
                
                // wenigstens ein Wert von Elternteil a
                // 1 < crossOverRate < 4
                int split = r.nextInt(7)+1;

                if (new Random().nextInt(100) > crossOverRate) {
                    kinder.add(crossOver(s1, s2, split));
                    kinder.add(crossOver(s2, s1, split));
                } else {
                    kinder.add(new Schachfeld(s1.queens));
                    kinder.add(new Schachfeld(s2.queens));
                }
            }
            population = kinder;
            
            // mutieren
            for (int i = 0; i < population.size(); i++) {
                Schachfeld mut = population.get(i);
                mut.mutieren(mutationsRate);
                population.set(i, mut);
            }
        }
    }

    // cross over mit zwei Individuen, ab einem Split Punkt von Teil b die Werte nehmen
    private Schachfeld crossOver(Schachfeld a, Schachfeld b, int split){
        Schachfeld child = new Schachfeld(new int[8]);
        for (int i = 0; i < 8; i++){
            if (i > split){
                child.queens[i] = a.queens[i];
            }
            else child.queens[i] = b.queens[i];
        }

        return child;
    }

    void rouletteSelektion() {

        List<Schachfeld> zuEntfernen = new LinkedList<>();
        // 
        matingpool = new ArrayList(population);

        int total = 0;
        for(Schachfeld s : matingpool) {
            total += s.fitness();
        }
        System.out.println("Durchschnitt vor selektion " + total / matingpool.size());
        
        double aktuelleWahr = 0d;
        
        // mit steigender Wahrscheinlichkeit Individuen mit kleinerer Fittness entfernen
        final double schritt = 1d / populationsGroesse;

        matingpool.sort(Comparator.comparingInt(Schachfeld::fitness));

        for (int i = 0; i < matingpool.size(); i++){
            aktuelleWahr += schritt;
            if (Math.random() > aktuelleWahr) {
                zuEntfernen.add(matingpool.get(i));
            }
        }
        matingpool.removeAll(zuEntfernen);
        
        total = 0;
        for(Schachfeld s : matingpool) {
            total += s.fitness();
        }
        System.out.println("Durchschnitt nach der Selektion " + total / matingpool.size());

        System.out.println("matingpool size = " + matingpool.size());
        // matingpool sortieren nach Fittness
        matingpool.sort(Comparator.comparingInt(Schachfeld::fitness));
        // hoechste Fittness anzeiegen
        System.out.println("beste Fittness: " + matingpool.getLast().fitness());
    }

    // Turnier erstellen und 1 Individuum in den Matingpool hinzufuegen
    // nicht mehr in Benutzung
    /*private void turnierSelektion(int turnierGroesse) {
        Schachfeld[] turnier = new Schachfeld[turnierGroesse];
        // zufaellige Individuen aus der population ins Turnier machen
        for (int i = 0; i < turnierGroesse; i++){
            turnier[i] = population[r.nextInt(populationsGroesse)];
        }

        // suche beste Fitness
        Schachfeld besteFittness = turnier[0];
        for (int j = 0; j < turnier.length; j++){
            if (turnier[j].fitness() < besteFittness.fitness()) besteFittness = turnier[j]; 
        }
        matingpool.add(new Schachfeld(besteFittness.queens));
    }*/

    // neues Schachfeld mit zufaelligen Koeniginnen erstellen
    private Schachfeld erstelleFeld() {
        int[] feldArray = new int[8];
        for (int i = 0; i < 8; i++){
            feldArray[i] = r.nextInt(8);
        }
        Schachfeld feld = new Schachfeld(feldArray);
        return feld;
    }
}