package sistem;

public class Transaksi {
    private int idPembayaran;
    private int tanggalBayar;
    private double jumlahBayar;
    private String statusBayar;
    
    public Transaksi(){
        
    }
    
    public Transaksi(int idPembayaran, int tanggalBayar, double jumlahBayar, String statusBayar){
        this.idPembayaran = idPembayaran;
        this.tanggalBayar = tanggalBayar;
        this.jumlahBayar = jumlahBayar;
    }
    
    public void prosesBayar(){
        
    }
    
    public void konfirmasiBayar(){
        
    }
}
