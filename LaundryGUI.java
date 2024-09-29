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
    private JTextArea outputArea;
    private JButton calculateButton;
    private JButton resetButton;
    private JButton reminderButton;
    private List<String> transactionHistory;

    public LaundryGUI() {
        // Pengaturan frame
        setTitle("Program Laundry Sederhana");
        setSize(400, 500);
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

        calculateButton = new JButton("Hitung Harga");
        resetButton = new JButton("Reset");
        reminderButton = new JButton("Set Pengingat");

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        // Menambahkan komponen ke frame
        add(menuLabel);
        add(menuComboBox);
        add(kgLabel);
        add(kgField);
        add(calculateButton);
        add(resetButton);
        add(reminderButton);
        add(new JScrollPane(outputArea));

        // Event handling
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePrice();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });

        reminderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setReminder();
            }
        });
    }

    private void calculatePrice() {
        double harga = 0, diskon = 0, pajak = 0;
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

        // Hitung total
        double total = (harga * kg) - diskon;
        pajak = total * 0.1; // Pajak 10%
        total += pajak; // Tambahkan pajak ke total

        // Menampilkan output
        String result = "Menu Yang Dipilih: " + menu + "\n" +
                        "Harga per Kg: Rp. " + harga + "\n" +
                        "Total Harga: Rp. " + total + "\n" +
                        "Pajak: Rp. " + pajak + "\n";
        outputArea.setText(result);
        
        // Menyimpan riwayat transaksi
        saveTransaction(menu, kg, total);
    }

    private void resetFields() {
        kgField.setText("");
        outputArea.setText("");
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
        String reminder = JOptionPane.showInputDialog(this, "Masukkan pengingat (misalnya: 'Cuci baju'):");
        if (reminder != null && !reminder.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pengingat telah disetel: " + reminder, "Pengingat", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaundryGUI gui = new LaundryGUI();
            gui.setVisible(true);
        });
    }
}
