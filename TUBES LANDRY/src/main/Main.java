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

import java.sql.*;
import java.util.ArrayList;

public class Main extends Application {

    private Stage primaryStage;
    private Admin currentAdmin = null;
    
    // In-Memory Database Dummy Data
    private ObservableList<Pelanggan> dataPelanggan = FXCollections.observableArrayList();
    private ObservableList<Transaksi> dataTransaksi = FXCollections.observableArrayList();
    private ObservableList<Layanan> daftarLayanan = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistem Manajemen Laundry Modern");
        
        // Load data from Database
        loadLayananFromDatabase();
        loadPelangganFromDatabase();
        loadTransaksiFromDatabase();

        showLoginScene();
    }

    private void showLoginScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("login-container");

        Label titleLabel = new Label("Washly");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Username");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(txtPassword, 1, 1);

        Button btnLogin = new Button("Login");
        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red;");

        btnLogin.setOnAction(e -> {
            Admin admin = loginAdmin(txtUsername.getText(), txtPassword.getText());
            if (admin != null) {
                currentAdmin = admin;
                showDashboardScene();
            } else {
                lblError.setText("Kombinasi User/Password Salah!");
            }
        });

        root.getChildren().addAll(titleLabel, grid, btnLogin, lblError);
        Scene scene = new Scene(root, 400, 300);
        loadCss(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showDashboardScene() {
        BorderPane root = new BorderPane();
        
        // Top Toolbar Menu
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");
        Label welcomeLabel = new Label("Halo, Admin: " + currentAdmin.getNamaAdmin());
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        topBar.getChildren().add(welcomeLabel);
        root.setTop(topBar);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(createPelangganTab(), createTransaksiTab(), createLaporanTab());
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 850, 600);
        loadCss(scene);
        primaryStage.setScene(scene);
    }

    private Tab createPelangganTab() {
        Tab tab = new Tab("Kelola Pelanggan");
        tab.setClosable(false);

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(15));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        TextField txtNama = new TextField();
        TextField txtHP = new TextField();
        TextField txtAlamat = new TextField();

        formGrid.add(new Label("Nama Pelanggan:"), 0, 0);
        formGrid.add(txtNama, 1, 0);
        formGrid.add(new Label("No HP:"), 0, 1);
        formGrid.add(txtHP, 1, 1);
        formGrid.add(new Label("Alamat:"), 0, 2);
        formGrid.add(txtAlamat, 1, 2);

        Button btnAdd = new Button("Simpan Pelanggan");
        formGrid.add(btnAdd, 1, 3);

        TableView<Pelanggan> table = new TableView<>(dataPelanggan);
        TableColumn<Pelanggan, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        TableColumn<Pelanggan, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TableColumn<Pelanggan, String> colHP = new TableColumn<>("No HP");
        colHP.setCellValueFactory(new PropertyValueFactory<>("noHP"));
        TableColumn<Pelanggan, String> colAlamat = new TableColumn<>("Alamat");
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        table.getColumns().addAll(colId, colNama, colHP, colAlamat);

        btnAdd.setOnAction(e -> {
            String nama = txtNama.getText();
            String hp = txtHP.getText();
            String alamat = txtAlamat.getText();
            if (!nama.isEmpty()) {
                String genUser = "user_" + System.currentTimeMillis();
                String genPass = "pass";
                
                if (savePelangganToDatabase(nama, hp, alamat, genUser, genPass)) {
                    loadPelangganFromDatabase();
                    txtNama.clear(); txtHP.clear(); txtAlamat.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan pelanggan!");
                    alert.showAndWait();
                }
            }
        });

        VBox layout = new VBox(10, formGrid, table);
        layout.setPadding(new Insets(10));
        tab.setContent(layout);
        return tab;
    }

    private Tab createTransaksiTab() {
        Tab tab = new Tab("Transaksi Baru");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<Pelanggan> cbPelanggan = new ComboBox<>(dataPelanggan);
        ComboBox<Layanan> cbLayanan = new ComboBox<>(daftarLayanan);
        TextField txtBerat = new TextField();
        Label lblSubtotal = new Label("Subtotal: Rp0");
        lblSubtotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        grid.add(new Label("Pilih Pelanggan:"), 0, 0);
        grid.add(cbPelanggan, 1, 0);
        grid.add(new Label("Pilih Jenis Layanan:"), 0, 1);
        grid.add(cbLayanan, 1, 1);
        grid.add(new Label("Berat (Kg):"), 0, 2);
        grid.add(txtBerat, 1, 2);
        grid.add(lblSubtotal, 1, 3);

        Button btnHitung = new Button("Hitung Biaya");
        Button btnSimpan = new Button("Simpan & Selesai Transaksi");
        HBox actionBox = new HBox(10, btnHitung, btnSimpan);
        grid.add(actionBox, 1, 4);

        btnHitung.setOnAction(e -> {
            try {
                Layanan lay = cbLayanan.getValue();
                double berat = Double.parseDouble(txtBerat.getText());
                if (lay != null) {
                    lblSubtotal.setText("Subtotal: Rp" + lay.hitungBiaya(berat) + " (" + lay.deskripsiLayanan() + ")");
                }
            } catch (NumberFormatException ex) {
                lblSubtotal.setText("Input berat tidak valid!");
            }
        });

        btnSimpan.setOnAction(e -> {
            Pelanggan pel = cbPelanggan.getValue();
            Layanan lay = cbLayanan.getValue();
            try {
                double berat = Double.parseDouble(txtBerat.getText());
                if (pel != null && lay != null) {
                    double cost = lay.hitungBiaya(berat);
                    if (saveTransaksiToDatabase(pel.getIdPelanggan(), berat, lay.getIdLayanan(), cost)) {
                        loadTransaksiFromDatabase();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaksi Berhasil Ditambahkan!");
                        alert.showAndWait();
                        txtBerat.clear();
                        lblSubtotal.setText("Subtotal: Rp0");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan transaksi!");
                        alert.showAndWait();
                    }
                }
            } catch (Exception ex) {
                // Silently safe fallback inside JavaFX
            }
        });

        tab.setContent(grid);
        return tab;
    }
    
    private void DetailTransactionBuilder(Transaksi trans, double berat, Layanan lay) {
        DetailTransaksi detail = new DetailTransaksi(trans.getIdTransaksi(), berat, lay);
        trans.tambahDetail(detail);
    }

    private Tab createLaporanTab() {
        Tab tab = new Tab("Laporan Pendapatan");
        tab.setClosable(false);

        TextArea txtAreaLog = new TextArea();
        txtAreaLog.setEditable(false);
        txtAreaLog.setStyle("-fx-font-family: 'Courier New';");

        Button btnRefresh = new Button("Cetak & Muat Dokumen Laporan");
        btnRefresh.setOnAction(e -> {
            LaporanTransaksi engineLaporan = new LaporanTransaksi(new ArrayList<>(dataTransaksi));
            txtAreaLog.setText(engineLaporan.getFormattedLog());
        });

        VBox layout = new VBox(10, btnRefresh, txtAreaLog);
        layout.setPadding(new Insets(15));
        VBox.setVgrow(txtAreaLog, Priority.ALWAYS);
        tab.setContent(layout);
        return tab;
    }

    private void loadCss(Scene scene) {
        try {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        } catch (Exception e) {
            // Fallback default inline css if resource file not found
        }
    }

    private void loadLayananFromDatabase() {
        daftarLayanan.clear();
        String query = "SELECT * FROM layanan";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id_layanan");
                String nama = rs.getString("nama_layanan");
                double harga = rs.getDouble("harga_per_kg");
                int estimasi = rs.getInt("estimasi_hari");
                String proses = rs.getString("jenis_proses");
                String tipe = rs.getString("tipe_layanan");
                
                if ("CuciKering".equals(tipe)) {
                    daftarLayanan.add(new CuciKering(id, nama, harga, estimasi, proses));
                } else if ("CuciSetrika".equals(tipe)) {
                    double biayaTambahan = rs.getDouble("biaya_tambahan");
                    daftarLayanan.add(new CuciSetrika(id, nama, harga, estimasi, proses, biayaTambahan));
                } else if ("SetrikaSaja".equals(tipe)) {
                    double penguranganBiaya = rs.getDouble("pengurangan_biaya");
                    daftarLayanan.add(new SetrikaSaja(id, nama, harga, estimasi, proses, penguranganBiaya));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPelangganFromDatabase() {
        dataPelanggan.clear();
        String query = "SELECT p.*, u.username, u.password FROM pelanggan p JOIN user u ON p.id_user = u.id_user";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int idPelanggan = rs.getInt("id_pelanggan");
                String nama = rs.getString("nama");
                String noHP = rs.getString("no_hp");
                String alamat = rs.getString("alamat");
                int idUser = rs.getInt("id_user");
                String user = rs.getString("username");
                String pass = rs.getString("password");
                
                dataPelanggan.add(new Pelanggan(idPelanggan, nama, noHP, alamat, idUser, user, pass));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTransaksiFromDatabase() {
        dataTransaksi.clear();
        String query = "SELECT t.*, p.id_user, p.nama, p.no_hp, p.alamat, u.username, u.password, s.nama_status " +
                       "FROM transaksi t " +
                       "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                       "JOIN user u ON p.id_user = u.id_user " +
                       "JOIN status_laundry s ON t.id_status = s.id_status";
                       
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int idTrans = rs.getInt("id_transaksi");
                int idPel = rs.getInt("id_pelanggan");
                String namaPel = rs.getString("nama");
                String noHPPel = rs.getString("no_hp");
                String alamatPel = rs.getString("alamat");
                int idUserPel = rs.getInt("id_user");
                String usernamePel = rs.getString("username");
                String passwordPel = rs.getString("password");
                
                int idStatus = rs.getInt("id_status");
                String namaStatus = rs.getString("nama_status");
                
                double jumlahBayar = rs.getDouble("jumlah_bayar");
                String tanggal = rs.getString("tanggal");
                
                Pelanggan pel = new Pelanggan(idPel, namaPel, noHPPel, alamatPel, idUserPel, usernamePel, passwordPel);
                
                Transaksi trans = new Transaksi(idTrans, pel);
                trans.setTanggal(tanggal);
                trans.setStatusLaundry(new StatusLaundry(idStatus, namaStatus));
                trans.setJumlahBayar(jumlahBayar);
                
                String detailQuery = "SELECT d.*, l.nama_layanan, l.harga_per_kg, l.estimasi_hari, l.jenis_proses, l.tipe_layanan, l.biaya_tambahan, l.pengurangan_biaya " +
                                     "FROM detail_transaksi d " +
                                     "JOIN layanan l ON d.id_layanan = l.id_layanan " +
                                     "WHERE d.id_transaksi = ?";
                                     
                try (PreparedStatement detStmt = conn.prepareStatement(detailQuery)) {
                    detStmt.setInt(1, idTrans);
                    try (ResultSet detRs = detStmt.executeQuery()) {
                        while (detRs.next()) {
                            int idDetail = detRs.getInt("id_detail");
                            double berat = detRs.getDouble("berat");
                            int idLay = detRs.getInt("id_layanan");
                            String namaLay = detRs.getString("nama_layanan");
                            double hargaLay = detRs.getDouble("harga_per_kg");
                            int estimasiLay = detRs.getInt("estimasi_hari");
                            String prosesLay = detRs.getString("jenis_proses");
                            String tipeLay = detRs.getString("tipe_layanan");
                            
                            Layanan lay = null;
                            if ("CuciKering".equals(tipeLay)) {
                                lay = new CuciKering(idLay, namaLay, hargaLay, estimasiLay, prosesLay);
                            } else if ("CuciSetrika".equals(tipeLay)) {
                                double biayaTambahan = detRs.getDouble("biaya_tambahan");
                                lay = new CuciSetrika(idLay, namaLay, hargaLay, estimasiLay, prosesLay, biayaTambahan);
                            } else if ("SetrikaSaja".equals(tipeLay)) {
                                double penguranganBiaya = detRs.getDouble("pengurangan_biaya");
                                lay = new SetrikaSaja(idLay, namaLay, hargaLay, estimasiLay, prosesLay, penguranganBiaya);
                            }
                            
                            if (lay != null) {
                                DetailTransaksi detail = new DetailTransaksi(idDetail, berat, lay);
                                trans.getDetailList().add(detail);
                            }
                        }
                    }
                }
                
                dataTransaksi.add(trans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Admin loginAdmin(String username, String password) {
        String query = "SELECT a.id_admin, a.nama_admin, u.id_user, u.username, u.password " +
                       "FROM admin a " +
                       "JOIN user u ON a.id_user = u.id_user " +
                       "WHERE u.username = ? AND u.password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idAdmin = rs.getInt("id_admin");
                    String namaAdmin = rs.getString("nama_admin");
                    int idUser = rs.getInt("id_user");
                    String user = rs.getString("username");
                    String pass = rs.getString("password");
                    return new Admin(idAdmin, user, pass, namaAdmin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean savePelangganToDatabase(String nama, String noHP, String alamat, String username, String password) {
        String insertUser = "INSERT INTO user (username, password) VALUES (?, ?)";
        String insertPelanggan = "INSERT INTO pelanggan (id_user, nama, no_hp, alamat) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            int idUser = -1;
            try (PreparedStatement stmtUser = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, username);
                stmtUser.setString(2, password);
                stmtUser.executeUpdate();
                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        idUser = rs.getInt(1);
                    }
                }
            }
            
            if (idUser == -1) {
                conn.rollback();
                return false;
            }
            
            try (PreparedStatement stmtPel = conn.prepareStatement(insertPelanggan)) {
                stmtPel.setInt(1, idUser);
                stmtPel.setString(2, nama);
                stmtPel.setString(3, noHP);
                stmtPel.setString(4, alamat);
                stmtPel.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    private boolean saveTransaksiToDatabase(int idPelanggan, double berat, int idLayanan, double subtotal) {
        String insertTrans = "INSERT INTO transaksi (id_pelanggan, tanggal, id_status, jumlah_bayar) VALUES (?, ?, ?, ?)";
        String insertDetail = "INSERT INTO detail_transaksi (id_transaksi, id_layanan, berat, subtotal) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            int idTrans = -1;
            String tanggal = java.time.LocalDate.now().toString();
            int idStatus = 1; // Default "Diproses"
            
            try (PreparedStatement stmtTrans = conn.prepareStatement(insertTrans, Statement.RETURN_GENERATED_KEYS)) {
                stmtTrans.setInt(1, idPelanggan);
                stmtTrans.setString(2, tanggal);
                stmtTrans.setInt(3, idStatus);
                stmtTrans.setDouble(4, subtotal);
                stmtTrans.executeUpdate();
                try (ResultSet rs = stmtTrans.getGeneratedKeys()) {
                    if (rs.next()) {
                        idTrans = rs.getInt(1);
                    }
                }
            }
            
            if (idTrans == -1) {
                conn.rollback();
                return false;
            }
            
            try (PreparedStatement stmtDetail = conn.prepareStatement(insertDetail)) {
                stmtDetail.setInt(1, idTrans);
                stmtDetail.setInt(2, idLayanan);
                stmtDetail.setDouble(3, berat);
                stmtDetail.setDouble(4, subtotal);
                stmtDetail.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}
