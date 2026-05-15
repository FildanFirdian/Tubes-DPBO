package model;

public class Pembayaran {
    private int idPembayaran;
    private int tanggalBayar;
    private double jumlahBayar;
    private String statusBayar;
    
    public Pembayaran(){
        
    }
    
    public Pembayaran(int idPembayaran, int tanggalBayar, double jumlahBayar, String statusBayar){
        this.idPembayaran = idPembayaran;
        this.tanggalBayar = tanggalBayar;
        this.jumlahBayar = jumlahBayar;
    }
    
    public void prosesBayar(){
        
    }
    
    public void konfirmasiBayar(){
        
    }
}
