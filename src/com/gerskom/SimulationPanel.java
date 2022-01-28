package com.gerskom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPanel extends JPanel {
    public final Grid grid;
    public double[][] tmpData;
    public boolean started = false;

    private static final int THREADS = 4;
    private static final int XTASKS = 4;
    private static final int YTASKS = 1;
    private final int deltaTime = 1;
    private final static int opt = 2;
    private float m = 10f;
    private double maxColor = 0;
    private final static float colorThreshold = 245f;
    public long firstTime;
    public long secondTime;
    private double maxDiff = 0;
    private double minDiff = Integer.MAX_VALUE;
    private final ArrayList<Double> diffs = new ArrayList<>();
    private final ArrayList<Double> fps = new ArrayList<>();
    public int i = 0;
    public Graphics2D g2D;

    public static final Color wallColor = new Color(50, 50, 50);
    public static final Color obstacleColor = new Color(77, 72, 72);

    public SimulationPanel (Grid grid) {
        super();
        this.grid = grid;

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseListener(new MainInputHandler(this));
        this.addMouseMotionListener(new MainInputHandler(this));
        this.addKeyListener(new MainInputHandler(this));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g.create();

        //g2D.translate(0.0, grid.height);
        //g2D.scale(1.0, -1.0);
        maxColor = 0;
        double sqrt2 = Math.sqrt(2);

        for(int x = 0; x < grid.width; x++){
            for(int y = 0; y < grid.height; y++){
                if(Grid.wallTable[x][y] != Grid.wall && Grid.wallTable[x][y] != Grid.obstacle) {
                    //double v = (Math.abs(grid.dataTable[x][y][1]) + Math.abs(grid.dataTable[x][y][2])) / 2;
                    double v = (grid.dataTable[x][y][1] + grid.dataTable[x][y][2]) / 2;

                    double color = (v * 255f * m);
                    //double color = (grid.dataTable[x][y][opt] * 255f * m);

                    if(color > maxColor)
                        maxColor = color;

                    if (color > 254.999d) color = 255;
                    else if (color < -254.999d) color = -255;
                    if (color > -0.001 && color < 0.001)
                        color = 0;

                    if(color >= 0)
                        g2D.setColor(new Color((int)color, 0, 0));

                    else if(color < 0)
                        g2D.setColor(new Color(0, 0, (int)-color));

                    //if(grid.dataTable[x][y][opt] >= 0)
                    //    g2D.setColor(new Color((int)color, 0, 0));
                    //else if(grid.dataTable[x][y][opt] < 0)
                    //    g2D.setColor(new Color(0, 0, (int)-color));

                    else
                        g2D.setColor(Color.PINK);
                }
                else if(Grid.wallTable[x][y] == Grid.obstacle)
                    g2D.setColor(obstacleColor);
                else
                    g2D.setColor(wallColor);

                g2D.fillRect(x, y, 1, 1);
            }
        }

        if(maxColor > colorThreshold) m -= 0.15f;
        else m += 0.15f;

        g2D.dispose();
    }

    public void startTheParallelGasSimulation(){
        started = true;
        Timer timer;

        //int taskSizeX = (int)Math.ceil((double)((grid.width - 1) - 1)/ XTASKS);
        //int taskSizeY = (int)Math.ceil((double)((grid.height - 1) - 1) / YTASKS);

        GridTaskMaker sim = new GridTaskMaker(grid, Grid.borderSize, grid.width - Grid.borderSize - 1, Grid.borderSize, grid.height - Grid.borderSize - 1);

        timer = new Timer(deltaTime, e -> {
            //try { customExport(); }
            //catch (IOException ex) { ex.printStackTrace(); }

            firstTime = System.nanoTime();

            i++;

            sim.eqRelaxOperations();
            sim.streamingBCOperation();

            /*ExecutorService executor = Executors.newFixedThreadPool(THREADS);
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(grid, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 1);
                    executor.execute(worker);
                }
            }
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(grid, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 2);
                    executor.execute(worker);
                }
            }

            executor.shutdown();*/

            //if((i % 25) == 0) repaint();
            repaint();

            secondTime = System.nanoTime();
        });
        timer.start();
    }

    private void customExport() throws IOException {
        if(i == 1 || i == 50 || i == 200 || i == 350 ||
           i == 575 || i == 900 || i == 2100 || i == 11000) {
            exportFrame();
        }
    }

    public void exportFrame() throws IOException {
        exportImage("LBM_FluidFlow_" + grid.width + "x" + grid.height);
        System.out.println("\nFRAME " + i + " EXPORTED");
    }

    public void exportImage(String fileName) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(grid.width, grid.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2D = bufferedImage.createGraphics();

        repaint();

        for(int x = 0; x < grid.width; x++){
            for(int y = 0; y < grid.height; y++){
                if(Grid.wallTable[x][y] != Grid.wall && Grid.wallTable[x][y] != Grid.obstacle) {
                    double color = (grid.dataTable[x][y][opt] * 255f * m);

                    if (color > 254.5d) color = 255;
                    else if (color < -254.5d) color = -255;
                    if (color > -0.002 && color < 0.002)
                        color = 0;

                    if(grid.dataTable[x][y][opt] >= 0)
                        g2D.setColor(new Color((int)color, 0, 0));

                    else if(grid.dataTable[x][y][opt] < 0)
                        g2D.setColor(new Color(0, 0, (int)-color));
                    else
                        g2D.setColor(Color.PINK);
                }
                else if(Grid.wallTable[x][y] == Grid.obstacle)
                    g2D.setColor(obstacleColor);
                else
                    g2D.setColor(wallColor);

                g2D.fillRect(x, y, 1, 1);
            }
        }
        g2D.dispose();

        String formatName = "bmp";
        File file;

        if (i != 0)
            file = new File("output/" + fileName + "_" + i + "." + formatName);
        else
            file = new File("output/test_gas." + formatName);

        ImageIO.write(bufferedImage, formatName, file);
    }

    public void fpsCounter() {
        double diff = secondTime - firstTime;
        double fps = (1 / (diff / 1000000000D));
        if(maxDiff <= diff) maxDiff = diff;
        else if(minDiff > diff) minDiff = diff;
        System.out.println("\niteration: " + i +
                //"\nbefore: " + firstTime + "\tafter: " + secondTime +
                "\nfixed interval: " + deltaTime + "ms" +
                "\ndiff: " + new DecimalFormat("##.###").format(diff / 1000000D) + "ms" +
                "\nFPS: " + new DecimalFormat("###.##").format(fps) +
                "\nmax diff: " + new DecimalFormat("##.###").format(maxDiff / 1000000D) + "ms" +
                "\nmin diff: " + new DecimalFormat("##.###").format(minDiff / 1000000D) + "ms");
        averageResults(diff, fps);
    }

    public void averageResults(double newDiff, double newFPS) {
        double diffAverage = 0;
        double fpsAverage = 0;
        diffs.add(newDiff);
        fps.add(newFPS);
        for(int i = 0; i < diffs.size(); i++) {
            diffAverage += diffs.get(i) / diffs.size();
            fpsAverage += fps.get(i) / fps.size();
        }
        System.out.println("average diff: " + new DecimalFormat("##.###").format(diffAverage / 1000000D)
                + "ms" + "\naverage FPS: " + new DecimalFormat("###.##").format(fpsAverage));
    }
}
