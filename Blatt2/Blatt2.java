public class Blatt2 {
    public static void main(String[] args) {
        System.out.println("Experiment 1:");
        QueenProblem prob1 = new QueenProblem();
        prob1.StarteSimulation(300, 3, 3, 100);
        System.out.println("");
        System.out.println("Experiment 2:");
        LaenderProblem prob2 = new LaenderProblem();
        prob2.StarteSimulation(20, 10, 20, 10);
    }
}
