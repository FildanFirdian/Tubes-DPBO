# 🧺 Sistem Manajemen Laundry
> Aplikasi manajemen laundry berbasis Java (JavaFX + MySQL) untuk mata kuliah DPBO.

---

## 📋 Daftar Isi
- [Persyaratan Sistem](#-persyaratan-sistem)
- [Cara Setup Database](#-cara-setup-database)
- [Cara Menjalankan](#-cara-menjalankan)
- [Struktur Proyek](#-struktur-proyek)
- [Fitur Aplikasi](#-fitur-aplikasi)
- [Akun Login Default](#-akun-login-default)
- [Troubleshooting](#-troubleshooting)

---

## ⚙ Persyaratan Sistem

| Komponen | Versi yang Digunakan | Keterangan |
|---|---|---|
| **JDK** | **25.0.1** (Oracle JDK) | Minimal JDK 17+ |
| **JavaFX** | **21.0.2** (Windows) | Sudah ada di folder `lib/` — **tidak perlu install** |
| **MySQL Connector/J** | **8.4.0** | Sudah ada di folder `lib/` — **tidak perlu install** |
| **MySQL Server** | 8.x (via Laragon/XAMPP) | Harus berjalan di `localhost:3306` |
| **OS** | Windows 10/11 | JavaFX yang disertakan hanya untuk Windows |

> **ℹ Info untuk pengguna Mac/Linux:** File JavaFX di folder `lib/` adalah versi **Windows**.  
> Download ulang versi yang sesuai dari https://gluonhq.com/products/javafx/ dan ganti file di `lib/`.

### Cek versi JDK kamu:
```bash
java -version
```
Output yang diharapkan: `java version "25.x.x"` atau minimal `java version "17.x.x"`

---

## 🗄 Cara Setup Database

### Langkah 1 — Pastikan MySQL berjalan
Buka **Laragon** atau **XAMPP** dan start MySQL service.

### Langkah 2 — Jalankan script SQL
Buka terminal / command prompt lalu jalankan:
```bash
mysql -u root < "TUBES LANDRY/database_setup.sql"
```

> Atau buka file `TUBES LANDRY/database_setup.sql` di **phpMyAdmin / MySQL Workbench** dan jalankan.

Script ini akan membuat:
- Database `sistemlaundry`
- Semua tabel yang diperlukan
- Data awal: status laundry, 3 jenis layanan, dan 1 akun admin

### Langkah 3 — Ubah password jika perlu
Jika password MySQL kamu **bukan kosong**, buka file:
```
TUBES LANDRY/src/sistem/DBConnection.java
```
Ubah baris:
```java
private static final String PASSWORD = "";  // ← ganti dengan password MySQL kamu
```

---

## ▶ Cara Menjalankan

### Cara 1: Gunakan file BAT (Paling Mudah ✅)
```bash
# Masuk ke folder TUBES LANDRY:
cd "TUBES LANDRY"

# Compile dulu:
compile.bat

# Lalu jalankan:
run.bat
```

### Cara 2: Manual dari terminal
```bash
# Dari dalam folder TUBES LANDRY:
set LIBS=lib\javafx.base.jar;lib\javafx.controls.jar;lib\javafx.graphics.jar;lib\javafx.fxml.jar

# Compile:
javac -encoding UTF-8 --class-path "%LIBS%;lib\mysql-connector-j-8.4.0.jar" -d bin (Get-ChildItem -Recurse src -Filter "*.java")

# Run:
java --module-path %LIBS% --add-modules javafx.controls,javafx.fxml -cp "bin;lib\mysql-connector-j-8.4.0.jar;%LIBS%" -Dprism.order=sw main.Launcher
```

### Cara 3: VS Code (Tekan F5)
Pastikan ekstensi **Extension Pack for Java** sudah terinstall di VS Code.  
Buka file `src/main/Launcher.java` → tekan **F5** → pilih konfigurasi **"Laundry App (Launcher)"**.

---

## 📁 Struktur Proyek

```
TUBES LANDRY/
├── src/
│   ├── actor/
│   │   ├── User.java          ← Abstract class: induk Admin & Pelanggan
│   │   ├── Admin.java         ← Admin: login + kelola sistem
│   │   └── Pelanggan.java     ← Pelanggan: data diri + riwayat transaksi
│   │
│   ├── service/
│   │   ├── Layanan.java       ← Abstract class: induk semua jenis layanan
│   │   ├── CuciKering.java    ← Layanan cuci + kering (rumus: berat × harga)
│   │   ├── CuciSetrika.java   ← Layanan cuci + setrika (+ biaya tambahan)
│   │   └── SetrikaSaja.java   ← Layanan setrika saja (- diskon %)
│   │
│   ├── sistem/
│   │   ├── DBConnection.java      ← Koneksi JDBC ke MySQL
│   │   ├── DatabaseManager.java   ← Semua operasi CRUD ke database
│   │   ├── SistemLaundry.java     ← Pengelola data in-memory (arsitektur awal)
│   │   ├── Transaksi.java         ← Model transaksi laundry
│   │   ├── DetailTransaksi.java   ← Model item dalam transaksi
│   │   ├── StatusLaundry.java     ← Model status pesanan
│   │   ├── LaporanTransaksi.java  ← Generator laporan transaksi
│   │   ├── Nota.java              ← Model nota/struk
│   │   └── CetakDokumen.java      ← Interface cetak dokumen
│   │
│   └── main/
│       ├── Main.java          ← Aplikasi JavaFX utama (UI + logika)
│       └── Launcher.java      ← Entry point (menghindari bug JavaFX class loading)
│
├── lib/
│   ├── javafx.base.jar        ← JavaFX 21.0.2 (Windows)
│   ├── javafx.controls.jar
│   ├── javafx.graphics.jar
│   ├── javafx.fxml.jar
│   ├── javafx.media.jar
│   ├── javafx.swing.jar
│   └── mysql-connector-j-8.4.0.jar  ← JDBC driver MySQL
│
├── bin/                       ← Output compile (.class files)
├── compile.bat                ← Script compile (Windows)
├── run.bat                    ← Script run (Windows)
└── database_setup.sql         ← Script setup database MySQL
```

---

## ✨ Fitur Aplikasi

### 🔐 Login Admin
- Autentikasi dari database MySQL
- Username & password divalidasi sebelum dikirim ke DB

### 👥 Kelola Pelanggan
| Fitur | Deskripsi |
|---|---|
| ➕ Tambah | Menambah pelanggan baru dengan validasi format (nama, HP, username min 3 karakter) |
| ✏ Edit | Mengubah data pelanggan yang dipilih di tabel |
| 🗑 Hapus | Menghapus pelanggan beserta **seluruh transaksinya** (dengan konfirmasi) |
| Klik baris | Otomatis mengisi form dari data tabel yang dipilih |

### 🧾 Transaksi Baru
- Pilih pelanggan dari dropdown (data dari DB)
- Pilih layanan: **Cuci Kering**, **Cuci Setrika**, atau **Setrika Saja**
- Input berat → preview biaya dengan tombol "Hitung"
- Simpan transaksi ke database dengan status awal **Diproses**

### 📋 Detail Transaksi
- Filter riwayat transaksi **per pelanggan**
- Klik baris transaksi → tampilkan detail item layanan di bawah
- Ubah status pesanan langsung dari tabel: **Diproses → Selesai → Diambil**

---

## 🔑 Akun Login Default

| Field | Nilai |
|---|---|
| Username | `admin` |
| Password | `admin123` |

---

## 🔧 Troubleshooting

### ❌ Error: `Communications link failure` / Koneksi database gagal
- Pastikan MySQL sudah berjalan (cek di Laragon/XAMPP)
- Periksa port MySQL: default `3306`
- Periksa password di `DBConnection.java`

### ❌ Error: `Access denied for user 'root'@'localhost'`
- Password MySQL kamu bukan kosong
- Ubah konstanta `PASSWORD` di `DBConnection.java`

### ❌ Error: `Table 'sistemlaundry.xxx' doesn't exist`
- Jalankan `database_setup.sql` terlebih dahulu (lihat langkah setup)

### ⚠ Layar hitam / aplikasi tidak muncul
- Program menggunakan **Software Rendering** (`-Dprism.order=sw`) yang sudah dikonfigurasi di `run.bat`
- Ini normal untuk environment tanpa GPU driver yang kompatibel dengan JavaFX

### ❌ Error: `Unsupported major.minor version`
- Versi JDK yang dipakai **lebih lama** dari versi waktu compile
- Pastikan JDK 17 atau lebih baru:
  ```bash
  java -version
  ```

### ❌ Error: `MySQL JDBC Driver tidak ditemukan`
- File `lib/mysql-connector-j-8.4.0.jar` mungkin terhapus
- Download ulang dari: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar
- Simpan di folder `lib/`

---

## 👥 Anggota Tim
*(Isi nama anggota tim kamu di sini)*

---

## 📚 Teknologi yang Digunakan
- **Java 25.0.1** (Oracle JDK)
- **JavaFX 21.0.2** — framework UI desktop
- **MySQL Connector/J 8.4.0** — JDBC driver
- **MySQL 8.x** — database server
