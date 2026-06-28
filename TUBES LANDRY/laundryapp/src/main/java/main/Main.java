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

import java.util.ArrayList;

public class Main extends Application {

    private Stage primaryStage;
    private Admin currentAdmin = new Admin(1, "admin", "admin123", "Super Admin");
    
    // Variabel penampung untuk menandai pelanggan mana yang sedang diedit
    private Pelanggan pelangganSedangDiedit = null; 
    
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
        
        // Seed Awal Data Layanan
        daftarLayanan.addAll(
            new CuciKering(101, "Cuci Kering Reguler", 6000, 2, "Mesin Otomatis"),
            new CuciSetrika(102, "Cuci Setrika Kilat", 9000, 1, "Setrika Uap", 2000),
            new SetrikaSaja(103, "Setrika Hemat", 4000, 2, "Manual", 0.10) // Diskon 10%
        );
        
        // Seed Awal Data Pelanggan
        dataPelanggan.add(new Pelanggan(1, "Budi Santoso", "0812345678", "Bandung", 10, "budi", "pwd"));

        showLoginScene();
    }

    private void showLoginScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("login-container");

        Label titleLabel = new Label("Washly System");
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
            if (currentAdmin.login(txtUsername.getText(), txtPassword.getText())) {
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
        Label welcomeLabel = new Label("Halo, " + currentAdmin.getNamaAdmin());
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

        // Membuat 3 Tombol Aksi (Simpan/Tambah, Edit, Hapus)
        Button btnAdd = new Button("Simpan Pelanggan");
        Button btnEdit = new Button("Edit Terpilih");
        Button btnHapus = new Button("Hapus Terpilih");
        
        // Memasukkan tombol ke dalam HBox agar berjejer rapi ke samping
        HBox layoutTombol = new HBox(10, btnAdd, btnEdit, btnHapus);
        formGrid.add(layoutTombol, 1, 3);

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

        // ========================================================
        //  AKSI TOMBOL SIMPAN (DUAL MODE: TAMBAH BARU / SIMPAN EDIT)
        // ========================================================
        btnAdd.setOnAction(e -> {
            String nama = txtNama.getText().trim();
            String hp = txtHP.getText().trim();
            String alamat = txtAlamat.getText().trim();

            if (nama.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nama pelanggan tidak boleh kosong!");
                alert.showAndWait();
                return;
            }

            if (pelangganSedangDiedit != null) {
                // === MODE EDIT DATA ===
                pelangganSedangDiedit.setNama(nama);
                pelangganSedangDiedit.setNoHP(hp);
                pelangganSedangDiedit.setAlamat(alamat);

                pelangganSedangDiedit = null; // Reset kembali penanda setelah selesai edit
                btnAdd.setText("Simpan Pelanggan"); // Kembalikan teks asli tombol
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data pelanggan berhasil diperbarui!");
                alert.showAndWait();
            } else {
                // === MODE TAMBAH BARU ===
                int newId = dataPelanggan.size() + 1;
                dataPelanggan.add(new Pelanggan(newId, nama, hp, alamat, newId, "user"+newId, "pass"));
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pelanggan baru berhasil ditambahkan!");
                alert.showAndWait();
            }

            // Sinkronisasi data ulang ke komponen UI
            table.refresh();
            txtNama.clear(); 
            txtHP.clear(); 
            txtAlamat.clear();
        });

        // ========================================================
        //  AKSI TOMBOL EDIT
        // ========================================================
        btnEdit.setOnAction(e -> {
            Pelanggan dipilih = table.getSelectionModel().getSelectedItem();
            
            if (dipilih != null) {
                pelangganSedangDiedit = dipilih; // Tandai data objek pelanggan yang diklik
                
                // Salin data objek ke dalam TextField inputan
                txtNama.setText(dipilih.getNama());
                txtHP.setText(dipilih.getNoHP());
                txtAlamat.setText(dipilih.getAlamat());
                
                // Beri penanda tekstual pada tombol utama
                btnAdd.setText("Simpan Perubahan");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih salah satu baris pelanggan di tabel terlebih dahulu!");
                alert.showAndWait();
            }
        });

        // ========================================================
        //  AKSI TOMBOL HAPUS
        // ========================================================
        btnHapus.setOnAction(e -> {
            Pelanggan dipilih = table.getSelectionModel().getSelectedItem();
            
            if (dipilih != null) {
                dataPelanggan.remove(dipilih); // Hapus objek data dari ObservableList database
                table.refresh();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data pelanggan berhasil dihapus!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih data pelanggan di tabel yang ingin dihapus!");
                alert.showAndWait();
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
                    Transaksi trans = new Transaksi(dataTransaksi.size() + 1001, pel);
                    DetailTransaksi detail = new DetailTransaksi(trans.getIdTransaksi(), berat, lay);
                    trans.tambahDetail(detail);
                    dataTransaksi.add(trans);
                    DetailTransactionBuilder(trans, berat, lay);
                    dataTransaksi.add(trans);
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaksi Berhasil Ditambahkan!");
                    alert.showAndWait();
                    txtBerat.clear();
                    lblSubtotal.setText("Subtotal: Rp0");
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
}