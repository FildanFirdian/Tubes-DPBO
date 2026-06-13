package service;

public class SetrikaSaja extends Layanan {

    private double penguranganBiaya;

    public SetrikaSaja(int idLayanan, String namaLayanan,
            double hargaPerKg, int estimasiHari,
            String jenisProses,
            double penguranganBiaya) {

        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);

        if (penguranganBiaya < 0) {
            throw new IllegalArgumentException("Pengurangan biaya tidak boleh negatif");
        }

        this.penguranganBiaya = penguranganBiaya;
    }

    public double getPenguranganBiaya() {
        return penguranganBiaya;
    }

    public void setPenguranganBiaya(double penguranganBiaya) {
        if (penguranganBiaya < 0) {
            throw new IllegalArgumentException("Pengurangan biaya tidak boleh negatif");
        }

        this.penguranganBiaya = penguranganBiaya;
    }

    @Override
    public double hitungBiaya(double berat) {

        if (berat <= 0) {
            throw new IllegalArgumentException("Berat laundry harus lebih dari 0 Kg");
        }

        double subtotal = berat * getHargaPerKg();
        double potongan = subtotal * penguranganBiaya;

        return subtotal - potongan;
    }

    @Override
    public String deskripsiLayanan() {
        return "Setrika Saja - " + getJenisProses() +
                " | Pengurangan biaya: " + (penguranganBiaya * 100) + "%" +
                " (" + getEstimasiHari() + " Hari)";
    }
}