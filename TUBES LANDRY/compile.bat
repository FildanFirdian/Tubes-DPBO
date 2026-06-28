@echo off
echo ========================================
echo  Compiling Laundry App...
echo ========================================
set LIBS=lib\javafx.base.jar;lib\javafx.controls.jar;lib\javafx.graphics.jar;lib\javafx.fxml.jar;lib\mysql-connector-j-8.4.0.jar

if exist bin (
    rd /s /q bin
)
mkdir bin

javac -encoding UTF-8 --class-path "%LIBS%" -d bin ^
    src\actor\User.java ^
    src\actor\Admin.java ^
    src\actor\Pelanggan.java ^
    src\service\Layanan.java ^
    src\service\CuciKering.java ^
    src\service\CuciSetrika.java ^
    src\service\SetrikaSaja.java ^
    src\sistem\StatusLaundry.java ^
    src\sistem\DetailTransaksi.java ^
    src\sistem\Transaksi.java ^
    src\sistem\CetakDokumen.java ^
    src\sistem\Nota.java ^
    src\sistem\LaporanTransaksi.java ^
    src\sistem\SistemLaundry.java ^
    src\sistem\DBConnection.java ^
    src\sistem\DatabaseManager.java ^
    src\main\Launcher.java ^
    src\main\Main.java

if %ERRORLEVEL% EQU 0 (
    echo ========================================
    echo  Compile SUKSES! Jalankan run.bat
    echo ========================================
) else (
    echo ========================================
    echo  GAGAL! Periksa error di atas.
    echo ========================================
)
