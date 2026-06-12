package service;

public class SetrikaSaja extends Layanan {

    private double diskon;

    public SetrikaSaja(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses,
            double diskon) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);

        if (diskon < 0) {
            diskon = 0;
        }

        this.diskon = diskon;
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
        return "Setrika Saja - " + getJenisProses() +
                " | Diskon: " + (diskon * 100) + "%" +
                " (" + getEstimasiHari() + " Hari)";
    }
}