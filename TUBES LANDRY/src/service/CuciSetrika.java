package service;

public class CuciSetrika extends Layanan {

    private double biayaTambahan;

    public CuciSetrika(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses,
            double biayaTambahan) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);

        if (biayaTambahan < 0) {
            biayaTambahan = 0;
        }

        this.biayaTambahan = biayaTambahan;
    }

    public double getBiayaTambahan() {
        return biayaTambahan;
    }

    public void setBiayaTambahan(double biayaTambahan) {
        if (biayaTambahan < 0) {
            biayaTambahan = 0;
        }

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
        return "Cuci Setrika - " + getJenisProses() +
                " (" + getEstimasiHari() + " Hari)";
    }
}