import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LaundryGUI extends JFrame {

    private JComboBox<String> menuComboBox;
    private JTextField kgField;
    private JCheckBox antarJemputCheckBox, parfumKhususCheckBox, pengemasanKhususCheckBox;
    private JTextArea outputArea;
    private JButton calculateButton, resetButton, reminderButton, exitButton, themeButton, exportButton;
    private boolean isDarkTheme = false;
    private List<String> transactionHistory;

    public LaundryGUI() {
        // Pengaturan frame
        setTitle("Program Laundry Sederhana");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Inisialisasi riwayat transaksi
        transactionHistory = new ArrayList<>();

        // Membuat komponen
        JLabel menuLabel = new JLabel("Pilih Layanan Laundry:");
        String[] layanan = {"Cuci Lengkap", "Laundry Kiloan", "Cuci + Setrika"};
        menuComboBox = new JComboBox<>(layanan);

        JLabel kgLabel = new JLabel("Berapa Kg:");
        kgField = new JTextField(10);

        JLabel ekstraLabel = new JLabel("Layanan Ekstra:");
        antarJemputCheckBox = new JCheckBox("Antar Jemput (+Rp. 20,000)");
        parfumKhususCheckBox = new JCheckBox("Parfum Khusus (+Rp. 15,000)");
        pengemasanKhususCheckBox = new JCheckBox("Pengemasan Khusus (+Rp. 10,000)");

        calculateButton = new JButton("Hitung Harga");
        resetButton = new JButton("Reset");
        reminderButton = new JButton("Set Pengingat");
        themeButton = new JButton("Ubah Tema");
        exitButton = new JButton("Keluar");
        exportButton = new JButton("Ekspor Laporan");

        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);

        // Menambahkan komponen ke frame
        add(menuLabel);
        add(menuComboBox);
        add(kgLabel);
        add(kgField);
        add(ekstraLabel);
        add(antarJemputCheckBox);
        add(parfumKhususCheckBox);
        add(pengemasanKhususCheckBox);
        add(calculateButton);
        add(resetButton);
        add(reminderButton);
        add(themeButton);
        add(exportButton);
        add(exitButton);
        add(new JScrollPane(outputArea));

        // Event handling
        calculateButton.addActionListener(e -> calculatePrice());
        resetButton.addActionListener(e -> resetFields());
        reminderButton.addActionListener(e -> setReminder());
        themeButton.addActionListener(e -> toggleTheme());
        exitButton.addActionListener(e -> exitApplication());
        exportButton.addActionListener(e -> exportReport());

        // Tema default
        applyTheme();
    }

    private void calculatePrice() {
        double harga = 0, diskon = 0, pajak = 0, totalEkstra = 0;
        int kg;

        String menu = (String) menuComboBox.getSelectedItem();
        try {
            kg = Integer.parseInt(kgField.getText());
            if (kg <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah kg harus lebih dari 0!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harap masukkan angka yang valid untuk kg!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (menu.equals("Cuci Lengkap")) {
            harga = 100000;
        } else if (menu.equals("Laundry Kiloan")) {
            harga = 150000;
        } else if (menu.equals("Cuci + Setrika")) {
            harga = 50000;
        }

        // Diskon
        if (kg >= 10) {
            diskon = 0.20 * (harga * kg);
            outputArea.append("Anda Mendapat Diskon 20% - Rp." + diskon + "\n");
        }

        // Biaya layanan ekstra
        if (antarJemputCheckBox.isSelected()) {
            totalEkstra += 20000;
        }
        if (parfumKhususCheckBox.isSelected()) {
            totalEkstra += 15000;
        }
        if (pengemasanKhususCheckBox.isSelected()) {
            totalEkstra += 10000;
        }

        // Hitung total
        double total = (harga * kg) - diskon + totalEkstra;
        pajak = total * 0.1; // Pajak 10%
        total += pajak; // Tambahkan pajak ke total

        // Estimasi waktu pengerjaan
        int waktuPengerjaan = (kg / 5) * (menu.equals("Cuci Lengkap") ? 1 : menu.equals("Laundry Kiloan") ? 2 : 3);
        waktuPengerjaan = Math.max(waktuPengerjaan, 1); // Minimal 1 jam

        // Menampilkan output
        String result = "Menu Yang Dipilih: " + menu + "\n" +
                "Harga per Kg: Rp. " + harga + "\n" +
                "Diskon: Rp. " + diskon + "\n" +
                "Layanan Ekstra: Rp. " + totalEkstra + "\n" +
                "Pajak: Rp. " + pajak + "\n" +
                "Total Harga: Rp. " + total + "\n" +
                "Estimasi Waktu Pengerjaan: " + waktuPengerjaan + " jam\n";
        outputArea.setText(result);

        // Menyimpan riwayat transaksi
        saveTransaction(menu, kg, total);
    }

    private void resetFields() {
        kgField.setText("");
        outputArea.setText("");
        antarJemputCheckBox.setSelected(false);
        parfumKhususCheckBox.setSelected(false);
        pengemasanKhususCheckBox.setSelected(false);
    }

    private void saveTransaction(String menu, int kg, double total) {
        String transaction = "Menu: " + menu + ", Kg: " + kg + ", Total: Rp. " + total + "\n";
        transactionHistory.add(transaction);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("riwayat_laundry.txt", true))) {
            writer.write(transaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setReminder() {
        String reminder = JOptionPane.showInputDialog(this, "Masukkan pengingat (misalnya: 'Cuci baju'): ");
        if (reminder != null && !reminder.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pengingat telah disetel: " + reminder, "Pengingat", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        applyTheme();
    }

    private void applyTheme() {
        Color backgroundColor = isDarkTheme ? Color.DARK_GRAY : Color.WHITE;
        Color textColor = isDarkTheme ? Color.WHITE : Color.BLACK;

        getContentPane().setBackground(backgroundColor);

        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JLabel || component instanceof JButton || component instanceof JTextArea) {
                component.setBackground(backgroundColor);
                component.setForeground(textColor);
            }
        }

        outputArea.setCaretColor(textColor);
        kgField.setCaretColor(textColor);
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void exportReport() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("laporan_laundry.txt"))) {
            for (String transaction : transactionHistory) {
                writer.write(transaction);
            }
            JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor ke laporan_laundry.txt", "Ekspor Laporan", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengekspor laporan!", "Ekspor Gagal", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaundryGUI gui = new LaundryGUI();
            gui.setVisible(true);
        });
    }
}