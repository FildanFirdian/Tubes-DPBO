package sistem;
import java.util.ArrayList;
import java.util.Date;
import actor.Pelanggan;

public class Transaksi {
    private int idTransaksi;
    private Date tanggalMasuk;
    private Date tanggalSelesai; 
    private Pelanggan pelanggan;
    private ArrayList<DetailTransaksi> daftarDetail;
    private Diskon diskon;
    private StatusLaundry statusLaundry;
    private double totalHarga;
    private double totalBayar;
    private double jumlahBayar;
    private double kembalian;
    private String statusBayar;
    
    public Transaksi(int idTransaksi, Date tanggalMasuk, Date tanggalSelesai, Pelanggan pelanggan{
        this.idTransaksi = idTransaksi;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalSelesai = tanggalSelesai;
        this.pelanggan = pelanggan;
    }
    
    public void tambahDetail(DetailTransaksi detail){
        daftarDetail.add(detail);   
    }
    
    public double hitungTotalHarga(){
        
    }

    public double terapkanDiskon(Diskon diskon){
        
    }

    public void inputPembayaran(double jumlahBayar){
        
    }

    public double hitungKembalian(){
        
    }

    public void ubahStatus(StatusLaundry statusLaundry){
        this.statusLaundry = statusLaundry;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public double getJumlahBayar() {
        return jumlahBayar;
    }

    public double getKembalian() {
        return kembalian;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public StatusLaundry getStatusLaundry() {
        return statusLaundry;
    }

    public ArrayList<DetailTransaksi> getDaftarDetail() {
        return daftarDetail;
    }
}
