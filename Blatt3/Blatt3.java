public class Blatt3 {
    public static void main(String[] args) {
        System.out.println("minimax ohne Pruning: ");
        Minimax m1 = new Minimax();
        System.out.println("minimax mit Pruning: ");
        MinimaxPruning m2 = new MinimaxPruning();
    }
}