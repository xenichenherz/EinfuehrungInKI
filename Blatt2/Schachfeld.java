import java.util.Random;

public class Schachfeld {
    public int[] queens;
    Schachfeld(int[] feld){
        this.queens = feld;
    }

    // maximale Konflikte: 28
    // z.B. bei: int[] queens = {1,1,1,1,1,1,1,1};
    public int fitness(){
        return 100 - (konflikte(queens));
    }

    // Konflikt auf diagonaler Ebene suchen (werden doppelt gezaehlt)
    public static int konflikte(int[] queens) {
        int conflicts = 0; 
        for (int i = 0; i < queens.length; i++) {
            for (int j = 0; j < queens.length; j++) {
                if (i == j) continue;

                // Diagonaler Konflikt: Abstand in Spalte == Abstand in Zeile
                if (Math.abs(i - j) == Math.abs(queens[i] - queens[j])) {
                    conflicts++;
                }
                // Zeile
                if (queens[i] == queens[j]) conflicts ++;
            }
        }
        return conflicts;
    }

    // mutieren indem man 1 Zahl zufaellig macht
    public void mutieren(double rate){
        for (int i = 0; i < 8; i++) {
            if (new Random().nextInt(100) > rate) {
                queens[i] = new Random().nextInt(8);
            }
        }
    }
}