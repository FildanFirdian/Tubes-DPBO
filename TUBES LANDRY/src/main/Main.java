package main;


import actor.Pelanggan;
import service.Layanan;
import sistem.SistemLaundry;
import sistem.Transaksi;
import sistem.Nota;
import sistem.DetailTransaksi;
import sistem.StatusLaundry;
import service.CuciKering;
import service.CuciSetrika;

import java.util.Date;


public class Main {

    public static void main(String[] args) {

        // Membuat Sistem Laundry
        SistemLaundry sistem = new SistemLaundry();


        // Tambah Pelanggan


        Pelanggan p1 = new Pelanggan(1,"Nay","08123456789","Tangerang",1,"nay","12345");

        sistem.tambahPelanggan(p1);

        System.out.println("=== DATA PELANGGAN ===");
        sistem.tampilSemuaPelanggan();

    
        // Tambah Layanan
  

        Layanan cuciKering = new CuciKering(001,"Cuci Kering",5000, 5, "Offline");
        Layanan cuciSetrika = new CuciSetrika(002,"Cuci Setrika",7000, 3, "Offline", 2.000);

        sistem.tambahLayanan(cuciKering);
        sistem.tambahLayanan(cuciSetrika);


        // Buat Transaksi


        Transaksi transaksi = new Transaksi(1001,new Date(),p1);

        DetailTransaksi detail1 = new DetailTransaksi(1,3.5,cuciSetrika);

        transaksi.tambahDetail(detail1);


        // Hitung Total


        double total = transaksi.hitungTotalHarga();

        System.out.println("\n=== TOTAL TRANSAKSI ===");
        System.out.println("Total : Rp " + total);


        // Pembayaran


        transaksi.prosesPembayaran(50000);

        System.out.println("Bayar : Rp 50000");
        System.out.println("Kembalian : Rp " + transaksi.getKembalian());

  
        // Status Laundry


        StatusLaundry diproses =
            new StatusLaundry(1, "Diproses");

        transaksi.ubahStatus(diproses);


        // Simpan Transaksi


        sistem.tambahTransaksi(transaksi);

        // Cetak Nota
  

        Nota nota = new Nota(transaksi);

        System.out.println();
        nota.cetakDokumen();
    }
}
