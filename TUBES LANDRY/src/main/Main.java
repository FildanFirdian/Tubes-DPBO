package main;

import actor.Admin;
import actor.Pelanggan;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import service.*;
import sistem.*;

import java.util.List;


/**
 * Kelas utama aplikasi Sistem Manajemen Laundry berbasis JavaFX.
 *
 * <p>Mewarisi {@link Application} dari JavaFX dan menjadi titik masuk
 * tampilan grafis. Mengelola navigasi antar scene (Login → Dashboard)
 * serta ketiga tab utama: Kelola Pelanggan, Transaksi Baru, Detail Transaksi.</p>
 *
 * <p>Semua operasi data dilakukan melalui {@link DatabaseManager} yang
 * terhubung ke MySQL via {@link sistem.DBConnection}.</p>
 */
public class Main extends Application {

    /** Stage utama JavaFX yang digunakan sepanjang siklus hidup aplikasi. */
    private Stage primaryStage;

    /** Admin yang sedang login; diisi setelah autentikasi berhasil. */
    private Admin currentAdmin;

    // Data yang dimuat dari database (ObservableList agar TableView auto-update)
    private ObservableList<Pelanggan>   dataPelanggan = FXCollections.observableArrayList();
    private ObservableList<Transaksi>   dataTransaksi = FXCollections.observableArrayList();
    private ObservableList<Layanan>     daftarLayanan = FXCollections.observableArrayList();
    private ObservableList<StatusLaundry> daftarStatus = FXCollections.observableArrayList();

    /** Objek pengelola semua operasi CRUD ke database MySQL. */
    private DatabaseManager dbManager = new DatabaseManager();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Titik masuk JavaFX — dipanggil otomatis oleh {@link Application#launch}.
     * Menyimpan referensi stage dan menampilkan layar login pertama kali.
     *
     * @param primaryStage stage utama yang disediakan oleh JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistem Manajemen Laundry");
        showLoginScene();
    }

    // ==================== LOGIN ====================

    /**
     * Menampilkan scene layar Login.
     *
     * <p>Mengautentikasi admin via {@link DatabaseManager#getAdminByUsernamePassword}.
     * Jika berhasil, semua data dimuat dari DB dan dashboard ditampilkan.</p>
     */
    private void showLoginScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label titleLabel = new Label("Washly System");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        Label subLabel = new Label("Sistem Manajemen Laundry");
        subLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #aaaaaa;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #16213e; -fx-padding: 20; -fx-background-radius: 10;");

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Username");
        txtUsername.setPrefWidth(220);
        txtUsername.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-prompt-text-fill: #888;");

        grid.add(new Label("Username:") {{ setStyle("-fx-text-fill: white;"); }}, 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(new Label("Password:") {{ setStyle("-fx-text-fill: white;"); }}, 0, 1);
        grid.add(txtPassword, 1, 1);

        Button btnLogin = new Button("Login");
        btnLogin.setPrefWidth(220);
        btnLogin.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #ff6b6b;");

        btnLogin.setOnAction(e -> {
            String user = txtUsername.getText().trim();
            String pass = txtPassword.getText().trim();
            Admin admin = dbManager.getAdminByUsernamePassword(user, pass);
            if (admin != null) {
                currentAdmin = admin;
                loadAllDataFromDB();
                showDashboardScene();
            } else {
                lblError.setText("⚠ Username atau Password salah!");
            }
        });

        root.getChildren().addAll(titleLabel, subLabel, grid, btnLogin, lblError);

        Scene scene = new Scene(root, 440, 360);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ==================== LOAD DATA ====================

    /**
     * Memuat ulang semua data dari database ke ObservableList.
     *
     * <p>Dipanggil sekali setelah login berhasil, dan setiap kali data
     * berubah (tambah/edit/hapus pelanggan atau transaksi).</p>
     */
    private void loadAllDataFromDB() {
        dataPelanggan.setAll(dbManager.getAllPelanggan());
        daftarLayanan.setAll(dbManager.getAllLayanan());
        daftarStatus.setAll(dbManager.getAllStatus());
        dataTransaksi.setAll(dbManager.getAllTransaksi(dataPelanggan));
    }

    // ==================== DASHBOARD ====================

    /**
     * Menampilkan scene Dashboard utama setelah login berhasil.
     *
     * <p>Dashboard terdiri dari toolbar atas (nama admin + tombol logout)
     * dan {@link TabPane} dengan 3 tab: Kelola Pelanggan, Transaksi Baru,
     * dan Detail Transaksi.</p>
     */
    private void showDashboardScene() {
        BorderPane root = new BorderPane();

        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #16213e;");

        Label welcomeLabel = new Label("Washly System  |  Halo, " + currentAdmin.getNamaAdmin());
        welcomeLabel.setStyle("-fx-text-fill: #e94560; -fx-font-weight: bold; -fx-font-size: 15px;");

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> showLoginScene());

        HBox.setHgrow(welcomeLabel, Priority.ALWAYS);
        topBar.getChildren().addAll(welcomeLabel, btnLogout);
        root.setTop(topBar);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createPelangganTab(),
            createTransaksiTab(),
            createDetailTransaksiTab()
        );
        tabPane.setStyle("-fx-background-color: #1a1a2e;");
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 950, 650);
        primaryStage.setScene(scene);
    }

    // ==================== TAB PELANGGAN ====================

    /**
     * Membuat tab "Kelola Pelanggan" dengan fitur CRUD lengkap.
     *
     * <p>Fitur tab ini:
     * <ul>
     *   <li>Tambah pelanggan baru ke database</li>
     *   <li>Edit data pelanggan yang dipilih</li>
     *   <li>Hapus pelanggan beserta seluruh transaksinya</li>
     *   <li>Klik baris tabel → form terisi otomatis</li>
     * </ul>
     * </p>
     *
     * <p><b>Validasi input:</b> nama, noHP, alamat, username, dan password
     * tidak boleh kosong. Exception dari model class ditangkap dan ditampilkan
     * sebagai pesan error di label status.</p>
     *
     * @return objek {@link Tab} yang siap ditambahkan ke TabPane
     */
    private Tab createPelangganTab() {
        Tab tab = new Tab("👥 Kelola Pelanggan");
        tab.setClosable(false);

        // ----- FORM INPUT -----
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(15));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        TextField txtNama = new TextField();
        txtNama.setPromptText("Nama lengkap");
        TextField txtHP = new TextField();
        txtHP.setPromptText("No. HP");
        TextField txtAlamat = new TextField();
        txtAlamat.setPromptText("Alamat");
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Username login pelanggan");

        Label lblNama = new Label("Nama:");
        lblNama.setStyle("-fx-text-fill: white;");
        formGrid.add(lblNama, 0, 0);
        formGrid.add(txtNama, 1, 0);
        
        Label lblHP = new Label("No HP:");
        lblHP.setStyle("-fx-text-fill: white;");
        formGrid.add(lblHP, 0, 1);
        formGrid.add(txtHP, 1, 1);
        
        Label lblAlamat = new Label("Alamat:");
        lblAlamat.setStyle("-fx-text-fill: white;");
        formGrid.add(lblAlamat, 0, 2);
        formGrid.add(txtAlamat, 1, 2);
        
        Label lblUser = new Label("Username:");
        lblUser.setStyle("-fx-text-fill: white;");
        formGrid.add(lblUser, 0, 3);
        formGrid.add(txtUsername, 1, 3);

        // ----- TABEL PELANGGAN -----
        TableView<Pelanggan> table = new TableView<>(dataPelanggan);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnTambah = new Button("➕ Tambah Pelanggan");
        Button btnEdit = new Button("✏ Edit Terpilih");
        Button btnHapus = new Button("🗑 Hapus Terpilih");
        Button btnBatal = new Button("🔄 Bersihkan");
        Label lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: green;");

        btnTambah.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
        btnEdit.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");
        btnHapus.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnBatal.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-cursor: hand;");

        btnBatal.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            txtNama.clear();
            txtHP.clear();
            txtAlamat.clear();
            txtUsername.clear();
            lblStatus.setText("");
        });

        HBox btnBox = new HBox(10, btnTambah, btnEdit, btnHapus, btnBatal);
        formGrid.add(btnBox, 1, 5);
        formGrid.add(lblStatus, 1, 6);

        TableColumn<Pelanggan, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        colId.setPrefWidth(50);

        TableColumn<Pelanggan, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Pelanggan, String> colHP = new TableColumn<>("No HP");
        colHP.setCellValueFactory(new PropertyValueFactory<>("noHP"));

        TableColumn<Pelanggan, String> colAlamat = new TableColumn<>("Alamat");
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        TableColumn<Pelanggan, String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));

        table.getColumns().addAll(colId, colNama, colHP, colAlamat, colUser);

        // Klik baris -> isi form
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                txtNama.setText(selected.getNama());
                txtHP.setText(selected.getNoHP());
                txtAlamat.setText(selected.getAlamat());
                txtUsername.setText(selected.getUsername());
                txtPassword.setText(selected.getPassword());
            } else {
                txtNama.clear();
                txtHP.clear();
                txtAlamat.clear();
                txtUsername.clear();
                txtPassword.clear();
            }
        });

        // ---- TAMBAH ----
        btnTambah.setOnAction(e -> {
            try {
                // Validasi field wajib diisi sebelum membuat objek
                String nama     = txtNama.getText().trim();
                String hp       = txtHP.getText().trim();
                String alamat   = txtAlamat.getText().trim();
                String usernam  = txtUsername.getText().trim();
                String pass     = txtPassword.getText().trim();

                if (nama.isEmpty() || hp.isEmpty() || alamat.isEmpty()
                        || usernam.isEmpty() || pass.isEmpty()) {
                    throw new IllegalArgumentException(
                        "Semua field (Nama, No HP, Alamat, Username, Password) wajib diisi!");
                }
                if (!hp.matches("[0-9+\\-]{8,15}")) {
                    throw new IllegalArgumentException(
                        "Format No HP tidak valid! Gunakan angka (8-15 digit), " +
                        "boleh diawali + atau -.");
                }
                if (usernam.length() < 3) {
                    throw new IllegalArgumentException(
                        "Username minimal 3 karakter.");
                }
                if (pass.length() < 3) {
                    throw new IllegalArgumentException(
                        "Password minimal 3 karakter.");
                }

                // Konstruktor Pelanggan akan melempar IllegalArgumentException
                // jika ada field yang tidak valid (double-check dari model)
                Pelanggan baru = new Pelanggan(0, nama, hp, alamat, 0, usernam, pass);
                boolean ok = dbManager.insertPelanggan(baru);
                if (ok) {
                    dataPelanggan.setAll(dbManager.getAllPelanggan());
                    txtNama.clear(); txtHP.clear(); txtAlamat.clear();
                    txtUsername.clear(); txtPassword.clear();
                    lblStatus.setStyle("-fx-text-fill: green;");
                    lblStatus.setText("✅ Pelanggan berhasil ditambahkan!");
                } else {
                    lblStatus.setStyle("-fx-text-fill: red;");
                    lblStatus.setText("❌ Gagal menyimpan ke database! " +
                        "Pastikan username belum dipakai.");
                }
            } catch (IllegalArgumentException ex) {
                lblStatus.setStyle("-fx-text-fill: red;");
                lblStatus.setText("⚠ " + ex.getMessage());
            }
        });

        // ---- EDIT ----
        btnEdit.setOnAction(e -> {
            Pelanggan selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblStatus.setStyle("-fx-text-fill: orange;");
                lblStatus.setText("⚠ Pilih pelanggan di tabel sebelum menekan Edit!");
                return;
            }
            try {
                String nama   = txtNama.getText().trim();
                String hp     = txtHP.getText().trim();
                String alamat = txtAlamat.getText().trim();

                if (nama.isEmpty() || hp.isEmpty() || alamat.isEmpty()) {
                    throw new IllegalArgumentException(
                        "Nama, No HP, dan Alamat tidak boleh kosong!");
                }
                if (!hp.matches("[0-9+\\-]{8,15}")) {
                    throw new IllegalArgumentException(
                        "Format No HP tidak valid! Gunakan angka (8-15 digit).");
                }

                // Setter model juga memvalidasi; tangkap jika ada error
                selected.setNama(nama);
                selected.setNoHP(hp);
                selected.setAlamat(alamat);

                boolean ok = dbManager.updatePelanggan(selected);
                if (ok) {
                    dataPelanggan.setAll(dbManager.getAllPelanggan());
                    txtNama.clear(); txtHP.clear(); txtAlamat.clear();
                    txtUsername.clear(); txtPassword.clear();
                    lblStatus.setStyle("-fx-text-fill: green;");
                    lblStatus.setText("✅ Data pelanggan berhasil diupdate!");
                } else {
                    lblStatus.setStyle("-fx-text-fill: red;");
                    lblStatus.setText("❌ Gagal update ke database!");
                }
            } catch (IllegalArgumentException ex) {
                lblStatus.setStyle("-fx-text-fill: red;");
                lblStatus.setText("⚠ " + ex.getMessage());
            }
        });

        // ---- HAPUS ----
        btnHapus.setOnAction(e -> {
            Pelanggan selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblStatus.setStyle("-fx-text-fill: orange;");
                lblStatus.setText("Pilih pelanggan yang ingin dihapus!");
                return;
            }
            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus pelanggan \"" + selected.getNama() + "\" beserta seluruh transaksinya?",
                    ButtonType.YES, ButtonType.NO);
            konfirmasi.setTitle("Konfirmasi Hapus");
            konfirmasi.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    boolean ok = dbManager.deletePelanggan(selected.getIdPelanggan());
                    if (ok) {
                        dataPelanggan.setAll(dbManager.getAllPelanggan());
                        dataTransaksi.setAll(dbManager.getAllTransaksi(dataPelanggan));
                        txtNama.clear(); txtHP.clear(); txtAlamat.clear();
                        txtUsername.clear(); txtPassword.clear();
                        lblStatus.setStyle("-fx-text-fill: green;");
                        lblStatus.setText("✅ Pelanggan berhasil dihapus!");
                    } else {
                        lblStatus.setStyle("-fx-text-fill: red;");
                        lblStatus.setText("❌ Gagal menghapus pelanggan!");
                    }
                }
            });
        });

        VBox layout = new VBox(10, formGrid, new Separator(), table);
        layout.setPadding(new Insets(10));
        VBox.setVgrow(table, Priority.ALWAYS);
        tab.setContent(layout);
        return tab;
    }

    // ==================== TAB TRANSAKSI ====================

    /**
     * Membuat tab "Transaksi Baru" untuk mencatat pesanan laundry pelanggan.
     *
     * <p>Alur kerja:
     * <ol>
     *   <li>Pilih pelanggan dari dropdown</li>
     *   <li>Pilih layanan (CuciKering/CuciSetrika/SetrikaSaja)</li>
     *   <li>Input berat dalam kg → tekan "Hitung Biaya" untuk preview</li>
     *   <li>Tekan "Simpan Transaksi" → data tersimpan ke database</li>
     * </ol>
     * </p>
     *
     * <p><b>Validasi:</b> pelanggan dan layanan wajib dipilih; berat harus
     * angka positif (format tidak valid akan ditangkap dengan {@link NumberFormatException}).</p>
     *
     * @return objek {@link Tab} yang siap ditambahkan ke TabPane
     */
    private Tab createTransaksiTab() {
        Tab tab = new Tab("🧾 Transaksi Baru");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(12);
        grid.setVgap(12);

        ComboBox<Pelanggan> cbPelanggan = new ComboBox<>(dataPelanggan);
        cbPelanggan.setPromptText("Pilih Pelanggan...");
        cbPelanggan.setPrefWidth(250);

        ComboBox<Layanan> cbLayanan = new ComboBox<>(daftarLayanan);
        cbLayanan.setPromptText("Pilih Jenis Layanan...");
        cbLayanan.setPrefWidth(250);

        TextField txtBerat = new TextField();
        txtBerat.setPromptText("contoh: 3.5");

        Label lblSubtotal = new Label("Subtotal: Rp 0");
        lblSubtotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #27ae60;");

        Label lblDeskripsi = new Label();
        lblDeskripsi.setStyle("-fx-font-style: italic; -fx-text-fill: #cccccc;");

        Label lblPel = new Label("Pelanggan:");
        lblPel.setStyle("-fx-text-fill: white;");
        grid.add(lblPel, 0, 0);
        grid.add(cbPelanggan, 1, 0);

        Label lblLay = new Label("Layanan:");
        lblLay.setStyle("-fx-text-fill: white;");
        grid.add(lblLay, 0, 1);
        grid.add(cbLayanan, 1, 1);

        Label lblBrt = new Label("Berat (Kg):");
        lblBrt.setStyle("-fx-text-fill: white;");
        grid.add(lblBrt, 0, 2);
        grid.add(txtBerat, 1, 2);

        grid.add(lblSubtotal, 1, 3);
        grid.add(lblDeskripsi, 1, 4);

        Button btnHitung = new Button("🔢 Hitung Biaya");
        Button btnSimpan = new Button("💾 Simpan Transaksi");
        btnHitung.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");
        btnSimpan.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");

        HBox actionBox = new HBox(10, btnHitung, btnSimpan);
        grid.add(actionBox, 1, 5);

        Label lblResult = new Label();
        grid.add(lblResult, 1, 6);

        btnHitung.setOnAction(e -> {
            try {
                Layanan lay = cbLayanan.getValue();
                if (lay == null) {
                    throw new IllegalArgumentException("Pilih jenis layanan terlebih dahulu!");
                }
                String beratStr = txtBerat.getText().trim();
                if (beratStr.isEmpty()) {
                    throw new IllegalArgumentException("Masukkan berat cucian (contoh: 3.5)");
                }
                double berat = Double.parseDouble(beratStr);
                if (berat <= 0) {
                    throw new IllegalArgumentException(
                        "Berat harus lebih dari 0 kg. Masukkan angka positif.");
                }
                // hitungBiaya() dari model juga melempar exception jika berat tidak valid
                double biaya = lay.hitungBiaya(berat);
                lblSubtotal.setText(String.format("Subtotal: Rp %.0f", biaya));
                lblDeskripsi.setText(lay.deskripsiLayanan());
            } catch (NumberFormatException ex) {
                lblSubtotal.setText("⚠ Format berat tidak valid! Gunakan angka (contoh: 3.5)");
            } catch (IllegalArgumentException ex) {
                lblSubtotal.setText("⚠ " + ex.getMessage());
            }
        });

        btnSimpan.setOnAction(e -> {
            try {
                Pelanggan pel = cbPelanggan.getValue();
                Layanan lay   = cbLayanan.getValue();

                if (pel == null) {
                    throw new IllegalArgumentException("Pilih pelanggan terlebih dahulu!");
                }
                if (lay == null) {
                    throw new IllegalArgumentException("Pilih jenis layanan terlebih dahulu!");
                }
                String beratStr = txtBerat.getText().trim();
                if (beratStr.isEmpty()) {
                    throw new IllegalArgumentException(
                        "Masukkan berat cucian sebelum menyimpan transaksi.");
                }
                double berat = Double.parseDouble(beratStr);
                if (berat <= 0) {
                    throw new IllegalArgumentException(
                        "Berat harus lebih dari 0 kg.");
                }

                // Ambil status default "Diproses" dari daftarStatus
                StatusLaundry statusDefault = daftarStatus.stream()
                        .filter(s -> s.getNamaStatus().equalsIgnoreCase("Diproses"))
                        .findFirst()
                        .orElse(new StatusLaundry(1, "Diproses"));

                // Konstruktor Transaksi & DetailTransaksi juga memvalidasi parameter
                Transaksi trans = new Transaksi(0, pel);
                trans.setStatusLaundry(statusDefault);

                int newId = dbManager.insertTransaksi(trans);
                if (newId > 0) {
                    DetailTransaksi detail = new DetailTransaksi(lay, berat);
                    dbManager.insertDetailTransaksi(newId, detail);
                    dataTransaksi.setAll(dbManager.getAllTransaksi(dataPelanggan));

                    lblResult.setStyle("-fx-text-fill: green;");
                    lblResult.setText("✅ Transaksi berhasil disimpan! ID: " + newId);
                    txtBerat.clear();
                    lblSubtotal.setText("Subtotal: Rp 0");
                    lblDeskripsi.setText("");
                    cbPelanggan.setValue(null);
                    cbLayanan.setValue(null);
                } else {
                    lblResult.setStyle("-fx-text-fill: red;");
                    lblResult.setText("❌ Gagal menyimpan transaksi ke database!");
                }
            } catch (NumberFormatException ex) {
                lblResult.setStyle("-fx-text-fill: red;");
                lblResult.setText("⚠ Format berat tidak valid! Gunakan angka (contoh: 3.5)");
            } catch (IllegalArgumentException ex) {
                lblResult.setStyle("-fx-text-fill: red;");
                lblResult.setText("⚠ " + ex.getMessage());
            }
        });

        tab.setContent(grid);
        return tab;
    }

    // ==================== TAB DETAIL TRANSAKSI PER PELANGGAN ====================

    /**
     * Membuat tab "Detail Transaksi" untuk melihat riwayat pesanan per pelanggan
     * dan mengubah status transaksi.
     *
     * <p>Alur kerja:
     * <ol>
     *   <li>Pilih pelanggan dari dropdown → tekan "Tampilkan Transaksi"</li>
     *   <li>Semua transaksi pelanggan muncul di tabel atas</li>
     *   <li>Klik baris transaksi → detail item layanan muncul di tabel bawah</li>
     *   <li>Ubah status via dropdown di kolom "Ubah Status" → tekan Update</li>
     * </ol>
     * </p>
     *
     * @return objek {@link Tab} yang siap ditambahkan ke TabPane
     */
    private Tab createDetailTransaksiTab() {
        Tab tab = new Tab("📋 Detail Transaksi");
        tab.setClosable(false);

        // Bagian atas: pilih pelanggan + tombol cari
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(15));
        topBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<Pelanggan> cbPelanggan = new ComboBox<>(dataPelanggan);
        cbPelanggan.setPromptText("Pilih Pelanggan...");
        cbPelanggan.setPrefWidth(220);

        Button btnCari = new Button("🔍 Tampilkan Transaksi");
        btnCari.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");

        Label lblPelFilter = new Label("Pelanggan: ");
        lblPelFilter.setStyle("-fx-text-fill: white;");
        topBox.getChildren().addAll(lblPelFilter, cbPelanggan, btnCari);

        // Tabel Transaksi
        TableView<Transaksi> tabelTransaksi = new TableView<>();
        tabelTransaksi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Transaksi, Integer> colId = new TableColumn<>("ID Transaksi");
        colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));

        TableColumn<Transaksi, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        TableColumn<Transaksi, Double> colTotal = new TableColumn<>("Total Bayar");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("jumlahBayar"));
        colTotal.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : String.format("Rp %.0f", val));
            }
        });

        TableColumn<Transaksi, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatusLaundry().getNamaStatus()
            )
        );

        // Kolom Aksi: ganti status
        TableColumn<Transaksi, Void> colAksi = new TableColumn<>("Ubah Status");
        colAksi.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<StatusLaundry> cbStatus = new ComboBox<>(daftarStatus);
            private final Button btnUpdate = new Button("Update");
            private final HBox box = new HBox(5, cbStatus, btnUpdate);
            {
                cbStatus.setPrefWidth(120);
                btnUpdate.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-cursor: hand;");
                btnUpdate.setOnAction(e -> {
                    Transaksi t = getTableView().getItems().get(getIndex());
                    StatusLaundry newStatus = cbStatus.getValue();
                    if (newStatus != null) {
                        boolean ok = dbManager.updateStatusTransaksi(t.getIdTransaksi(), newStatus.getIdStatus());
                        if (ok) {
                            // refresh tabel
                            Pelanggan sel = cbPelanggan.getValue();
                            if (sel != null) {
                                List<Transaksi> segar = dbManager.getTransaksiByPelanggan(
                                        sel.getIdPelanggan(), dataPelanggan);
                                tabelTransaksi.setItems(FXCollections.observableArrayList(segar));
                            }
                        }
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Transaksi t = getTableView().getItems().get(getIndex());
                    // Preset status saat ini
                    daftarStatus.stream()
                        .filter(s -> s.getIdStatus() == t.getStatusLaundry().getIdStatus())
                        .findFirst().ifPresent(cbStatus::setValue);
                    setGraphic(box);
                }
            }
        });

        tabelTransaksi.getColumns().addAll(colId, colTgl, colTotal, colStatus, colAksi);

        // Tabel Detail Item
        Label lblDetail = new Label("Detail Item Layanan:");
        lblDetail.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: white;");

        TableView<DetailTransaksi> tabelDetail = new TableView<>();
        tabelDetail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelDetail.setPrefHeight(180);

        TableColumn<DetailTransaksi, String> colLayanan = new TableColumn<>("Layanan");
        colLayanan.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getLayanan().getNamaLayanan()
            )
        );

        TableColumn<DetailTransaksi, Double> colBerat = new TableColumn<>("Berat (Kg)");
        colBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));

        TableColumn<DetailTransaksi, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colSubtotal.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : String.format("Rp %.0f", val));
            }
        });

        TableColumn<DetailTransaksi, String> colDeskripsi = new TableColumn<>("Deskripsi");
        colDeskripsi.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getLayanan().deskripsiLayanan()
            )
        );

        tabelDetail.getColumns().addAll(colLayanan, colBerat, colSubtotal, colDeskripsi);

        // Klik baris transaksi -> tampilkan detail
        tabelTransaksi.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                // load detail dari DB langsung (sudah di-load saat getTransaksiByPelanggan)
                tabelDetail.setItems(FXCollections.observableArrayList(selected.getDetailList()));
            } else {
                tabelDetail.getItems().clear();
            }
        });

        // Tombol cari
        btnCari.setOnAction(e -> {
            Pelanggan sel = cbPelanggan.getValue();
            if (sel == null) return;
            List<Transaksi> transByPel = dbManager.getTransaksiByPelanggan(
                    sel.getIdPelanggan(), dataPelanggan);
            tabelTransaksi.setItems(FXCollections.observableArrayList(transByPel));
            tabelDetail.getItems().clear();
        });

        VBox layout = new VBox(10,
                topBox,
                new Separator(),
                tabelTransaksi,
                new Separator(),
                lblDetail,
                tabelDetail
        );
        layout.setPadding(new Insets(10));
        VBox.setVgrow(tabelTransaksi, Priority.ALWAYS);

        tab.setContent(layout);
        return tab;
    }
}
