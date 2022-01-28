package com.gerskom;

import java.awt.event.*;
import java.io.IOException;

public class MainInputHandler implements KeyListener, MouseListener, MouseMotionListener {
    SimulationPanel simulationPanel;
    private static int mouseButton = -1;
    private static int opt = 1;

    private final float brushSize = 50f;
    private final float brushDensity = 25f;

    public MainInputHandler(SimulationPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'r' -> {
                //simulationPanel.grid.initRandomCells();
                simulationPanel.repaint();
            }
            case 'f' -> {
                try {
                    simulationPanel.exportFrame();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            case 'p' -> {
                //System.out.println("Number of particles: " + simulationPanel.countParticles());
                //System.out.println("Total concentration: " + simulationPanel.countTotalC());
            }
            case 'o' -> {
                simulationPanel.fpsCounter();
            }
            case't' -> {
                if(opt == 1) opt = 0;
                else opt = 1;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = simulationPanel.getMousePosition().x;
        int y = simulationPanel.getMousePosition().y;

        switch (e.getButton()) {
            case 1 -> {
                mouseButton = 1;
                if(opt == 1)
                    simulationPanel.grid.addObstacleSquare(x, y, 12);
                else
                    simulationPanel.grid.removeWallSquare(x, y, 10);
            }
            case 2 -> {
                mouseButton = 2;
                //simulationPanel.grid.removeSquareOfParticles(x, y, 100);
                simulationPanel.grid.initCell(x, y, 1, 0.7f, 0);
            }
            case 3 -> {
                mouseButton = 3;
                if(opt == 1)
                    simulationPanel.grid.addSquareOfParticles(x, y, 200, 1.0, 0.01d, 0.02d);
                else
                    simulationPanel.grid.addSquareOfParticles(x, y, 50, .1f, 0f);
            }
        }
        simulationPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = simulationPanel.getMousePosition().x;
        int y = simulationPanel.getMousePosition().y;

        switch (mouseButton) {
            case 1 -> {
                if(opt == 1)
                    simulationPanel.grid.addObstacleSquare(x, y, 12);
                else
                    simulationPanel.grid.removeWallSquare(x, y, 10);
            }
            case 2 -> simulationPanel.grid.removeSquareOfParticles(x, y, 100);
            case 3 ->  {
                if(opt == 1)
                    simulationPanel.grid.addSquareOfParticles(x, y, 200, 1f, 0.05f, 0);
                //else
                //    simulationPanel.grid.addBrushOfParticles(x, y, brushSize, brushDensity);
            }

        }
        simulationPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
