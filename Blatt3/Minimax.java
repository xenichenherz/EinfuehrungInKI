public class Minimax {
    static final char LEER = ' ';
    static final char SPIELERX = 'X'; // Maximizing player
    static final char SPIELERO = 'O'; // Minimizing player
    static int knotenZaehler = 0;

    Minimax() {
        char[][] spielfeld = {
            { ' ', 'O', ' ' },
            { ' ', ' ', ' ' },
            { ' ', ' ', ' ' }
        };

        Zug besterZug = besterZugSpielerX(spielfeld);
        System.out.println("bester Zug: Spalte " + besterZug.spalte + ", Zeile " + besterZug.zeile);
        System.out.println("berechnete Knoten: " + knotenZaehler);
    }

    static Zug besterZugSpielerX(char[][] board) {
        int besterScore = -1000;
        Zug besterZug = null;

        // ganzes Feld durchlaufen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Zug suchen und X bei jedem leeren feld testen
                if (board[i][j] == LEER) {
                    board[i][j] = SPIELERX;
                    int score = minimax(board, false);
                    board[i][j] = LEER;

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
                    int score = minimax(feld, true);
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

    static int minimax(char[][] spielFeld, boolean maximieren) {
        Integer ergebnis = utility(spielFeld);
        if (ergebnis != null) return ergebnis;
        knotenZaehler++;

        if (maximieren) {
            int besterScore = -1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // jede Kombination suchen, bei der man auf der
                    // Stelle ein X platzieren kann
                    if (spielFeld[i][j] == LEER) {
                        spielFeld[i][j] = SPIELERX;
                        besterScore = Math.max(besterScore, minimax(spielFeld, false));
                        spielFeld[i][j] = LEER; // danach Spielfeld zuruecksetzen
                    }
                }
            }
            return besterScore;
        }
        
        else {
            int besterScore = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // jede Kombination suchen, bei der man auf der
                    // Stelle ein O platzieren kann
                    if (spielFeld[i][j] == LEER) {
                        spielFeld[i][j] = SPIELERO;
                        besterScore = Math.min(besterScore, minimax(spielFeld, true));
                        spielFeld[i][j] = LEER;
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