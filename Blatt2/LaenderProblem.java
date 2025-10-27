import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LaenderProblem {
    public final static Border[] BORDERS = {
        new Border(1,2),
        new Border(1,3),
        new Border(2,3),
        new Border(2,4),
        new Border(3,4),
        new Border(4,5),
    };

    Random r = new Random();
    private List<Landfarbe> population;
    private List<Landfarbe> matingpool;
    private int populationsGroesse, matingpoolGroesse, mutationsRate, crossOverRate, durchlaeufe;
    
    public void StarteSimulation(int populationsGroesse, int mutationsRate, int crossOverRate, int durchlaeufe) {
        this.populationsGroesse = populationsGroesse;
        this.mutationsRate = mutationsRate;
        this.crossOverRate = crossOverRate;
        this.durchlaeufe = durchlaeufe;
        this.matingpoolGroesse = matingpoolGroesse;

        population = new LinkedList<>();
        matingpool = new LinkedList<Landfarbe>();

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
            List<Landfarbe> kinder = new LinkedList<>();
            for (int i = 0; i < populationsGroesse; i += 2) {
                Landfarbe s1 = matingpool.get(new Random().nextInt(matingpool.size()));
                Landfarbe s2 = matingpool.get(new Random().nextInt(matingpool.size()));
                
                // wenigstens ein Wert von Elternteil a
                // 1 < crossOverRate < 4
                int split = r.nextInt(5)+1;

                if (new Random().nextInt(100) > crossOverRate) {
                    kinder.add(crossOver(s1, s2, split));
                    kinder.add(crossOver(s2, s1, split));
                } else {
                    kinder.add(new Landfarbe(s1.farben));
                    kinder.add(new Landfarbe(s2.farben));
                }
            }

            population = kinder;
            
            // mutieren
            for (int i = 0; i < population.size(); i++) {
                Landfarbe mut = population.get(i);
                mut.mutieren(mutationsRate);
                population.set(i, mut);
            }
        }
    }

    // cross over mit zwei Individuen, ab einem Split-Punkt von Elternteil b die Werte nehmen
    private Landfarbe crossOver(Landfarbe a, Landfarbe b, int split){
        Landfarbe child = new Landfarbe(new int[6]);
        for (int i = 0; i < 6; i++){
            if (i > split){
                child.farben[i] = a.farben[i];
            }
            else child.farben[i] = b.farben[i];
        }

        return child;
    }

    void rouletteSelektion() {

        List<Landfarbe> zuEntfernen = new LinkedList<>();
        matingpool = new ArrayList(population);

        int total = 0;
        for(Landfarbe s : matingpool) {
            total += s.fitness();
        }
        System.out.println("Durchschnitt vor Selektion " + total / matingpool.size());
        
        double aktuelleWahr = 0d;
        
        // mit steigender Wahrscheinlichkeit Individuen mit kleinerer Fittness entfernen
        final double schritt = 1d / populationsGroesse;

        matingpool.sort(Comparator.comparingInt(Landfarbe::fitness));

        for (int i = 0; i < matingpool.size(); i++){
            aktuelleWahr += schritt;
            if (Math.random() > aktuelleWahr) {
                zuEntfernen.add(matingpool.get(i));
            }
        }
        matingpool.removeAll(zuEntfernen);
        
        total = 0;
        for(Landfarbe s : matingpool) {
            total += s.fitness();
        }
        System.out.println("Durchschnitt nach der Selektion " + total / matingpool.size());

        System.out.println("matingpool size = " + matingpool.size());
        matingpool.sort(Comparator.comparingInt(Landfarbe::fitness));
        System.out.println("hoechste Fittness: " + matingpool.getLast().fitness()); // 97: Loesung mit 3 Farben
    }

    // neues Landkarte mit zufaelligen Farben erstellen
    private Landfarbe erstelleFeld() {
        int[] feldArray = new int[6];
        for (int i = 0; i < 6; i++){
            feldArray[i] = r.nextInt(5);
        }
        Landfarbe feld = new Landfarbe(feldArray);
        return feld;
    }
}