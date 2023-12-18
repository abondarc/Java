package com.example.demo5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class TVProgram implements Serializable {
    String channel;
    String show;
    String time;
    String date;
    int popularity;
    String category;

    public TVProgram(String channel, String show, String time, String date, int popularity, String category) {
        this.channel = channel;
        this.show = show;
        this.time = time;
        this.date = date;
        this.popularity = popularity;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Channel: " + channel + ", Show: " + show + ", Time: " + time + ", Date: " + date + ", Popularity: " + popularity + ", Category: " + category;
    }
}

public class TVProgramManagerGUI {
    private static ArrayList<TVProgram> tvPrograms = new ArrayList<>();
    private static JTable programTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("TV Program Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        programTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(programTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton showButton = createButton("Show Programs");
        JButton addButton = createButton("Add Program");
        JButton editButton = createButton("Edit Program");
        JButton deleteButton = createButton("Delete Program");
        JButton saveButton = createButton("Save Programs");
        JButton loadButton = createButton("Load Programs");

        buttonPanel.add(showButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Обработчики событий
        showButton.addActionListener(e -> showPrograms());
        addButton.addActionListener(e -> addProgram());
        editButton.addActionListener(e -> editProgram());
        deleteButton.addActionListener(e -> deleteProgram());
        saveButton.addActionListener(e -> savePrograms());
        loadButton.addActionListener(e -> loadPrograms());
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 30));
        return button;
    }

    private static void showPrograms() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Channel");
        tableModel.addColumn("Show");
        tableModel.addColumn("Time");
        tableModel.addColumn("Date");
        tableModel.addColumn("Popularity");
        tableModel.addColumn("Category");

        sortProgramsByPopularity();

        for (TVProgram program : tvPrograms) {
            Object[] rowData = {program.channel, program.show, program.time, program.date, program.popularity, program.category};
            tableModel.addRow(rowData);
        }

        programTable.setModel(tableModel);
    }

    private static void sortProgramsByPopularity() {
        Collections.sort(tvPrograms, (p1, p2) -> Integer.compare(p2.popularity, p1.popularity));
    }

    private static void addProgram() {
        JFrame addFrame = new JFrame("Add Program");
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setSize(300, 200);
        addFrame.setLocationRelativeTo(null);

        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(7, 2));
        addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField channelField = new JTextField();
        JTextField showField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField popularityField = new JTextField();
        JTextField categoryField = new JTextField();

        addPanel.add(new JLabel("Channel:"));
        addPanel.add(channelField);
        addPanel.add(new JLabel("Show:"));
        addPanel.add(showField);
        addPanel.add(new JLabel("Time:"));
        addPanel.add(timeField);
        addPanel.add(new JLabel("Date:"));
        addPanel.add(dateField);
        addPanel.add(new JLabel("Popularity:"));
        addPanel.add(popularityField);
        addPanel.add(new JLabel("Category:"));
        addPanel.add(categoryField);

        JButton addButton = createButton("Add");
        addPanel.add(addButton);

        addButton.addActionListener(e -> {
            String channel = channelField.getText();
            String show = showField.getText();
            String time = timeField.getText();
            String date = dateField.getText();
            int popularity = Integer.parseInt(popularityField.getText());
            String category = categoryField.getText();

            TVProgram program = new TVProgram(channel, show, time, date, popularity, category);
            tvPrograms.add(program);

            addFrame.dispose();
            showPrograms();
        });

        addFrame.getContentPane().add(addPanel);
        addFrame.setVisible(true);
    }

    private static void editProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a program to edit.");
            return;
        }

        TVProgram selectedProgram = tvPrograms.get(selectedRow);

        JFrame editFrame = new JFrame("Edit Program");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(300, 200);
        editFrame.setLocationRelativeTo(null);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(7, 2));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField channelField = new JTextField(selectedProgram.channel);
        JTextField showField = new JTextField(selectedProgram.show);
        JTextField timeField = new JTextField(selectedProgram.time);
        JTextField dateField = new JTextField(selectedProgram.date);
        JTextField popularityField = new JTextField(Integer.toString(selectedProgram.popularity));
        JTextField categoryField = new JTextField(selectedProgram.category);

        editPanel.add(new JLabel("Channel:"));
        editPanel.add(channelField);
        editPanel.add(new JLabel("Show:"));
        editPanel.add(showField);
        editPanel.add(new JLabel("Time:"));
        editPanel.add(timeField);
        editPanel.add(new JLabel("Date:"));
        editPanel.add(dateField);
        editPanel.add(new JLabel("Popularity:"));
        editPanel.add(popularityField);
        editPanel.add(new JLabel("Category:"));
        editPanel.add(categoryField);

        JButton editButton = createButton("Edit");
        editPanel.add(editButton);

        editButton.addActionListener(e -> {
            tvPrograms.remove(selectedRow);
            String channel = channelField.getText();
            String show = showField.getText();
            String time = timeField.getText();
            String date = dateField.getText();
            int popularity = Integer.parseInt(popularityField.getText());
            String category = categoryField.getText();

            TVProgram updatedProgram = new TVProgram(channel, show, time, date, popularity, category);
            tvPrograms.add(selectedRow, updatedProgram);

            editFrame.dispose();
            showPrograms();
        });

        editFrame.getContentPane().add(editPanel);
        editFrame.setVisible(true);
    }

    private static void deleteProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a program to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this program?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tvPrograms.remove(selectedRow);
            showPrograms();
        }
    }

    private static void savePrograms() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tvPrograms.dat"))) {
            oos.writeObject(tvPrograms);
            JOptionPane.showMessageDialog(null, "Programs saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving programs.");
        }
    }

    private static void loadPrograms() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tvPrograms.dat"))) {
            tvPrograms = (ArrayList<TVProgram>) ois.readObject();
            JOptionPane.showMessageDialog(null, "Programs loaded successfully.");
            showPrograms();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading programs.");
        }
    }
}
