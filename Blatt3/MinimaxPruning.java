public class MinimaxPruning {
    static final char LEER = ' ';
    static final char SPIELERX = 'X'; // Maximizing player
    static final char SPIELERO = 'O'; // Minimizing player
    static int knotenZaehler = 0;
    static int pruningZaehler = 0;

    MinimaxPruning() {
        char[][] spielfeld = {
            { ' ', 'O', ' ' },
            { ' ', ' ', ' ' },
            { ' ', ' ', ' ' }
        };

        Zug besterZug = besterZugSpielerX(spielfeld);
        System.out.println("bester Zug: Spalte " + besterZug.spalte + ", Zeile " + besterZug.zeile);
        System.out.println("berechnete Knoten: " + knotenZaehler);
        System.out.println("Pruning wurde " + pruningZaehler + " Mal durchgefuehrt");
    }

    static Zug besterZugSpielerX(char[][] feld) {
        int besterScore = -1000;
        Zug besterZug = null;

        // ganzes Feld durchlaufen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Zug suchen und X bei jedem leeren feld testen
                if (feld[i][j] == LEER) {
                    feld[i][j] = SPIELERX;
                    int score = minimax(feld, false, -1000, 1000);
                    feld[i][j] = LEER;

                    if (score > besterScore) {
                        besterScore = score;
                        besterZug = new Zug(i, j);
                    }
                }
            }
        }
        return besterZug;
    }

    static Zug besterZugSpielerO(char[][] feld) {
        int besterScore = 1000;
        Zug besterZug = null;

        // ganzes Feld durchlaufen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Zug suchen und X bei jedem leeren feld testen
                if (feld[i][j] == LEER) {
                    feld[i][j] = SPIELERO;
                    int score = minimax(feld, true, -1000,1000);
                    feld[i][j] = LEER;

                    if (score < besterScore) {
                        besterScore = score;
                        besterZug = new Zug(i, j);
                    }
                }
            }
        }
        return besterZug;
    }

    static int minimax(char[][] feld, boolean maximieren, int alpha, int beta) {
        Integer ergebnis = utility(feld);
        if (ergebnis != null) return ergebnis;
        knotenZaehler++;

        if (maximieren) {
            int besterScore = -1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // jede Kombination suchen, bei der man auf der
                    // Stelle ein X platzieren kann
                    if (feld[i][j] == LEER) {
                        feld[i][j] = SPIELERX;
                        int score = minimax(feld, false, alpha, beta);
                        feld[i][j] = LEER;
                        besterScore = Math.max(besterScore, score);
                        alpha = Math.max(alpha, besterScore);
                        if (beta <= alpha) {
                            pruningZaehler++;
                            break; // Pruning
                        }
                    }
                }
            }
            return besterScore;
        }
        
        else
        {
            int besterScore = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (feld[i][j] == LEER) {
                        feld[i][j] = SPIELERO;
                        int score = minimax(feld, true, alpha, beta);
                        feld[i][j] = LEER;
                        besterScore = Math.min(besterScore, score);
                        beta = Math.min(beta, besterScore);
                        if (beta <= alpha) {
                            pruningZaehler++;
                            break; // Pruning
                        }
                    }
                }
            }
            return besterScore;
        }
    }


    // +1 fuer Gewinnen, -1 Bewertung fuer Verlieren
    static Integer utility(char[][] feld) {
        for (int i = 0; i < 3; i++) {
            // Gewinnen/Verlieren bei Zeilen
            if (feld[0][i] != LEER &&
                feld[0][i] == feld[1][i] &&
                feld[1][i] == feld[2][i])
                return feld[0][i] == SPIELERX ? 1 : -1;

            // Gewinnen/Verlieren bei Spalten
            // alle 3 Felder sollen nicht leer und gleich sein
            if (feld[i][0] != LEER &&
                feld[i][0] == feld[i][1] &&
                feld[i][1] == feld[i][2])
                return feld[i][0] == SPIELERX ? 1 : -1;
        }

        // Gewinnen/Verlieren bei beiden Diagnoalen
        if (feld[0][0] != LEER &&
            feld[0][0] == feld[1][1] &&
            feld[1][1] == feld[2][2])
            return feld[0][0] == SPIELERX ? 1 : -1;

        if (feld[0][2] != LEER &&
            feld[0][2] == feld[1][1] &&
            feld[1][1] == feld[2][0])
            return feld[0][2] == SPIELERX ? 1 : -1;

        // Alle Felder voll -> unentschieden
        boolean full = true;
        for (char[] spalte : feld){
            for (char zelle : spalte){
                if (zelle == LEER) full = false;
            }
        }
        return full ? 0 : null; // 0 = unentschieden, null = Spiel geht weiter
    }
}