CREATE DATABASE IF NOT EXISTS sistemlaundry;
USE sistemlaundry;

-- 1. Table: user
CREATE TABLE IF NOT EXISTS user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- 2. Table: admin
CREATE TABLE IF NOT EXISTS admin (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    nama_admin VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

-- 3. Table: pelanggan
CREATE TABLE IF NOT EXISTS pelanggan (
    id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    nama VARCHAR(100) NOT NULL,
    no_hp VARCHAR(20) NOT NULL,
    alamat TEXT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

-- 4. Table: layanan
CREATE TABLE IF NOT EXISTS layanan (
    id_layanan INT PRIMARY KEY,
    nama_layanan VARCHAR(100) NOT NULL,
    harga_per_kg DOUBLE NOT NULL,
    estimasi_hari INT NOT NULL,
    jenis_proses VARCHAR(50) NOT NULL,
    tipe_layanan VARCHAR(50) NOT NULL, -- 'CuciKering', 'CuciSetrika', 'SetrikaSaja'
    biaya_tambahan DOUBLE DEFAULT 0,
    pengurangan_biaya DOUBLE DEFAULT 0
);

-- 5. Table: status_laundry
CREATE TABLE IF NOT EXISTS status_laundry (
    id_status INT PRIMARY KEY,
    nama_status VARCHAR(50) NOT NULL
);

-- 6. Table: transaksi
CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi INT PRIMARY KEY,
    id_pelanggan INT NOT NULL,
    tanggal VARCHAR(50) NOT NULL,
    id_status INT NOT NULL,
    jumlah_bayar DOUBLE NOT NULL,
    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    FOREIGN KEY (id_status) REFERENCES status_laundry(id_status)
);

-- 7. Table: detail_transaksi
CREATE TABLE IF NOT EXISTS detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_layanan INT NOT NULL,
    berat DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi) ON DELETE CASCADE,
    FOREIGN KEY (id_layanan) REFERENCES layanan(id_layanan)
);

-- Seed Data
-- Default Admin
INSERT INTO user (id_user, username, password) VALUES (1, 'admin', 'admin123')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO admin (id_admin, id_user, nama_admin) VALUES (1, 1, 'Super Admin')
ON DUPLICATE KEY UPDATE nama_admin=nama_admin;

-- Default Pelanggan
INSERT INTO user (id_user, username, password) VALUES (10, 'budi', 'pwd')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO pelanggan (id_pelanggan, id_user, nama, no_hp, alamat) VALUES (1, 10, 'Budi Santoso', '0812345678', 'Bandung')
ON DUPLICATE KEY UPDATE nama=nama;

-- Default Layanan
INSERT INTO layanan (id_layanan, nama_layanan, harga_per_kg, estimasi_hari, jenis_proses, tipe_layanan, biaya_tambahan, pengurangan_biaya)
VALUES 
(101, 'Cuci Kering Reguler', 6000, 2, 'Mesin Otomatis', 'CuciKering', 0, 0),
(102, 'Cuci Setrika Kilat', 9000, 1, 'Setrika Uap', 'CuciSetrika', 2000, 0),
(103, 'Setrika Hemat', 4000, 2, 'Manual', 'SetrikaSaja', 0, 0.10)
ON DUPLICATE KEY UPDATE nama_layanan=VALUES(nama_layanan);

-- Default Statuses
INSERT INTO status_laundry (id_status, nama_status) VALUES
(1, 'Diproses'),
(2, 'Selesai'),
(3, 'Diambil')
ON DUPLICATE KEY UPDATE nama_status=VALUES(nama_status);
