package com.cookandroid.minesweeper123;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Cell extends LinearLayout {

    private int row, col, neighboringMines;
    private boolean isMine, isRevealed, isFlagged, mineClicked, mineRevealed, flaggedIncorrect;
    private ImageView imageView;

    public Cell(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        imageView = new ImageView(context);
        imageView.setLayoutParams(new LayoutParams(48, 48));
        addView(imageView);

        reset();
    }

    public void reset() {
        isMine = false;
        isRevealed = false;
        isFlagged = false;
        mineClicked = false;
        mineRevealed = false;
        flaggedIncorrect = false;
        neighboringMines = 0;
        updateImage();
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
        updateImage();
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
        updateImage();
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setNeighboringMines(int neighboringMines) {
        this.neighboringMines = neighboringMines;
        updateImage();
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }

    public void setMineClicked(boolean mineClicked) {
        this.mineClicked = mineClicked;
        updateImage();
    }

    public void setMineRevealed(boolean mineRevealed) {
        this.mineRevealed = mineRevealed;
        updateImage();
    }

    public void setFlaggedIncorrect(boolean flaggedIncorrect) {
        this.flaggedIncorrect = flaggedIncorrect;
        updateImage();
    }

    public void toggleFlag() {
        if (!isRevealed) {
            isFlagged = !isFlagged;
            updateImage();
        }
    }

    public void reveal() {
        if (!isRevealed && !isFlagged) {
            isRevealed = true;
            updateImage();
        }
    }

    private void updateImage() {
        if (isRevealed) {
            if (mineClicked) {
                imageView.setImageResource(R.drawable.clicked_mine);
            } else if (mineRevealed) {
                imageView.setImageResource(R.drawable.cell_mine);
            } else if (flaggedIncorrect) {
                imageView.setImageResource(R.drawable.incorrect);
            } else {
                int resourceId = getResources().getIdentifier("cell_" + neighboringMines, "drawable", getContext().getPackageName());
                imageView.setImageResource(resourceId);
            }
        } else if (isFlagged) {
            imageView.setImageResource(R.drawable.cell_flag);
        } else {
            imageView.setImageResource(R.drawable.cell_empty);
        }
    }
}
