package sistem;

import actor.Pelanggan;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private int idTransaksi;
    private Pelanggan pelanggan;
    private String tanggal;          
    private StatusLaundry statusLaundry; 
    private double jumlahBayar;      
    private List<DetailTransaksi> detailList = new ArrayList<>();

    // KONTRAKTOR UTAMA: Dipanggil oleh JavaFX Main App
    public Transaksi(int idTransaksi, Pelanggan pelanggan) {
        this.idTransaksi = idTransaksi;
        this.pelanggan = pelanggan;
        this.tanggal = java.time.LocalDate.now().toString(); // Default hari ini
        this.statusLaundry = new StatusLaundry(1, "Diproses"); 
    }

    public void tambahDetail(DetailTransaksi detail) {
        detailList.add(detail);
        this.jumlahBayar = getTotalHarga();
    }

    public double getKembalian() {
        double kembalian = jumlahBayar - getTotalHarga();
        if (kembalian < 0){
            return 0;
        }
        return kembalian;
    }

    public int getIdTransaksi() { return idTransaksi; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public String getTanggal() { return tanggal; }
    public StatusLaundry getStatusLaundry() { return statusLaundry; }
    
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setStatusLaundry(StatusLaundry statusLaundry) { this.statusLaundry = statusLaundry; }
    public void setJumlahBayar(double jumlahBayar) { this.jumlahBayar = jumlahBayar; }
    public double getJumlahBayar() { return jumlahBayar; }
    public List<DetailTransaksi> getDetailList() { return detailList; }
    
    public double getTotalHarga() {
        return detailList.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();
    }
}