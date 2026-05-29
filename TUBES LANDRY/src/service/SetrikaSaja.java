
package service;

public class SetrikaSaja extends Layanan {

    private String jenisProses;
    private double diskon;

    public SetrikaSaja(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses,
            double diskon) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari);

        if (jenisProses == null || jenisProses.isEmpty()) {
            jenisProses = "Reguler";
        }

        if (diskon < 0) {
            diskon = 0;
        }

        this.jenisProses = jenisProses;
        this.diskon = diskon;
    }

    public String getJenisProses() {
        return jenisProses;
    }

    public void setJenisProses(String jenisProses) {
        this.jenisProses = jenisProses;
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        if (diskon < 0) {
            diskon = 0;
        }
        this.diskon = diskon;
    }

    @Override
    public double hitungBiaya(double berat) {

        if (berat <= 0) {
            return 0;
        }

        double subtotal = berat * getHargaPerKg();
        double potongan = subtotal * diskon;

        return subtotal - potongan;
    }

    @Override
    public String deskripsiLayanan() {
        return "Setrika Saja - " + jenisProses +
                " | Diskon: " + (diskon * 100) + "%" +
                " (" + getEstimasiHari() + " Hari)";
    }
}
    
    

    
    
    
    

