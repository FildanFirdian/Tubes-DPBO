package sistem;

import java.util.Collection;

public class LaporanTransaksi implements CetakDokumen {

    private Collection<Transaksi> daftarTransaksi;

    public LaporanTransaksi(Collection<Transaksi> daftarTransaksi) {
        this.daftarTransaksi = daftarTransaksi;
    }

    @Override
    public void cetakDokumen() {
        System.out.println("===== LAPORAN TRANSAKSI =====");
        for (Transaksi t : daftarTransaksi) {
            System.out.println("ID : " + t.getIdTransaksi() + " | Total : " + t.getTotalHarga());
        }
    }

    // GANTI / TAMBAHKAN METHOD INI AGAR EROR DI MAIN.JAVA HILANG
    public String getFormattedLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== LAPORAN TRANSAKSI =====\n");
        for (Transaksi t : daftarTransaksi) {
            sb.append("ID : ").append(t.getIdTransaksi())
              .append(" | Total : Rp").append(t.getTotalHarga()).append("\n");
        }
        return sb.toString();
    }
}