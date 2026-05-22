package sistem;

import java.util.Date;
public class Nota implements CetakDokumen{
    private String noNota;
    private Date tanggalCetak;
    private Transaksi transaksi;

    public Nota(String noNota, Date tanggalCetak, Transaksi transaksi) {
      this.noNota = noNota;
      this.tanggalCetak = tanggalCetak;
      this.transaksi = transaksi;
    }

    public void tampilNota() {
      System.out.println("===== NOTA =====");
      System.out.println("No Nota               : " + noNota);
      System.out.println("Tanggal Cetak         : " + tanggalCetak);
      System.out.println("Transaksi             : " + transaksi);
      
    }  

    @Override
    public void cetakDokumen() {
    System.out.println("Nota Sedang dicetak...");
    }

}
