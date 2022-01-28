package com.gerskom;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitialFrame extends JFrame {
    public int width;
    public int height;

    JPanel northPanel = new JPanel();
    JPanel westPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    JTextField gameWidth = new JTextField("400");
    JTextField gameHeight = new JTextField("300");
    JButton startButton = new JButton("PLAY");

    InitialFrame() {
        int frameWidth = 600;
        int frameHeight = 400;
        this.setSize(frameWidth, frameHeight);
        this.setTitle("Forest Fire Simulation");
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(10,10));
        this.setVisible(true);

        northPanel.setBackground(Color.DARK_GRAY);
        westPanel.setBackground(Color.DARK_GRAY);
        southPanel.setBackground(Color.DARK_GRAY);
        eastPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setBackground(new Color(58,58,58));

        centerPanel.setLayout(new BorderLayout());
        JPanel centerCenter = new JPanel();
        centerCenter.setOpaque(false);

        startButton.addActionListener( e -> {
            width = Integer.parseInt(gameWidth.getText());
            height = Integer.parseInt(gameHeight.getText());
            //nMax = Integer.parseInt(gameNMax.getText());

            new MainFrame(new Grid(width, height));
            this.setVisible(false);
            this.dispose();
        });

        gameWidth.setPreferredSize(new Dimension(100, 40));
        gameHeight.setPreferredSize(new Dimension(100, 40));
        //gameNMax.setPreferredSize(new Dimension(100, 40));
        startButton.setPreferredSize(new Dimension(75, 40));

        centerCenter.add(gameWidth);
        centerCenter.add(gameHeight);
        //centerCenter.add(gameNMax);
        centerCenter.add(startButton);

        northPanel.setPreferredSize(new Dimension(100,10));
        westPanel.setPreferredSize(new Dimension(10,100));
        eastPanel.setPreferredSize(new Dimension(10,100));
        southPanel.setPreferredSize(new Dimension(100,10));
        centerPanel.setPreferredSize(new Dimension(100,100));

        centerPanel.add(centerCenter, BorderLayout.CENTER);

        this.add(northPanel, BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }
}
