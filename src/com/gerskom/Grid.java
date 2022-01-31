package com.gerskom;

import java.util.Arrays;
import java.util.Random;

public class Grid {
    public int width;
    public int height;
    public static final int borderSize = 3;
    public static int[][] wallTable;
    public final static int wall = -1;
    public final static int obstacle = -2;
    public double[][][][] cellTable;
    public double[][][] dataTable;
    public static final double[][] ck = new double[2][9];
    public static final double[] w = new double[9];

    public static final int dT = 1;
    public static final float tau = 1f;

    private static final double c = 1d;
    private static final double epsilon = 0.00000000d;
    private static final double initRo = 1d;
    private static final double initUx = 0.0d;
    private static final double initUy = 0.0d;
    private static final double borderUxMax = 0.15d;
    private static final double borderUyMax = 0.0d;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cellTable = new double[width][height][3][9];
        this.dataTable = new double[width][height][3];
        initCk();
        initW();
        initCellsOfSimArea();
        wallTable = new int[width][height];
        initComputeArea();
    }

    public void initCell(int xCor, int yCor, double ro, double ux, double uy) {
        if(wallTable[xCor][yCor] != wall) {
            dataTable[xCor][yCor][0] = ro;
            dataTable[xCor][yCor][1] = ux;
            dataTable[xCor][yCor][2] = uy;
        }
    }

    public void initWallCell(int x, int y) {
        if(x == borderSize/* && y > borderSize + 15 && y < height - borderSize - 15*/) {
            dataTable[x][y][0] = initRo;
            //dataTable[x][y][1] = borderUxMax / height * y;
            dataTable[x][y][1] = borderUxMax;
            dataTable[x][y][2] = borderUyMax;
        }
        else if(x == width - borderSize - 1/* && y > borderSize + 15 && y < height - borderSize - 15*/) {
            dataTable[x][y][0] = initRo;
            //dataTable[x][y][1] = borderUxMax / height * y;
            dataTable[x][y][1] = borderUxMax;
            dataTable[x][y][2] = borderUyMax;
        }
        else if(y == borderSize) {
            dataTable[x][y][0] = initRo;
            dataTable[x][y][1] = borderUxMax;
            dataTable[x][y][2] = borderUyMax;
        }
        else if(y == height - borderSize - 1) {
            dataTable[x][y][0] = initRo;
            dataTable[x][y][1] = initUx;
            dataTable[x][y][2] = borderUyMax;
        }
        else {
            dataTable[x][y][0] = initRo;
            dataTable[x][y][1] = 0;
            dataTable[x][y][2] = 0;
        }
        wallTable[x][y] = wall;
    }

    public void removeWallCell(int xCor, int yCor) {
        wallTable[xCor][yCor] = 0;
    }

    public void addWallSquare(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                removeParticle(x, y);
                initWallCell(x, y);
            }
    }

    public void addObstacleSquare(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                removeParticle(x, y);
                wallTable[x][y] = obstacle;
            }
    }

    public void removeWallSquare(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                removeParticle(x, y);
                removeWallCell(x, y);
            }
    }

    public void addSquareOfParticles(int xCor, int yCor, int size, double ro, double ux) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                if(wallTable[x][y] != wall)
                    initCell(x, y, ro, ux, 0);
            }
    }

    public void addSquareOfParticles(int xCor, int yCor, int size, double ro, double ux, double uy) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                if(wallTable[x][y] != wall)
                    initCell(x, y, ro, ux, uy);
            }
    }

    public void addStripsOfFlow(int xCor, int yCor, int size, double ux) {
        for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
            initCell(xCor - size, y, initRo, ux, 0);
            initCell(xCor + size, y, initRo, ux, 0);
        }
    }

    /*public void addBrushOfParticles(int xCor, int yCor, float size, float density) {
        for (double r = 4; r <= size / 2; r += 1.5) {
            for (double a = 0; a < 2 * Math.PI; a += 0.05) {
                Random random = new Random();
                float rand = 100 * random.nextFloat();

                int x = (int)(r * Math.cos(a));
                int y = (int)(r * Math.sin(a));

                if (density >= rand)
                    if ((x + xCor) >= 0 && (x + xCor) <= width && (y + yCor) >= 0 && (y + yCor) <= height)
                        initCell(x + xCor, y + yCor);
            }
        }
    }*/

    private void removeParticle(int xCor, int yCor) {
        if(xCor > 0 && xCor < width && yCor > 0 && yCor < height) {
            if (wallTable[xCor][yCor] != wall) {
                dataTable[xCor][yCor][0] = initRo;
                dataTable[xCor][yCor][1] = 0;
                dataTable[xCor][yCor][2] = 0;
            }
        }
    }

    public void removeSquareOfParticles(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++)
                removeParticle(x, y);
    }

    /*public void initRandomCells() {
        for(int c = 0; c < nMax; c++) {
            Random ran = new Random();
            int x = ran.nextInt(width - 10) + 5;
            int y = ran.nextInt(height - 10) + 5;
            initCell(x, y);
        }
    }*/

    private void initW() {
        w[0] = 4d / 9d + epsilon;
        w[1] = 1d / 9d;
        w[2] = 1d / 36d;
        w[3] = 1d / 9d;
        w[4] = 1d / 36d;
        w[5] = 1d / 9d;
        w[6] = 1d / 36d;
        w[7] = 1d / 9d;
        w[8] = 1d / 36d;
        //System.out.println(w[0] + w[1] + w[2] + w[3] + w[4] + w[5] + w[6] + w[7] + w[8]);
    }

    private void initCk() {
        ck[0][0] =  0d;             ck[1][0] = 0d;
        ck[0][1] =  0d;             ck[1][1] = -c;
        ck[0][2] =  c - epsilon;    ck[1][2] = -c + epsilon;
        ck[0][3] =  c;              ck[1][3] = 0d;
        ck[0][4] =  c - epsilon;    ck[1][4] = c - epsilon;
        ck[0][5] =  0d;             ck[1][5] = c;
        ck[0][6] = -c + epsilon;    ck[1][6] = c - epsilon;
        ck[0][7] = -c;              ck[1][7] = 0d;
        ck[0][8] = -c + epsilon;    ck[1][8] = -c + epsilon;
    }

    private void initBoundaries(int x, int y) {
        if (x <= borderSize || x >= width - borderSize - 1 || y <= borderSize || y >= height - borderSize - 1) {
            dataTable[x][y][1] = (1d * y / (height - borderSize)) * borderUxMax;
        }
        //if (x <= borderSize || y <= borderSize || y >= height - borderSize - 1) {
        //    dataTable[x][y][1] = borderUxMax;
        //}
        //if (x <= borderSize && y > borderSize + 50 && y < height - borderSize - 50) {
        //    dataTable[x][y][1] = borderUxMax;
        //}
        else dataTable[x][y][1] = 0d;

        dataTable[x][y][2] = borderUyMax;
        dataTable[x][y][0] = initRo;
    }

    private void initComputeArea() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                initBoundaries(x, y);

                double vx = dataTable[x][y][1];
                double vy = dataTable[x][y][2];
                double vx2 = vx * vx;
                double vy2 = vy * vy;
                double c = 1d - 1.5d * (vx2 + vy2);
                double rho = dataTable[x][y][0] / 36.0d;

                cellTable[x][y][2][0] = 16.0d * rho * c;
                cellTable[x][y][2][1] = 4.0d * rho * (c + 3.0d * vy + 4.5d * vy2);
                cellTable[x][y][2][3] = 4.0d * rho * (c + 3.0d * vx + 4.5d * vx2);
                cellTable[x][y][2][5] = 4.0d * rho * (c - 3.0d * vy + 4.5d * vy2);
                cellTable[x][y][2][7] = 4.0d * rho * (c - 3.0d * vx + 4.5d * vx2);
                cellTable[x][y][2][2] = rho * (c + 3.0d *  (vx + vy) + 4.5d *  (vx + vy) *  (vx + vy));
                cellTable[x][y][2][4] = rho * (c + 3.0d *  (vx - vy) + 4.5d *  (vx - vy) *  (vx - vy));
                cellTable[x][y][2][6] = rho * (c + 3.0d * (-vx - vy) + 4.5d *  (vx + vy) *  (vx + vy));
                cellTable[x][y][2][8] = rho * (c + 3.0d * (-vx + vy) + 4.5d * (-vx + vy) * (-vx + vy));

                cellTable[x][y][0][0] = cellTable[x][y][2][0];
                cellTable[x][y][0][1] = cellTable[x][y][2][1];
                cellTable[x][y][0][3] = cellTable[x][y][2][3];
                cellTable[x][y][0][5] = cellTable[x][y][2][5];
                cellTable[x][y][0][7] = cellTable[x][y][2][7];
                cellTable[x][y][0][2] = cellTable[x][y][2][2];
                cellTable[x][y][0][4] = cellTable[x][y][2][4];
                cellTable[x][y][0][6] = cellTable[x][y][2][6];
                cellTable[x][y][0][8] = cellTable[x][y][2][8];
            }
    }

    private void initCellsOfSimArea() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                for(int i = 0; i < 3; i++)
                    for (int j = 0; j < 9; j++)
                        cellTable[x][y][i][j] = 0;
            }

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                dataTable[x][y][0] = initRo;
                dataTable[x][y][1] = initUx;
                dataTable[x][y][2] = initUy;
            }

    }
}