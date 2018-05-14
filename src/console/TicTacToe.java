package console;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

enum Player {
    X(1), O(-1);
    int marker;
    Player(int m){
        marker = m;
    }
    public int getMarker(){
        return marker;
    }
}
public class TicTacToe {
    private int diag = 0;
    private int antidiag = 0;
    private int[] rows;
    private int[] cols;
    private int SIZE = 3;

    public enum GameStatus {
        PLAYER_X_WON, PLAYER_O_WON, GAME_TIED;
    }

    public TicTacToe(){
        Board board = new Board();
    }

    public static void main(String[] args){
        Random rand = new Random();
        Board game = new Board();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to have the first turn? (Y/N): ");
        String firstTurn = scanner.nextLine();
        boolean isFirstTurn = false;
        if(firstTurn.equals("Y")){
            isFirstTurn = true;
        }
        Cell cell = null;
        int res = 0;
        Player player = isFirstTurn ? Player.X : Player.O;
        while(!game.isFull() && res == 0){

            if(player == Player.O) {
                // computer move
                cell = game.currentMove();
            } else {
                // human move
                System.out.println("Player X, your move1");
                System.out.print("X Coordinate: ");
                int row = scanner.nextInt();
                System.out.print("Y Coordinate: ");
                int col = scanner.nextInt();
                while(!game.isValidMove(row, col)){
                    System.out.println("\nInvalid move! Try again!");
                    System.out.print("X Coordinate: ");
                    row = scanner.nextInt();
                    System.out.print("Y Coordinate: ");
                    col = scanner.nextInt();
                }
                cell = new Cell(row, col);
            }
            res = game.move(cell, player);
            player = (player == Player.X ? Player.O : Player.X);
            System.out.println();
        }

        GameStatus result = game.isFull() ? GameStatus.GAME_TIED : (res == 1 ? GameStatus.PLAYER_X_WON : GameStatus.PLAYER_O_WON);
        switch(result){
            case PLAYER_X_WON:
                System.out.println("Player X won!");
                break;
            case PLAYER_O_WON:
                System.out.println("Player 0 won!");
                break;
            case GAME_TIED:
                System.out.println("Tied!");
                break;
        }
    }
}
class Cell {
    private int row;
    private int col;
    private int val;
    private boolean occupied;

    public Cell(int index){
        this.row = index / Board.getSize();
        this.col = index % Board.getSize();
        occupied = false;
    }

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        this.val = 0;
        occupied = false;
    }

    public Cell(int row, int col, int val){
        this.row = row;
        this.col = col;
        this.val = val;
        occupied = true;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
        occupied = true;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("console.Cell (").append(row).append(", ").append(col).append(") ");
        return sb.toString();
    }
}

class Board {
    private static int SIZE = 3;
    private int[] rows;
    private int[] cols;
    private int diag;
    private int antidiag;
    private List<Integer> track;
    private Cell[][] cells;

    private Random generator = new Random();

    public Board(){
        rows = new int[SIZE];
        cols = new int[SIZE];
        track = new ArrayList<>();
        for(int i = 0; i < SIZE * SIZE; i++){
            track.add(i);
        }
        cells = new Cell[SIZE][SIZE];
        for(int r = 0; r < cells.length; r++){
            for(int c = 0; c < cells[0].length; c++){
                cells[r][c] = new Cell(r, c);
            }
        }
    }

    public Board(int size){
        this();
        SIZE = size;
    }

    public static int getSize(){
        return SIZE;
    }

    public Cell currentMove(){
        int nextIndex = generator.nextInt(track.size());
        return new Cell(track.get(nextIndex));
    }

    public int move(Cell cell, Player player){
        if(player == Player.O) {
            System.out.println("Player O is is making his move!");
            System.out.println("X coordinate: " + cell.getRow());
            System.out.println("Y coordinate: " + cell.getCol());
        }
        int toAdd = player.getMarker();
        int size = rows.length;
        int row = cell.getRow();
        int col = cell.getCol();

        Cell curr = cells[row][col];
        curr.setVal(toAdd);

        rows[row] += toAdd;
        cols[col] += toAdd;

        if(col == row){
            diag += toAdd;
        }
        if(col == cols.length - row - 1){
            antidiag += toAdd;
        }
        track.remove(new Integer(convertRCToNum(row, col)));
        return (Math.abs(rows[row]) == size || Math.abs(cols[col]) == size || Math.abs(diag) == size || Math.abs(antidiag) == size) ? player.marker : 0;
    }

    public boolean isValidMove(int row, int col){
        int index = convertRCToNum(row, col);
        for(int t: track){
            if(t == index){
                return true;
            }
        }
        return false;
    }

    public boolean isFull(){
        return track.size() < 1;
    }


    private int convertRCToNum(int row, int col){
        return row * SIZE + col;
    }

}




