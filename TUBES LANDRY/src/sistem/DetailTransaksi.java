package sistem;
import service.Layanan;

public class DetailTransaksi {

    private int idDetail;
    private double berat;
    private double subtotal;
    private Layanan layanan;

    public DetailTransaksi(int idDetail, double berat, Layanan layanan) {
        this.idDetail = idDetail;
        this.berat = berat;
        this.layanan = layanan;

        subtotal = hitungSubtotal();
    }

    public double hitungSubtotal() {
        return layanan.hitungBiaya(berat);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public double getBerat() {
        return berat;
    }
}