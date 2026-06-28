package sistem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * {@code DBConnection} adalah kelas utilitas untuk membuat dan mengelola
 * koneksi JDBC ke database MySQL {@code sistemlaundry}.
 *
 * <p>Kelas ini bersifat <b>statis</b> — tidak perlu diinstansiasi.
 * Setiap kali {@link #getConnection()} dipanggil, koneksi baru dibuat
 * dan harus ditutup oleh pemanggil (gunakan try-with-resources).</p>
 *
 * <p><b>Konfigurasi koneksi:</b>
 * <ul>
 *   <li>Host: {@code localhost:3306}</li>
 *   <li>Database: {@code sistemlaundry}</li>
 *   <li>User: {@code root}</li>
 *   <li>Password: {@code ""} (kosong, default Laragon/XAMPP)</li>
 * </ul>
 * </p>
 *
 * <p>Jika password MySQL Anda berbeda, ubah konstanta {@code PASSWORD} di kelas ini.</p>
 */
public class DBConnection {

    /** URL koneksi JDBC ke database sistemlaundry. */
    private static final String URL      = "jdbc:mysql://localhost:3306/sistemlaundry";

    /** Nama pengguna MySQL (default: root). */
    private static final String USER     = "root";

    /** Password MySQL (default kosong untuk Laragon/XAMPP). Ubah jika perlu. */
    private static final String PASSWORD = "";

    /**
     * Kelas utilitas — konstruktor privat agar tidak dapat diinstansiasi.
     */
    private DBConnection() {}

    /**
     * Membuat dan mengembalikan koneksi baru ke database MySQL.
     *
     * <p>Menggunakan JDBC Driver {@code com.mysql.cj.jdbc.Driver} dari
     * {@code mysql-connector-j-8.4.0.jar}.</p>
     *
     * <p><b>Contoh penggunaan:</b>
     * <pre>{@code
     * try (Connection conn = DBConnection.getConnection()) {
     *     // gunakan conn ...
     * } catch (SQLException e) {
     *     e.printStackTrace();
     * }
     * }</pre>
     * </p>
     *
     * @return objek {@link Connection} yang aktif ke database
     * @throws SQLException jika koneksi gagal (database mati, password salah, dll.)
     *                      atau jika driver JDBC tidak ditemukan di classpath
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver tidak ditemukan. " +
                "Pastikan mysql-connector-j-8.4.0.jar ada di folder lib/", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
