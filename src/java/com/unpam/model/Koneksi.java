package com.unpam.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Kelas ini mengelola koneksi ke database MySQL.
 */
public class Koneksi {

    private static final String driver = "com.mysql.cj.jdbc.Driver"; 
    private static final String database = "jdbc:mysql://localhost:3306/dbaplikasigajikaryawan";

    private static final String user = "root"; // Username database
    private static final String password = ""; // Password database
    private Connection connection; // Objek koneksi database
    private String pesanKesalahan; // Pesan kesalahan jika terjadi masalah

    /**
     * Mendapatkan pesan kesalahan yang terjadi saat koneksi database.
     *
     * @return String berisi pesan kesalahan.
     */
    public String getPesanKesalahan() {
        return pesanKesalahan;
    }

    
    public Connection getConnection() {
        connection = null; // Reset koneksi
        pesanKesalahan = ""; // Reset pesan kesalahan

        // Mencoba memuat driver JDBC.
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            pesanKesalahan = "JDBC Driver tidak ditemukan atau rusak:\n" + ex;
            return null; // Penting untuk mengembalikan null jika driver tidak ditemukan
        }

        // Jika driver berhasil dimuat, mencoba membuat koneksi.
        if (pesanKesalahan.isEmpty()) { // Menggunakan isEmpty() untuk memeriksa string kosong
            try {
                connection = DriverManager.getConnection(database + "?user=" + user + "&password=" + password);
            } catch (SQLException ex) {
                pesanKesalahan = "Koneksi ke database " + database + " gagal:\n" + ex;
                connection = null; // Pastikan koneksi di-null-kan jika terjadi exception
            }
        }
        return connection;
    }
}
