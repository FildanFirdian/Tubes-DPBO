package service;

public class CuciKering extends Layanan {

    private String jenisProses;

    public CuciKering(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari);

        if (jenisProses == null || jenisProses.isEmpty()) {
            jenisProses = "Reguler";
        }

        this.jenisProses = jenisProses;
    }

    @Override
    public double hitungBiaya(double berat) {

        if (berat <= 0) {
            return 0;
        }

        return berat * getHargaPerKg();
    }

    @Override
    public String deskripsiLayanan() {
        return "Cuci Kering - " + jenisProses +
                " (" + getEstimasiHari() + " Hari)";
    }
}