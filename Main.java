package belajarjava;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    
        double harga = 0, diskon = 0;
    
        int pilihan, kg;

        String menu = null;

        System.out.println("=============SELAMAT DATANG DI PROGRAM LAUNDRY SEDERHANA=============\n");

        System.out.println(" 1. Cuci lengkap\n 2. Laundry kiloan\n 3. Cuci + setrika\n");

        System.out.println("========================================================\n");

        Scanner input = new Scanner(System.in);
        System.out.print("Pilihan \t:");

        pilihan = input.nextInt();

        System.out.print("Berapa kg \t:");

        kg = input.nextInt();

        if (pilihan == 1) {
            menu = "Cuci lengkap";
            harga = 100000;

            if (kg >= 10) {
                diskon = 10 * (harga * kg) / 100;
                System.out.println("Anda Mendapat Diskon 20% -Rp." + diskon);
            }
        } else if (pilihan == 2) {
            menu = "Laundry kiloan";
            harga = 150000;

            if (kg >= 10) {
                diskon = 10 * (harga * kg) / 100;
                System.out.println("Anda mendapat diskon 20% -Rp." + diskon);
            }
        } else if (pilihan == 3) {
            menu = "Cuci + Setrika";
            harga = 50000;

            if (kg >= 10) {
                diskon = 10 * (harga * kg) / 100;
                System.out.println("Anda mendapatkan diskon 20% -Rp." + diskon);
            }
        } else {
            System.out.println("Diskon 20% jika pencucian lebih dari 10 kg");
        }
        
        System.out.println("\n========================================================\n");
        System.out.println("Menu Yang Dipilih");
        System.out.println(menu + "\t: Rp. " + harga + "/Kg");
        harga = (harga * kg) - diskon;
        System.out.println("Total Harga \t: Rp. " + harga);
    }
}