package actor;
import java.util.ArrayList;


public class Pelanggan extends User {
    private Member member;
    private ArrayList<Transaksi> riwayatTransaksi;

    public Pelanggan(int idUser, String nama, String noHP, String alamat, String username, String password) {
        super(idUser, nama, noHP, alamat, username, password);
        this.member = null;
        this.riwayatTransaksi = new ArrayList<>();
    }

    public void tampilDataDiri() {
        System.out.println("=== DATA DIRI PELANGGAN ===");
        System.out.println("ID User  : " + getIdUser());
        System.out.println("Nama     : " + getNama());
        System.out.println("No HP    : " + getNoHP());
        System.out.println("Alamat   : " + getAlamat());
        System.out.println("Username : " + getUsername());

        if (member != null && member.cekKeanggotaan()) {
            System.out.println("Status Member : Aktif");
            System.out.println("Level Member  : " + member.getLevelMember());
            System.out.println("Poin Member   : " + member.getPoin());
        } else {
            System.out.println("Status Member : Bukan Member");
        }
    }

    public void tampilkanSemuaPesanan() {
        System.out.println("=== SELURUH PESANAN PELANGGAN ===");

        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada pesanan.");
            return;
        }

        for (Transaksi transaksi : riwayatTransaksi) {
            transaksi.tampilkanDetailTransaksi();
            System.out.println("--------------------------------");
        }
    }

    public Transaksi cariPesananByIdTransaksi(int idTransaksi) {
        for (Transaksi transaksi : riwayatTransaksi) {
            if (transaksi.getIdTransaksi() == idTransaksi) {
                return transaksi;
            }
        }

        return null;
    }

    public void tampilkanPesananByIdTransaksi(int idTransaksi) {
        Transaksi transaksi = cariPesananByIdTransaksi(idTransaksi);

        if (transaksi != null) {
            System.out.println("=== PESANAN DITEMUKAN ===");
            transaksi.tampilkanDetailTransaksi();
        } else {
            System.out.println("Pesanan dengan ID Transaksi " + idTransaksi + " tidak ditemukan.");
        }
    }

    public void tambahTransaksi(Transaksi transaksi) {
        if (transaksi != null) {
            riwayatTransaksi.add(transaksi);
            System.out.println("Transaksi berhasil ditambahkan ke riwayat pelanggan.");
        } else {
            System.out.println("Transaksi tidak boleh kosong.");
        }
    }

    public ArrayList<Transaksi> lihatRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    public StatusLaundry lihatStatusLaundry(Transaksi transaksi) {
        if (transaksi != null) {
            return transaksi.getStatusLaundry();
        }

        return null;
    }

    public void tambahPoinMember(int jumlahPoin) {
        if (member != null) {
            member.tambahPoin(jumlahPoin);
            System.out.println("Poin member berhasil ditambahkan.");
        } else {
            System.out.println("Pelanggan belum menjadi member.");
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
        tampilDataDiri();
    }
}