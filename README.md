# Sistem Informasi Penggajian Karyawan Berbasis Java Web

Teknologi: Java Server Pages (JSP), Java Servlets, dan JasperReports  
Pengembang: Dian Tri Yuniarti  
Program Studi S1 Teknik Informatika, Universitas Pamulang

---

## Deskripsi Sistem

Aplikasi web enterprise berskala menengah untuk mengelola proses administrasi kompensasi dan penggajian karyawan. Sistem ini menangani pendataan karyawan, kalkulasi gaji pokok dan tunjangan secara otomatis, pemotongan pajak dan iuran, serta penerbitan slip gaji dan rekapitulasi laporan menggunakan generator laporan JasperReports (iReport).

---

## Fitur Utama

1. Manajemen Master Data Karyawan
   * Pendataan NIK, nama lengkap, departemen, jabatan, golongan, dan status keluarga.

2. Kalkulasi Penggajian Otomatis
   * Perhitungan otomatis komponen gaji: gaji pokok, tunjangan jabatan, tunjangan kehadiran, lembur, dan potongan kehadiran.

3. Cetak Slip Gaji dan Laporan Rekapitulasi
   * Integrasi JasperReports (`reports/`) untuk ekspor dokumen slip gaji individual dan rekap gaji bulanan berformat PDF.

4. Koneksi Basis Data Terpusat
   * Interaksi basis data MySQL melalui driver JDBC `mysql-connector-j-8.0.31.jar`.

---

## Arsitektur Teknologi

* Bahasa Pemrograman: Java EE (Servlet & JSP)
* Framework Pelaporan: JasperReports / iReport
* Web Server / Servlet Container: Apache Tomcat / GlassFish
* Basis Data: MySQL
* IDE: Apache NetBeans

---

## Panduan Menjalankan

1. Buka Apache NetBeans IDE.
2. Pastikan web server lokal (Apache Tomcat atau GlassFish) dan MySQL service pada XAMPP telah aktif.
3. Buka proyek `aplikasi-gaji-karyawan-java-web`.
4. Lakukan proses Clean and Build pada proyek.
5. Jalankan aplikasi (Run Project) untuk membuka antarmuka di browser lokal.
