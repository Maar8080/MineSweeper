package com.cookandroid.minesweeper123;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private int rows, cols, mines;
    private Cell[][] cells;
    private GridLayout gridLayout;
    private TextView minesLeftTextView, timerTextView;
    private ImageButton restartButton, flagModeButton;
    private boolean isFlagMode = false;
    private int minesLeft, seconds = 0;
    private Handler timerHandler = new Handler();
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 10);
        cols = intent.getIntExtra("cols", 10);
        mines = intent.getIntExtra("mines", 12);

        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(cols);
        minesLeftTextView = findViewById(R.id.mineCounter);
        timerTextView = findViewById(R.id.timerText);
        restartButton = findViewById(R.id.restartButton);
        flagModeButton = findViewById(R.id.modeSwitchButton);

        initializeGrid();
        startTimer();
    }

    private void initializeGrid() {
        cells = new Cell[rows][cols];
        gridLayout.removeAllViews();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = new Cell(this);
                cell.setRow(row);
                cell.setCol(col);
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFlagMode) {
                            cell.toggleFlag();
                            updateMinesLeft(cell.isFlagged());
                        } else {
                            if (!cell.isFlagged()) {
                                cell.reveal();
                                if (cell.isMine()) {
                                    gameOver(false, cell);
                                } else {
                                    if (cell.getNeighboringMines() == 0) {
                                        revealNeighbors(cell);
                                    }
                                    checkWin();
                                }
                            }
                        }
                    }
                });
                cells[row][col] = cell;
                gridLayout.addView(cell);
            }
        }
        setMines();
        setNeighbors();
    }

    private void setMines() {
        minesLeft = mines;
        minesLeftTextView.setText(": " + minesLeft);
        int minesSet = 0;
        while (minesSet < mines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            if (!cells[row][col].isMine()) {
                cells[row][col].setMine(true);
                minesSet++;
            }
        }
    }

    private void setNeighbors() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMine()) {
                    int mines = countNeighboringMines(row, col);
                    cells[row][col].setNeighboringMines(mines);
                }
            }
        }
    }

    private int countNeighboringMines(int row, int col) {
        int mines = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (row + i >= 0 && row + i < rows && col + j >= 0 && col + j < cols) {
                    if (cells[row + i][col + j].isMine()) {
                        mines++;
                    }
                }
            }
        }
        return mines;
    }

    private void revealNeighbors(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (row + i >= 0 && row + i < rows && col + j >= 0 && col + j < cols) {
                    Cell neighbor = cells[row + i][col + j];
                    if (!neighbor.isRevealed() && !neighbor.isFlagged()) {
                        neighbor.reveal();
                        if (neighbor.getNeighboringMines() == 0) {
                            revealNeighbors(neighbor);
                        }
                    }
                }
            }
        }
    }

    private void updateMinesLeft(boolean isFlagged) {
        if (isFlagged) {
            minesLeft--;
        } else {
            minesLeft++;
        }
        minesLeftTextView.setText(": " + minesLeft);
    }

    private void checkWin() {
        boolean isWin = true;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMine() && !cells[row][col].isRevealed()) {
                    isWin = false;
                    break;
                }
            }
            if (!isWin) break;
        }
        if (isWin) {
            gameOver(true, null);
        }
    }

    private void gameOver(boolean isWin, Cell clickedMine) {
        isRunning = false;
        String message = isWin ? "You Win!" : "Game Over!";
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[row][col];
                if (!isWin) {
                    if (cell.isMine()) {
                        cell.setMineRevealed(true);
                    }
                    if (cell.isFlagged() && !cell.isMine()) {
                        cell.setFlaggedIncorrect(true);
                    }
                }
                cell.reveal();
            }
        }
        if (clickedMine != null) {
            clickedMine.setMineClicked(true);
        }
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }

    public void onRestart(View view) {
        initializeGrid();
        startTimer();
    }

    public void onToggleFlagMode(View view) {
        isFlagMode = !isFlagMode;
        flagModeButton.setImageResource(isFlagMode ? R.drawable.flag_mod : R.drawable.reveal_mod);
    }

    private void startTimer() {
        seconds = 0;
        isRunning = true;
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                seconds++;
                int minutes = seconds / 60;
                int secs = seconds % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, secs));
                timerTextView.setTextSize(Dimension.SP, 32);
                timerHandler.postDelayed(this, 1000);
            }
        }
    };
}
