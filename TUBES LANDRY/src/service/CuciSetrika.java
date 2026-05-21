package service;

public class CuciSetrika extends Layanan {

    private String jenisProses;
    private double biayaTambahan;

    public CuciSetrika(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses,
            double biayaTambahan) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari);

        if (jenisProses == null || jenisProses.isEmpty()) {
            jenisProses = "Reguler";
        }

        if (biayaTambahan < 0) {
            biayaTambahan = 0;
        }

        this.jenisProses = jenisProses;
        this.biayaTambahan = biayaTambahan;
    }

    @Override
    public double hitungBiaya(double berat) {

        if (berat <= 0) {
            return 0;
        }

        return (berat * getHargaPerKg()) + biayaTambahan;
    }

    @Override
    public String deskripsiLayanan() {
        return "Cuci Setrika - " + jenisProses +
                " (" + getEstimasiHari() + " Hari)";
    }
}