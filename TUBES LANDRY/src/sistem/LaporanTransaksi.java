package sistem;

import java.util.Collection;

public class LaporanTransaksi implements CetakDokumen{
    
    private String periode;
    private Collection<Transaksi> daftarTransaksi;

    public LaporanTransaksi(String periode, Collection<Transaksi> dafTransaksi) {
      this.periode = periode;
      this.daftarTransaksi = daftarTransaksi;
    }

    public double hitungTotalPendapatan() {
      double total = 0;

      for(Transaksi transaksi : daftarTransaksi){
        total += transaksi.getTotalHarga();
      }
      return total;
    }

    @Override
    public void cetakDokumen() {
    System.out.println("====== LAPORAN TRANSAKSI ======");
    System.out.println("Periode: " + periode);
    System.out.println("Total Pendapatan: Rp " + hitungTotalPendapatan());
    }
}
