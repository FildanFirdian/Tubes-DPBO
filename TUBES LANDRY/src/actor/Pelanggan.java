package actor;
import java.util.ArrayList;
import sistem.Member;
import sistem.Transaksi;
import sistem.SistemLaundry;

public class Pelanggan extends User {

    private int idPelanggan;
    private String nama;
    private String noHP;
    private String alamat;

    public Pelanggan(int idPelanggan, String nama, String noHP, String alamat,int idUser,String username, String password) {
        super(idUser, username, password);
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.noHP = noHP;
        this.alamat = alamat;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        return "ID: " + idPelanggan +
                ", Nama: " + nama +
                ", No HP: " + noHP +
                ", Alamat: " + alamat;
    }

    @Override
    public void tampilInfo() {
        System.out.println("ID Pelanggan : " + idPelanggan);
        System.out.println("Nama         : " + nama);
        System.out.println("No HP        : " + noHP);
        System.out.println("Alamat       : " + alamat);
    }
}