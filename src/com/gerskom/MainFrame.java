package com.gerskom;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    Grid map;

    JPanel northPanel = new JPanel();
    JPanel westPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel eastPanel = new JPanel();

    SimulationPanel centerPanel;

    MainFrame(Grid map) {
        //ImageIcon icon = new ImageIcon("icon.png");

        this.map = map;
        centerPanel = new SimulationPanel(map);

        int hGap = 10;
        int vGap = 10;

        int width = map.width + 6 * hGap - 3;
        int height = map.height + 8 * vGap;
        this.setSize(width, height);
        this.setTitle("Lattice Gas Simulation");
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        //this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(hGap, vGap));
        this.setVisible(true);

        northPanel.setBackground(Color.DARK_GRAY);
        westPanel.setBackground(Color.DARK_GRAY);
        southPanel.setBackground(Color.DARK_GRAY);
        eastPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setBackground(new Color(58,58,58));

        centerPanel.setLayout(new BorderLayout());

        northPanel.setPreferredSize(new Dimension(0, vGap));
        westPanel.setPreferredSize(new Dimension(hGap,0));
        eastPanel.setPreferredSize(new Dimension(hGap,0));
        southPanel.setPreferredSize(new Dimension(0, vGap));

        this.add(northPanel, BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(centerPanel, BorderLayout.CENTER);

        centerPanel.startTheParallelGasSimulation();
        //centerPanel.startTheGasSimulation();
    }
}
