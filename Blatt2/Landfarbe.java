import java.util.Random;
import java.util.HashSet;

public class Landfarbe {
    // Die Laenge des Arrays ist die Anzahl der Laender, der Wert ist die Farbe
    public int[] farben;

    Landfarbe(int[] farben){
        this.farben = farben;
    }

    // eine zufaellige Farbe aendern
    public void mutieren(double rate){
        if (new Random().nextInt(100) > rate) {
            Random r = new Random();
            farben[r.nextInt(6)] = r.nextInt(5);
        }
    }
        
    int fitness() {
        int konflikte = 0;
        for (int i = 0; i < farben.length; i ++) {
            for (Border b : LaenderProblem.BORDERS) {
                // wenn die Grenze nicht akutelles land enthaelt, ueberspringen
                if (b.a != i && b.b != i) continue;
                // falls die Farben der beiden laender gleich sind, Konflikt
                if (farben[b.a] == farben[b.b]) konflikte ++;
            }
        }
        int farbenBenutzt = 0;
        HashSet<Integer> benutzt = new HashSet<>();
        for (int i : farben) {
            benutzt.add(i);
        }
        return 100 - (konflikte*5 + benutzt.size());       
    } 
}