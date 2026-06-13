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
            throw new IllegalArgumentException("ID layanan harus lebih dari 0");
        }

        if (namaLayanan == null || namaLayanan.isEmpty()) {
            throw new IllegalArgumentException("Nama layanan tidak boleh kosong");
        }

        if (hargaPerKg <= 0) {
            throw new IllegalArgumentException("Harga per Kg harus lebih dari 0");
        }

        if (estimasiHari < 0) {
            throw new IllegalArgumentException("Estimasi hari tidak boleh negatif");
        }

        if (jenisProses == null || jenisProses.isEmpty()) {
            throw new IllegalArgumentException("Jenis proses tidak boleh kosong");
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