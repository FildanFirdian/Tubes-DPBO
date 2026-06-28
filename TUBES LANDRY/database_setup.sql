-- ============================================================
-- SistemLaundry - Database Setup Script
-- Jalankan: mysql -u root < database_setup.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS sistemlaundry;
USE sistemlaundry;

-- ============================================================
-- TABEL USER (induk untuk admin & pelanggan)
-- ============================================================
CREATE TABLE IF NOT EXISTS user (
    id_user   INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(50) NOT NULL
);

-- ============================================================
-- TABEL ADMIN
-- ============================================================
CREATE TABLE IF NOT EXISTS admin (
    id_admin   INT AUTO_INCREMENT PRIMARY KEY,
    id_user    INT NOT NULL,
    nama_admin VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user(id_user)
);

-- ============================================================
-- TABEL PELANGGAN
-- ============================================================
CREATE TABLE IF NOT EXISTS pelanggan (
    id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
    id_user      INT NOT NULL,
    nama         VARCHAR(100) NOT NULL,
    no_hp        VARCHAR(20)  NOT NULL,
    alamat       TEXT         NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user(id_user)
);

-- ============================================================
-- TABEL LAYANAN (Single Table Inheritance untuk subclass)
-- ============================================================
CREATE TABLE IF NOT EXISTS layanan (
    id_layanan        INT PRIMARY KEY,
    nama_layanan      VARCHAR(100) NOT NULL,
    harga_per_kg      DOUBLE       NOT NULL,
    estimasi_hari     INT          NOT NULL,
    jenis_proses      VARCHAR(50)  NOT NULL,
    tipe_layanan      VARCHAR(50)  NOT NULL,  -- CuciKering / CuciSetrika / SetrikaSaja
    biaya_tambahan    DOUBLE DEFAULT 0,        -- Hanya untuk CuciSetrika
    pengurangan_biaya DOUBLE DEFAULT 0         -- Hanya untuk SetrikaSaja (diskon)
);

-- ============================================================
-- TABEL STATUS LAUNDRY
-- ============================================================
CREATE TABLE IF NOT EXISTS status_laundry (
    id_status   INT PRIMARY KEY,
    nama_status VARCHAR(50) NOT NULL
);

-- ============================================================
-- TABEL TRANSAKSI
-- ============================================================
CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_pelanggan INT NOT NULL,
    tanggal      VARCHAR(50) NOT NULL,
    id_status    INT NOT NULL,
    jumlah_bayar DOUBLE NOT NULL,
    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan),
    FOREIGN KEY (id_status)    REFERENCES status_laundry(id_status)
);

-- ============================================================
-- TABEL DETAIL TRANSAKSI
-- ============================================================
CREATE TABLE IF NOT EXISTS detail_transaksi (
    id_detail    INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_layanan   INT NOT NULL,
    berat        DOUBLE NOT NULL,
    subtotal     DOUBLE NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi),
    FOREIGN KEY (id_layanan)   REFERENCES layanan(id_layanan)
);

-- ============================================================
-- SEED DATA - Status
-- ============================================================
INSERT IGNORE INTO status_laundry (id_status, nama_status) VALUES
(1, 'Diproses'),
(2, 'Selesai'),
(3, 'Diambil');

-- ============================================================
-- SEED DATA - Layanan
-- ============================================================
INSERT IGNORE INTO layanan VALUES
(101, 'Cuci Kering Reguler', 6000, 2, 'Mesin Otomatis', 'CuciKering',  0,    0),
(102, 'Cuci Setrika Kilat',  9000, 1, 'Setrika Uap',    'CuciSetrika', 2000, 0),
(103, 'Setrika Hemat',       4000, 2, 'Manual',         'SetrikaSaja',  0,   0.1);

-- ============================================================
-- SEED DATA - Admin (password: admin123)
-- ============================================================
INSERT IGNORE INTO user (id_user, username, password) VALUES (1, 'admin', 'admin123');
INSERT IGNORE INTO admin (id_admin, id_user, nama_admin) VALUES (1, 1, 'Super Admin');
