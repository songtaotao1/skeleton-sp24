package game2048logic;

import game2048rendering.Board;
import game2048rendering.Side;
import game2048rendering.Tile;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    private final Board board;
    /** Current score. */
    private int score;

    /* Coordinate System: column x, row y of the board (where x = 0,
     * y = 0 is the lower-left corner of the board) will correspond
     * to board.tile(x, y).  Be careful!
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (x, y) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score) {
        board = new Board(rawValues);
        this.score = score;
    }

    /** Return the current Tile at (x, y), where 0 <= x < size(),
     *  0 <= y < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int x, int y) {
        return board.tile(x, y);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }


    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists() || !atLeastOneMoveExists();
    }

    /** Returns this Model's board. */
    public Board getBoard() {
        return board;
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public boolean emptySpaceExists() {
        // TODO: Task 2. Fill in this function.
        boolean foundEmpty = false;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Tile t = board.tile(row, col);  // 通过 b.tile(row, col) 获取对应位置的 Tile
                if (t == null) {
                    foundEmpty = true;
                }

            }
        }
        return foundEmpty;

    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public boolean maxTileExists() {
        boolean foundmax = false;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Tile t = board.tile(row, col);  // 通过 b.tile(row, col) 获取对应位置的 Tile
                if (t!=null&& t.value() == MAX_PIECE) {
                    foundmax = true;
                    return  foundmax;
                }

            }
        }
        return foundmax;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public boolean atLeastOneMoveExists() {
        boolean atLeastblank = false;
        boolean next_same = false;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                Tile t = board.tile(col, row);
                Tile tl = (row - 1 >= 0) ? board.tile(col, row-1) : null;
                Tile tr = (row + 1 < 4) ? board.tile(col, row + 1) : null;
                Tile tu = (col - 1 >= 0) ? board.tile(col - 1, row) : null;
                Tile td = (col + 1 < 4) ? board.tile( col+ 1, row) : null;


                if (t==null) {
                    atLeastblank = true;
                }else {
                    if ((tl != null && t.value() == tl.value()) ||
                            (tr != null && t.value() == tr.value()) ||
                            (tu != null && t.value() == tu.value()) ||
                            (td != null && t.value() == td.value())) {
                        next_same = true;
                    }
                }


            }
        }
        return  atLeastblank || next_same;
    }

    /**
     * Moves the tile at position (x, y) as far up as possible.
     *
     * Rules for Tilt:
     * 1. If two Tiles are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     */
    public void moveTileUpAsFarAsPossible(int x, int y) {
        Tile currTile = board.tile(x, y);
        int myValue = currTile.value();


        if (currTile == null) {
            return; // 当前位置没有图块
        }

        int currentRow = y;
        int targetRow = currentRow; // 初始化为当前行

        // 从当前行的上一行开始向上遍历
        for (int row = currentRow + 1; row <=3 ; row++) {
            Tile tileAtRow = board.tile(x, row);
            if (board.tile(x, row) == null) {
                targetRow = row; // 更新目标行
            } else {
                if(!tileAtRow.wasMerged()&& myValue==tileAtRow.value() ){
                    targetRow = row;
                    score=score+myValue*2;
                }
                break;
            }
        }

        // 仅当需要移动时调用一次 move
        if (targetRow != currentRow) {
            board.move(x, targetRow, currTile);
        }
    }






























    /** Handles the movements of the tilt in column x of the board
     * by moving every tile in the column as far up as possible.
     * The viewing perspective has already been set,
     * so we are tilting the tiles in this column up.
     * */
    public void tiltColumn(int x) {
        // TODO: Task 7. Fill in this function.


             for (int row = 0; row <3; row++) {
                Tile t = board.tile(x, row);
                if (t != null) {
                    moveTileUpAsFarAsPossible(x, row);
                }
            }



    }

    public void tilt(Side side) {
        // TODO: Tasks 8 and 9. Fill in this function.
        board.setViewingPerspective(side);
        for (int row = 2; row>=0 ; row--) {
            for (int col = 3; col >=0; col--) {
                Tile t = board.tile(col, row);
                if (t!= null) {
                    moveTileUpAsFarAsPossible(col, row);
                }
            }
        }
        board.setViewingPerspective(Side.NORTH);

    }

    /** Tilts every column of the board toward SIDE.
     */
    public void tiltWrapper(Side side) {
        board.resetMerged();
        tilt(side);
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int y = size() - 1; y >= 0; y -= 1) {
            for (int x = 0; x < size(); x += 1) {
                if (tile(x, y) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(x, y).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (game is %s) %n", score(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Model m) && this.toString().equals(m.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
