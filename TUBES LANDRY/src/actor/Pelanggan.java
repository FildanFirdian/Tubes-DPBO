package actor;
import java.util.ArrayList;
import sistem.Member;
import sistem.Transaksi;
import sistem.SistemLaundry;

public class Pelanggan extends User {
    private Member member;
    private ArrayList<Transaksi> riwayatTransaksi;

    public Pelanggan(int idUser, String nama, String noHP, String alamat, String username, String password) {
        super(idUser, nama, noHP, alamat, username, password);
        this.riwayatTransaksi = new ArrayList<>();
        this.member = null;
    }

    public void tambahTransaksi(Transaksi transaksi) {
        riwayatTransaksi.add(transaksi);
    }

    public ArrayList<Transaksi> lihatRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    public StatusLaundry lihatStatusLaundry(Transaksi transaksi) {
        return transaksi.getStatusLaundry();
    }

    public void tambahPoinMember(int jumlahPoin) {
        if (member != null) {
            member.tambahPoin(jumlahPoin);
        } else {
            System.out.println("Pelanggan belum memiliki member.");
        }
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public void tampilInfo() {
        super.tampilInfo();

        if (member != null) {
            System.out.println("Level Member : " + member.getLevelMember());
            System.out.println("Poin Member  : " + member.getPoin());
        } else {
            System.out.println("Belum menjadi member.");
        }
    }
}