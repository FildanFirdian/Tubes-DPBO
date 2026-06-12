package service;

public abstract class Layanan {

    private int idLayanan;
    private String namaLayanan;
    private double hargaPerKg;
    private int estimasiHari;
    private String jenisProses;

    public Layanan(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses) {

        if (idLayanan <= 0) {
            idLayanan = 1;
        }

        if (namaLayanan == null || namaLayanan.isEmpty()) {
            namaLayanan = "Tidak Diketahui";
        }

        if (hargaPerKg <= 0) {
            hargaPerKg = 0;
        }

        if (estimasiHari < 0) {
            estimasiHari = 0;
        }

        if (jenisProses == null || jenisProses.isEmpty()) {
            jenisProses = "Reguler";
        }

        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.hargaPerKg = hargaPerKg;
        this.estimasiHari = estimasiHari;
        this.jenisProses = jenisProses;
    }

    public abstract double hitungBiaya(double berat);

    public abstract String deskripsiLayanan();

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    public int getIdLayanan() {
        return idLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public int getEstimasiHari() {
        return estimasiHari;
    }

    public String getJenisProses() {
        return jenisProses;
    }
}