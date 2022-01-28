package com.gerskom;

import static com.gerskom.Grid.*;

public class GridTaskMaker /*implements Runnable*/{
    private final Grid grid;
    private final int xp;
    private final int xk;
    private final int yp;
    private final int yk;
    private final int process;

    public GridTaskMaker(Grid grid, int xp, int xk, int yp, int yk) {
        this.grid = grid;
        this.xp = xp;
        this.xk = xk;
        this.yp = yp;
        this.yk = yk;
        this.process = 0;
    }
    public GridTaskMaker(Grid grid, int xp, int xk, int yp, int yk, int opt) {
        this.grid = grid;
        this.xp = xp;
        this.xk = xk;
        this.yp = yp;
        this.yk = yk;
        this.process = opt;
    }

    /*@Override
    public void run() {
        switch (process) {
            case 1 -> eqRelaxOperations();
            case 2 -> streamingBCOperation();
        }
    }*/

    public void eqRelaxOperations() {
        for(int x = xp; x <= xk; x++) {
            for (int y = yp; y <= yk; y++) {
                grid.dataTable[x][y][0] = grid.cellTable[x][y][0][0] + grid.cellTable[x][y][0][1] + grid.cellTable[x][y][0][2] +
                                          grid.cellTable[x][y][0][3] + grid.cellTable[x][y][0][4] + grid.cellTable[x][y][0][5] +
                                          grid.cellTable[x][y][0][6] + grid.cellTable[x][y][0][7] + grid.cellTable[x][y][0][8];

                grid.dataTable[x][y][1] = (grid.cellTable[x][y][0][2] + grid.cellTable[x][y][0][3] + grid.cellTable[x][y][0][4] -
                                           grid.cellTable[x][y][0][6] - grid.cellTable[x][y][0][7] - grid.cellTable[x][y][0][8]) /
                                           grid.dataTable[x][y][0];

                grid.dataTable[x][y][2] = -(grid.cellTable[x][y][0][4] + grid.cellTable[x][y][0][5] + grid.cellTable[x][y][0][6] -
                                           grid.cellTable[x][y][0][8] - grid.cellTable[x][y][0][1] - grid.cellTable[x][y][0][2]) /
                                           grid.dataTable[x][y][0];

                double vx = grid.dataTable[x][y][1];
                double vy = grid.dataTable[x][y][2];
                double vx2 = vx * vx;
                double vy2 = vy * vy;
                double c = 1d - 1.5d * (vx2 + vy2);
                double rho = grid.dataTable[x][y][0] / 36d;

                grid.cellTable[x][y][2][0] = 16.0d * rho * c;
                grid.cellTable[x][y][2][1] = 4.0d * rho * (c + 3.0d * vy + 4.5d * vy2);
                grid.cellTable[x][y][2][3] = 4.0d * rho * (c + 3.0d * vx + 4.5d * vx2);
                grid.cellTable[x][y][2][5] = 4.0d * rho * (c - 3.0d * vy + 4.5d * vy2);
                grid.cellTable[x][y][2][7] = 4.0d * rho * (c - 3.0d * vx + 4.5d * vx2);
                grid.cellTable[x][y][2][2] = rho * (c + 3.0d *  (vx + vy) + 4.5d *  (vx + vy) *  (vx + vy));
                grid.cellTable[x][y][2][4] = rho * (c + 3.0d *  (vx - vy) + 4.5d *  (vx - vy) *  (vx - vy));
                grid.cellTable[x][y][2][6] = rho * (c + 3.0d * (-vx - vy) + 4.5d *  (vx + vy) *  (vx + vy));
                grid.cellTable[x][y][2][8] = rho * (c + 3.0d * (-vx + vy) + 4.5d * (-vx + vy) * (-vx + vy));

                grid.cellTable[x][y][1][0] = grid.cellTable[x][y][2][0];
                grid.cellTable[x][y][1][1] = grid.cellTable[x][y][2][1];
                grid.cellTable[x][y][1][3] = grid.cellTable[x][y][2][3];
                grid.cellTable[x][y][1][5] = grid.cellTable[x][y][2][5];
                grid.cellTable[x][y][1][7] = grid.cellTable[x][y][2][7];
                grid.cellTable[x][y][1][2] = grid.cellTable[x][y][2][2];
                grid.cellTable[x][y][1][4] = grid.cellTable[x][y][2][4];
                grid.cellTable[x][y][1][6] = grid.cellTable[x][y][2][6];
                grid.cellTable[x][y][1][8] = grid.cellTable[x][y][2][8];
            }
        }
    }

    public void streamingBCOperation() {
        for(int x = xp; x <= xk; x++) {
            for (int y = yp; y <= yk; y++) {
                grid.cellTable[x][y][0][0] = grid.cellTable[x][y][1][0];

                if(x < grid.width - borderSize - 1) {
                    grid.cellTable[x][y][0][7] = grid.cellTable[x + 1][y][1][7];
                    grid.cellTable[x + 1][y][0][3] = grid.cellTable[x][y][1][3];
                }
                if(y < grid.height - borderSize - 1) {
                    grid.cellTable[x][y][0][5] = grid.cellTable[x][y + 1][1][5];
                    grid.cellTable[x][y + 1][0][1] = grid.cellTable[x][y][1][1];
                }
                if(x < grid.width - borderSize - 1 && y < grid.height - borderSize - 1) {
                    grid.cellTable[x][y][0][6] = grid.cellTable[x + 1][y + 1][1][6];
                    grid.cellTable[x + 1][y + 1][0][2] = grid.cellTable[x][y][1][2];
                }
                if(x > borderSize && y < grid.height - borderSize - 1) {
                    grid.cellTable[x][y][0][4] = grid.cellTable[x - 1][y + 1][1][4];
                    grid.cellTable[x - 1][y + 1][0][8] = grid.cellTable[x][y][1][8];
                }
                if(x == borderSize || x == grid.width - borderSize - 1 ||
                   y == borderSize || y == grid.height - borderSize - 1 || wallTable[x][y] == obstacle) {
                    grid.cellTable[x][y][0][1] = grid.cellTable[x][y][1][1];
                    grid.cellTable[x][y][0][3] = grid.cellTable[x][y][1][3];
                    grid.cellTable[x][y][0][5] = grid.cellTable[x][y][1][5];
                    grid.cellTable[x][y][0][7] = grid.cellTable[x][y][1][7];
                    grid.cellTable[x][y][0][2] = grid.cellTable[x][y][1][2];
                    grid.cellTable[x][y][0][4] = grid.cellTable[x][y][1][4];
                    grid.cellTable[x][y][0][6] = grid.cellTable[x][y][1][6];
                    grid.cellTable[x][y][0][8] = grid.cellTable[x][y][1][8];
                }
            }
        }
    }

}
