package sistem;
import service.Layanan;

public class DetailTransaksi {
    private int idDetail;
    private double berat;
    private double subtotal;
    private Layanan layanan;

    public DetailTransaksi(int idDetail, double berat, double subtotal, Layanan layanan){
        this.idDetail = idDetail;
        this.berat = berat;
        this.subtotal = subtotal;
        this.layanan = layanan;
    }

    public double hitungSubTotal(){
        if (this.layanan != null){
            return this.berat * this.layanan.getHargaPerKg();
        }
        return  0.0;
    }

    public double getSubTotal(){
        return this.subtotal;
    }

    public Layanan getLayanan(){
        return this.layanan;
    }

    public double getBerat(){
        return this.berat;
    }
}
