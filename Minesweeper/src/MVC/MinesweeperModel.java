package MVC;

import java.util.Random;

public class MinesweeperModel {

    private final int ROWCOUNT = 9;
    private final int COLUMNCOUNT = 11;
    int mineCount;
    int countFlag;
    Cell[][] cellsTable;
    boolean firstStep;
    boolean gameOver;

    public MinesweeperModel(int mineCount)
    {
        this.mineCount = mineCount;
        countFlag = mineCount;
        firstStep = true;
        gameOver = false;
        cellsTable = new Cell[ROWCOUNT][COLUMNCOUNT];
        for (int i = 0; i <  ROWCOUNT; i++) {
            for (int j = 0; j < COLUMNCOUNT; j++) {
                cellsTable[i][j] = new Cell(i,j);
            }
        }
    }

    public boolean isWin() {
        for (int i = 0; i < ROWCOUNT; i++) {
            for (int j = 0; j < COLUMNCOUNT; j++) {
                if ((!cellsTable[i][j].mined) && (cellsTable[i][j].state != "opened"))
                    return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void openCell(int row, int column) {
        cellsTable[row][column].open();

        if (firstStep) {
            firstStep = false;
            generateMines();
        }

        if (cellsTable[row][column].mined)
            gameOver = true;
        else {
            cellsTable[row][column].counter = countMinesAroundCell(row, column);
            if (cellsTable[row][column].counter == 0)
                openCellNeighbours(row, column);
        }
    }

    public void flaggedCell(int row, int column) {
        cellsTable[row][column].flagged();
        if (cellsTable[row][column].state == "flagged")
            countFlag--;
        if (cellsTable[row][column].state == "closed")
            countFlag++;
    }

    private void generateMines() {
        for (int i = 0; i < mineCount; i++) {
            int row = new Random().nextInt(ROWCOUNT);
            int column = new Random().nextInt(COLUMNCOUNT);
            if (cellsTable[row][column].state != "opened" && (!cellsTable[row][column].mined))
                cellsTable[row][column].mined = true;
        }
    }

    private int countMinesAroundCell(int row, int column) {
        int count = 0;
        int startRow = (row - 1 >= 0) ? row - 1 : row;
        int finishRow = (row + 1 < ROWCOUNT) ? row + 1 : row;
        int startColumn = (column - 1 >= 0) ? column - 1 : column;
        int finishColumn = (column + 1 < COLUMNCOUNT) ? column + 1 : column;
        for (int i = startRow; i <= finishRow; i++) {
            for (int j = startColumn; j <= finishColumn; j++) {
                if (cellsTable[i][j].mined)
                    count++;
            }
        }
        return count;
    }

    private void openCellNeighbours(int row, int column){
        int startRow = (row - 1 >= 0) ? row - 1 : row;
        int finishRow = (row + 1 < ROWCOUNT) ? row + 1 : row;
        int startColumn = (column - 1 >= 0) ? column - 1 : column;
        int finishColumn = (column + 1 < COLUMNCOUNT) ? column + 1 : column;
        for (int i = startRow; i <= finishRow; i++) {
            for (int j = startColumn; j <= finishColumn; j++) {
                if (cellsTable[i][j].state == "closed")
                    openCell(i,j);
            }
        }
    }
}
