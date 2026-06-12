package sistem;

public class Nota implements CetakDokumen {

    private Transaksi transaksi;

    public Nota(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    @Override
    public void cetakDokumen() {

        System.out.println("===== NOTA LAUNDRY =====");
        System.out.println("Pelanggan : "
                + transaksi.getPelanggan().getNama());

        System.out.println("Total : "
                + transaksi.getTotalHarga());

        System.out.println("Kembalian : "
                + transaksi.getKembalian());
    }
}