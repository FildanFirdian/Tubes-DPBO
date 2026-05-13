package model;

public abstract class Layanan {

    private String test1;
    private int idLayanan;
    private String namaLayanan;
    private double hargaPerKg;
    private int estimasiHari;

    public Layanan(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari) {

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

        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.hargaPerKg = hargaPerKg;
        this.estimasiHari = estimasiHari;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    public int getEstimasiHari() {
        return estimasiHari;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public abstract double hitungBiaya(double berat);

    public abstract String deskripsiLayanan();
}