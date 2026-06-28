package sistem;

import actor.Admin;
import actor.Pelanggan;
import service.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code DatabaseManager} adalah lapisan akses data (Data Access Layer) utama
 * yang mengelola semua operasi CRUD ke database MySQL {@code sistemlaundry}.
 *
 * <p>Setiap metode membuka koneksi baru via {@link DBConnection#getConnection()}
 * dan menutupnya secara otomatis menggunakan try-with-resources, sehingga
 * tidak ada koneksi yang bocor (connection leak).</p>
 *
 * <p><b>Entitas yang dikelola:</b>
 * <ul>
 *   <li>{@link Admin} — autentikasi login</li>
 *   <li>{@link Layanan} — daftar layanan laundry</li>
 *   <li>{@link Pelanggan} — data pelanggan (CRUD lengkap)</li>
 *   <li>{@link Transaksi} — pencatatan transaksi</li>
 *   <li>{@link DetailTransaksi} — item layanan dalam transaksi</li>
 *   <li>{@link StatusLaundry} — status pesanan</li>
 * </ul>
 * </p>
 */
public class DatabaseManager {

    // ==================== ADMIN ====================

    /**
     * Mencari admin berdasarkan kombinasi username dan password.
     *
     * <p>Melakukan JOIN antara tabel {@code admin} dan {@code user} untuk
     * mendapatkan data lengkap admin. Digunakan pada proses login.</p>
     *
     * @param username username yang diinput pengguna
     * @param password password yang diinput pengguna
     * @return objek {@link Admin} jika ditemukan; {@code null} jika tidak cocok
     */
    public Admin getAdminByUsernamePassword(String username, String password) {
        String sql = "SELECT a.id_admin, a.nama_admin, u.id_user, u.username, u.password " +
                     "FROM admin a JOIN user u ON a.id_user = u.id_user " +
                     "WHERE u.username = ? AND u.password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(rs.getInt("id_admin"), rs.getString("username"),
                        rs.getString("password"), rs.getString("nama_admin"));
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getAdminByUsernamePassword: " + e.getMessage());
        }
        return null;
    }

    // ==================== LAYANAN ====================

    /**
     * Mengambil semua data layanan dari tabel {@code layanan}.
     *
     * <p>Menggunakan kolom {@code tipe_layanan} untuk menentukan subclass
     * mana yang harus di-instansiasi ({@link CuciKering}, {@link CuciSetrika},
     * atau {@link SetrikaSaja}). Ini adalah penerapan pola <em>Single Table
     * Inheritance</em>.</p>
     *
     * @return daftar semua {@link Layanan}; kosong jika tabel kosong
     */
    public List<Layanan> getAllLayanan() {
        List<Layanan> list = new ArrayList<>();
        String sql = "SELECT * FROM layanan";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int    id    = rs.getInt("id_layanan");
                String nama  = rs.getString("nama_layanan");
                double harga = rs.getDouble("harga_per_kg");
                int    est   = rs.getInt("estimasi_hari");
                String jenis = rs.getString("jenis_proses");
                String tipe  = rs.getString("tipe_layanan");

                Layanan l = null;
                switch (tipe) {
                    case "CuciKering":
                        l = new CuciKering(id, nama, harga, est, jenis);
                        break;
                    case "CuciSetrika":
                        l = new CuciSetrika(id, nama, harga, est, jenis,
                                rs.getDouble("biaya_tambahan"));
                        break;
                    case "SetrikaSaja":
                        l = new SetrikaSaja(id, nama, harga, est, jenis,
                                rs.getDouble("pengurangan_biaya"));
                        break;
                    default:
                        l = new CuciKering(id, nama, harga, est, jenis);
                }
                if (l != null) list.add(l);
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getAllLayanan: " + e.getMessage());
        }
        return list;
    }

    // ==================== PELANGGAN ====================

    /**
     * Mengambil semua data pelanggan dari tabel {@code pelanggan}
     * beserta data login dari tabel {@code user} via JOIN.
     *
     * @return daftar semua {@link Pelanggan}; kosong jika tabel kosong
     */
    public List<Pelanggan> getAllPelanggan() {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT p.id_pelanggan, p.nama, p.no_hp, p.alamat, " +
                     "u.id_user, u.username, u.password " +
                     "FROM pelanggan p JOIN user u ON p.id_user = u.id_user";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pelanggan(
                    rs.getInt("id_pelanggan"),
                    rs.getString("nama"),
                    rs.getString("no_hp"),
                    rs.getString("alamat"),
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getAllPelanggan: " + e.getMessage());
        }
        return list;
    }

    /**
     * Menyimpan pelanggan baru ke database dalam satu transaksi atomik.
     *
     * <p>Alur:
     * <ol>
     *   <li>Insert ke tabel {@code user} → dapatkan {@code id_user} yang di-generate</li>
     *   <li>Insert ke tabel {@code pelanggan} menggunakan {@code id_user} tersebut</li>
     * </ol>
     * Jika salah satu langkah gagal, seluruh operasi di-rollback.</p>
     *
     * @param p objek {@link Pelanggan} yang akan disimpan (id boleh 0)
     * @return {@code true} jika berhasil; {@code false} jika gagal
     */
    public boolean insertPelanggan(Pelanggan p) {
        String sqlUser     = "INSERT INTO user (username, password) VALUES (?, ?)";
        String sqlPelanggan = "INSERT INTO pelanggan (id_user, nama, no_hp, alamat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, p.getUsername());
                stmtUser.setString(2, p.getPassword());
                stmtUser.executeUpdate();
                ResultSet keys = stmtUser.getGeneratedKeys();
                if (keys.next()) {
                    int newUserId = keys.getInt(1);
                    try (PreparedStatement stmtP = conn.prepareStatement(sqlPelanggan)) {
                        stmtP.setInt(1, newUserId);
                        stmtP.setString(2, p.getNama());
                        stmtP.setString(3, p.getNoHP());
                        stmtP.setString(4, p.getAlamat());
                        stmtP.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("[DB ERROR] insertPelanggan (rollback): " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] insertPelanggan: " + e.getMessage());
        }
        return false;
    }

    /**
     * Memperbarui data pelanggan yang sudah ada di database.
     *
     * <p>Hanya kolom {@code nama}, {@code no_hp}, dan {@code alamat} yang
     * diperbarui. Username dan password tidak bisa diubah melalui metode ini.</p>
     *
     * @param p objek {@link Pelanggan} dengan data terbaru; {@code idPelanggan} harus valid
     * @return {@code true} jika baris berhasil diperbarui; {@code false} jika gagal
     */
    public boolean updatePelanggan(Pelanggan p) {
        String sql = "UPDATE pelanggan SET nama=?, no_hp=?, alamat=? WHERE id_pelanggan=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNama());
            stmt.setString(2, p.getNoHP());
            stmt.setString(3, p.getAlamat());
            stmt.setInt(4, p.getIdPelanggan());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DB ERROR] updatePelanggan: " + e.getMessage());
        }
        return false;
    }

    /**
     * Menghapus pelanggan beserta seluruh data terkaitnya secara atomik.
     *
     * <p>Urutan penghapusan mengikuti foreign key constraint:
     * <ol>
     *   <li>Hapus semua baris {@code detail_transaksi} yang transaksinya milik pelanggan ini</li>
     *   <li>Hapus semua baris {@code transaksi} milik pelanggan ini</li>
     *   <li>Hapus baris {@code pelanggan}</li>
     *   <li>Hapus baris {@code user} yang berelasi</li>
     * </ol>
     * Jika salah satu langkah gagal, seluruh operasi di-rollback.</p>
     *
     * @param idPelanggan ID pelanggan yang akan dihapus
     * @return {@code true} jika berhasil; {@code false} jika gagal
     */
    public boolean deletePelanggan(int idPelanggan) {
        String getIdUser = "SELECT id_user FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idUser = -1;
                try (PreparedStatement s = conn.prepareStatement(getIdUser)) {
                    s.setInt(1, idPelanggan);
                    ResultSet rs = s.executeQuery();
                    if (rs.next()) idUser = rs.getInt("id_user");
                }

                // 1. Hapus detail transaksi terkait
                String delDetail = "DELETE dt FROM detail_transaksi dt " +
                                   "JOIN transaksi t ON dt.id_transaksi = t.id_transaksi " +
                                   "WHERE t.id_pelanggan = ?";
                try (PreparedStatement s = conn.prepareStatement(delDetail)) {
                    s.setInt(1, idPelanggan); s.executeUpdate();
                }
                // 2. Hapus transaksi terkait
                try (PreparedStatement s = conn.prepareStatement(
                        "DELETE FROM transaksi WHERE id_pelanggan = ?")) {
                    s.setInt(1, idPelanggan); s.executeUpdate();
                }
                // 3. Hapus pelanggan
                try (PreparedStatement s = conn.prepareStatement(
                        "DELETE FROM pelanggan WHERE id_pelanggan = ?")) {
                    s.setInt(1, idPelanggan); s.executeUpdate();
                }
                // 4. Hapus user
                if (idUser != -1) {
                    try (PreparedStatement s = conn.prepareStatement(
                            "DELETE FROM user WHERE id_user = ?")) {
                        s.setInt(1, idUser); s.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("[DB ERROR] deletePelanggan (rollback): " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] deletePelanggan: " + e.getMessage());
        }
        return false;
    }

    // ==================== TRANSAKSI ====================

    /**
     * Mengambil semua transaksi dari database beserta status masing-masing.
     *
     * <p>Melakukan JOIN antara {@code transaksi} dan {@code status_laundry}.
     * Pelanggan di-resolve dari parameter {@code pelangganList} agar tidak
     * perlu query tambahan per transaksi.</p>
     *
     * @param pelangganList daftar pelanggan yang sudah dimuat sebelumnya
     * @return daftar semua {@link Transaksi}; kosong jika tidak ada data
     */
    public List<Transaksi> getAllTransaksi(List<Pelanggan> pelangganList) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, t.tanggal, t.jumlah_bayar, " +
                     "s.id_status, s.nama_status FROM transaksi t " +
                     "JOIN status_laundry s ON t.id_status = s.id_status";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int idP = rs.getInt("id_pelanggan");
                Pelanggan p = pelangganList.stream()
                        .filter(x -> x.getIdPelanggan() == idP)
                        .findFirst().orElse(null);
                if (p == null) continue;

                Transaksi t = new Transaksi(rs.getInt("id_transaksi"), p);
                t.setTanggal(rs.getString("tanggal"));
                t.setStatusLaundry(new StatusLaundry(
                        rs.getInt("id_status"), rs.getString("nama_status")));
                t.setJumlahBayar(rs.getDouble("jumlah_bayar"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getAllTransaksi: " + e.getMessage());
        }
        return list;
    }

    /**
     * Mengambil semua transaksi milik satu pelanggan tertentu,
     * lengkap dengan detail item layanannya.
     *
     * <p>Digunakan di tab "Detail Transaksi" untuk menampilkan riwayat
     * pesanan berdasarkan pelanggan yang dipilih.</p>
     *
     * @param idPelanggan   ID pelanggan yang dicari riwayatnya
     * @param pelangganList daftar pelanggan yang sudah dimuat sebelumnya
     * @return daftar {@link Transaksi} milik pelanggan tersebut; kosong jika tidak ada
     */
    public List<Transaksi> getTransaksiByPelanggan(int idPelanggan, List<Pelanggan> pelangganList) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.id_pelanggan, t.tanggal, t.jumlah_bayar, " +
                     "s.id_status, s.nama_status FROM transaksi t " +
                     "JOIN status_laundry s ON t.id_status = s.id_status " +
                     "WHERE t.id_pelanggan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelanggan);
            ResultSet rs = stmt.executeQuery();
            Pelanggan p = pelangganList.stream()
                    .filter(x -> x.getIdPelanggan() == idPelanggan)
                    .findFirst().orElse(null);
            if (p == null) return list;
            while (rs.next()) {
                Transaksi t = new Transaksi(rs.getInt("id_transaksi"), p);
                t.setTanggal(rs.getString("tanggal"));
                t.setStatusLaundry(new StatusLaundry(
                        rs.getInt("id_status"), rs.getString("nama_status")));
                t.setJumlahBayar(rs.getDouble("jumlah_bayar"));
                loadDetailTransaksi(conn, t);  // Load detail sekaligus
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getTransaksiByPelanggan: " + e.getMessage());
        }
        return list;
    }

    /**
     * Memuat semua item detail dari tabel {@code detail_transaksi}
     * untuk satu transaksi dan menambahkannya ke objek transaksi tersebut.
     *
     * <p>Metode privat ini dipanggil oleh {@link #getTransaksiByPelanggan}.
     * Menggunakan koneksi yang sudah ada agar tidak membuka koneksi baru.</p>
     *
     * @param conn koneksi database yang aktif
     * @param t    objek {@link Transaksi} yang akan diisi detail-nya
     * @throws SQLException jika terjadi kesalahan query
     */
    private void loadDetailTransaksi(Connection conn, Transaksi t) throws SQLException {
        String sql = "SELECT dt.id_detail, dt.berat, dt.subtotal, " +
                     "l.id_layanan, l.nama_layanan, l.harga_per_kg, l.estimasi_hari, " +
                     "l.jenis_proses, l.tipe_layanan, l.biaya_tambahan, l.pengurangan_biaya " +
                     "FROM detail_transaksi dt " +
                     "JOIN layanan l ON dt.id_layanan = l.id_layanan " +
                     "WHERE dt.id_transaksi = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getIdTransaksi());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Layanan l = buildLayanan(rs);
                if (l != null) {
                    DetailTransaksi dt = new DetailTransaksi(l, rs.getDouble("berat"));
                    t.tambahDetail(dt);
                }
            }
        }
    }

    /**
     * Membangun objek {@link Layanan} yang tepat dari baris ResultSet
     * berdasarkan kolom {@code tipe_layanan}.
     *
     * <p>Penerapan <em>Factory Method</em> sederhana: memilih subclass
     * yang sesuai berdasarkan string tipe.</p>
     *
     * @param rs ResultSet yang menunjuk ke baris layanan saat ini
     * @return objek subclass Layanan yang sesuai; {@code null} jika tipe tidak dikenal
     * @throws SQLException jika kolom tidak ditemukan di ResultSet
     */
    private Layanan buildLayanan(ResultSet rs) throws SQLException {
        int    id    = rs.getInt("id_layanan");
        String nama  = rs.getString("nama_layanan");
        double harga = rs.getDouble("harga_per_kg");
        int    est   = rs.getInt("estimasi_hari");
        String jenis = rs.getString("jenis_proses");
        String tipe  = rs.getString("tipe_layanan");
        switch (tipe) {
            case "CuciKering":  return new CuciKering(id, nama, harga, est, jenis);
            case "CuciSetrika": return new CuciSetrika(id, nama, harga, est, jenis, rs.getDouble("biaya_tambahan"));
            case "SetrikaSaja": return new SetrikaSaja(id, nama, harga, est, jenis, rs.getDouble("pengurangan_biaya"));
            default:
                System.err.println("[DB WARN] buildLayanan: tipe tidak dikenal = " + tipe);
                return null;
        }
    }

    /**
     * Menyimpan transaksi baru ke tabel {@code transaksi} dan mengembalikan
     * ID yang di-generate otomatis oleh database.
     *
     * <p>Metode ini hanya menyimpan header transaksi. Detail item harus
     * disimpan secara terpisah menggunakan {@link #insertDetailTransaksi}.</p>
     *
     * @param t objek {@link Transaksi} yang akan disimpan
     * @return ID transaksi baru (auto-increment); {@code -1} jika gagal
     */
    public int insertTransaksi(Transaksi t) {
        String sql = "INSERT INTO transaksi (id_pelanggan, tanggal, id_status, jumlah_bayar) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, t.getPelanggan().getIdPelanggan());
            stmt.setString(2, t.getTanggal());
            stmt.setInt(3, t.getStatusLaundry().getIdStatus());
            stmt.setDouble(4, t.getTotalHarga());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("[DB ERROR] insertTransaksi: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Menyimpan satu item detail transaksi ke tabel {@code detail_transaksi}.
     *
     * <p>Dipanggil setelah {@link #insertTransaksi} berhasil dan ID transaksi
     * baru sudah diketahui.</p>
     *
     * @param idTransaksi ID transaksi yang sudah tersimpan di database
     * @param dt          objek {@link DetailTransaksi} yang akan disimpan
     */
    public void insertDetailTransaksi(int idTransaksi, DetailTransaksi dt) {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_layanan, berat, subtotal) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            stmt.setInt(2, dt.getLayanan().getIdLayanan());
            stmt.setDouble(3, dt.getBerat());
            stmt.setDouble(4, dt.getSubtotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB ERROR] insertDetailTransaksi: " + e.getMessage());
        }
    }

    /**
     * Memperbarui status laundry dari sebuah transaksi.
     *
     * <p>Dipanggil dari tab "Detail Transaksi" saat admin mengubah status
     * pesanan melalui dropdown (Diproses → Selesai → Diambil).</p>
     *
     * @param idTransaksi ID transaksi yang statusnya akan diubah
     * @param idStatus    ID status baru dari tabel {@code status_laundry}
     * @return {@code true} jika berhasil diperbarui; {@code false} jika gagal
     */
    public boolean updateStatusTransaksi(int idTransaksi, int idStatus) {
        String sql = "UPDATE transaksi SET id_status = ? WHERE id_transaksi = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idStatus);
            stmt.setInt(2, idTransaksi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DB ERROR] updateStatusTransaksi: " + e.getMessage());
        }
        return false;
    }

    // ==================== STATUS LAUNDRY ====================

    /**
     * Mengambil semua status laundry yang tersedia dari tabel {@code status_laundry}.
     *
     * <p>Hasil digunakan untuk mengisi dropdown "Ubah Status" di tab
     * Detail Transaksi. Default status: Diproses (1), Selesai (2), Diambil (3).</p>
     *
     * @return daftar semua {@link StatusLaundry}; kosong jika tabel kosong
     */
    public List<StatusLaundry> getAllStatus() {
        List<StatusLaundry> list = new ArrayList<>();
        String sql = "SELECT * FROM status_laundry";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new StatusLaundry(
                    rs.getInt("id_status"),
                    rs.getString("nama_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getAllStatus: " + e.getMessage());
        }
        return list;
    }
}
