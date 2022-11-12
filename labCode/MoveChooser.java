import java.util.PriorityQueue;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Comparator;


public class MoveChooser {

    public static int[][] WEIGHTS = {{120, -20, 20, 5, 5, 20, -20, 120},
                                     {-20, -40, -5, -5, -5, -5, -40, -20},
                                     {20, -5, 15, 3, 3, 15, -5, 20},
                                     {5, -5, 3, 3, 3, 3, -5, 5},
                                     {5, -5, 3, 3, 3, 3, -5, 5},
                                     {20, -5, 15, 3, 3, 15, -5, 20},
                                     {-20, -40, -5, -5, -5, -5, -40, -20},
                                     {120, -20, 20, 5, 5, 20, -20, 120}};


                                     // PriorityQueue<Map.Entry<Move, Integer>> queue = new PriorityQueue<>(Map.Entry.comparingByValue(Comparator.reverseOrder()));

    public static Move chooseMove(BoardState boardState){

	    int searchDepth = Othello.searchDepth;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        BoardState temp;

        PriorityQueue<Map.Entry<Move, Integer>> queue = new PriorityQueue<>(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int bestScore = Integer.MIN_VALUE;
        int score;
        Move bestMove = null;

        for (Move m : boardState.getLegalMoves()) {
            temp = boardState.deepCopy();
            temp.makeLegalMove(m.x, m.y);
            // System.out.println(m.x + " " + m.y);


            score = minValue(temp, alpha, beta, searchDepth - 1);
            if (score >= bestScore) {
                bestScore = score;
                bestMove = m;
            }
            if (score >= beta) {
                break;
            }
            alpha = Math.max(alpha, score);

        }

        // if (bestMove) {
        //     System.out.println("No Moves");
        //     return null;
        // }


        return bestMove;

        // ArrayList<Move> moves= boardState.getLegalMoves();
        // if(moves.isEmpty()){
        //     return null;
	    // }
        // return moves.get(0);
    }

    public static int maxValue (BoardState boardState, int alpha, int beta, int depth) {
        if (depth <= 0 || boardState.gameOver()) {
            return staticVal(boardState);
        }

        int v = Integer.MIN_VALUE;
        BoardState temp;

        if (boardState.getLegalMoves().isEmpty()) {
            temp = boardState.deepCopy();
            temp.colour = -temp.colour;

            v = Math.max(v, minValue(temp, alpha, beta, depth - 1));

            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }

        for (Move m : boardState.getLegalMoves()) {
            temp = boardState.deepCopy();
            temp.makeLegalMove(m.x, m.y);
            v = Math.max(v, minValue(temp, alpha, beta, depth - 1));
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }

        return v;
    }

    public static int minValue (BoardState boardState, int alpha, int beta, int depth) {
        if (depth <= 0 || boardState.gameOver()) {
            return staticVal(boardState);
        }

        int v = Integer.MAX_VALUE;
        BoardState temp;

        if (boardState.getLegalMoves().isEmpty()) {
            temp = boardState.deepCopy();
            temp.colour = -temp.colour;

            v = Math.min(v, maxValue(temp, alpha, beta, depth - 1));
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }

        for (Move m : boardState.getLegalMoves()) {
            temp = boardState.deepCopy();
            temp.makeLegalMove(m.x, m.y);
            v = Math.min(v, maxValue(temp, alpha, beta, depth - 1));
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }

        return v;
    }


    public static int staticVal(BoardState boardState) {


        int val = 0;

        if (boardState.gameOver()) {
            int pieces = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    pieces += boardState.getContents(i, j);
                }
            }
            if (pieces > 0) {
                val = Integer.MAX_VALUE;
            }
            else if (pieces < 0) {
                val = Integer.MIN_VALUE;
            }

        }
        else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    val += WEIGHTS[i][j] * boardState.getContents(i, j);
                }

            }
        }

        return val;
    }
}
