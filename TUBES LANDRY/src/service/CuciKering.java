package service;

public class CuciKering extends Layanan {

    public CuciKering(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);
    }

    @Override
    public double hitungBiaya(double berat) {

        if (berat <= 0) {
            throw new IllegalArgumentException("Berat laundry harus lebih dari 0 Kg");
        }

        return berat * getHargaPerKg();
    }

    @Override
    public String deskripsiLayanan() {
        return "Cuci Kering - " + getJenisProses() +
                " (" + getEstimasiHari() + " Hari)";
    }
}