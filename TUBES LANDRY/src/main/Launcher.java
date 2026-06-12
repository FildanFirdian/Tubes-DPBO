package main; // Sesuaikan dengan nama package Anda jika berbeda

public class Launcher {
    public static void main(String[] args) {
        // PERBAIKAN: Paksa JavaFX menggunakan Software Rendering (CPU)
        System.setProperty("prism.order", "sw");
        
        // Opsional: Untuk melihat log detail jika nanti masih ada kendala
        System.setProperty("prism.verbose", "true");

        // Jalankan aplikasi utama
        Main.main(args); 
    }
}