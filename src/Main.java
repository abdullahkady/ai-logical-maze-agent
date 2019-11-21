import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void GenGrid(String grid) {
        Scanner scanner = new Scanner(grid);
        scanner.useDelimiter(";|,");
        // m,n;ix,iy;tx,ty; s1x,s1y,s2x,s2y,s3x,s3y,s4x,s4y
        int[] dimensions = { scanner.nextInt(), scanner.nextInt() };
        int[] ironMan = { scanner.nextInt(), scanner.nextInt() };
        int[] thanos = { scanner.nextInt(), scanner.nextInt() };
        int[][] stones = new int[4][2];
        for (int i = 0; i < stones.length; i++) {
            stones[i][0] = scanner.nextInt();
            stones[i][1] = scanner.nextInt();
        }
        try {
            PrintWriter writer = new PrintWriter("KB.pl", "UTF-8");
            String gridPredicate = String.format("grid_dimensions(%d, %d).", dimensions[0], dimensions[1]);
            String ironManPredicate = String.format("ironman_position(%d, %d, s0).", ironMan[0], ironMan[1]);
            String thanosPredicate = String.format("thanos(%d, %d, s0).", thanos[0], thanos[1]);
            writer.println(gridPredicate);
            writer.println(ironManPredicate);
            writer.println(thanosPredicate);
            writer.println(generateHelperPredicate(stones));
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        // Write initial state predicates to KB.pl based on the grid.
    }

    private static String generateHelperPredicate(int[][] stones) {
        // @formatter:off
        /*
            * Inject the stone predicate into a snapped_helper predicate in the following
            * format:
            * snapped_helper(S) :-
            *      thanos(X, Y, s0),
            *      ironman_position([[]],[STONES], X,Y, I_S),
            *      S=result(snap, I_S).
        */
        // @formatter:on

        String result = "snapped_helper(S) :-\n";
        result += "\tthanos(X, Y, s0),\n";
        result += "\tironman_position([[]], [";
        for (int[] stone : stones) {
            result += String.format("[%d, %d], ", stone[0], stone[1]);
        }
        // Hacky way to get rid of extra comma.
        result = result.substring(0, result.length() - 2);
        result += "], X, Y, I_S),\n";
        result += "\tS=result(snap, I_S).\n";
        return result;
    }

    public static void main(String args[]) {
        // TODO: Hard code 2 KB1, KB2 files.
        GenGrid("5,5;1,2;3,4;1,1,2,1,2,2,3,3");
    }
}
