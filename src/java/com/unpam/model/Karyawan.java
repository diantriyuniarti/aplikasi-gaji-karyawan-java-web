package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.unpam.view.PesanDialog;

public class Karyawan {
    private String ktp, nama, password;
    private int ruang;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    // Getter dan Setter
    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getRuang() {
        return ruang;
    }

    public void setRuang(int ruang) {
        this.ruang = ruang;
    }

    public String getPesan() {
        return pesan;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }

    // Simpan data karyawan
    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection = null;

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "INSERT INTO tbkaryawan(ktp, nama, ruang, password) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, ktp);
                    stmt.setString(2, nama);
                    stmt.setInt(3, ruang);
                    stmt.setString(4, password);
                    int jumlahSimpan = stmt.executeUpdate();

                    if (jumlahSimpan < 1) {
                        adaKesalahan = true;
                        pesan = "Gagal menyimpan data karyawan.";
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat menyimpan data:\n" + ex.getMessage();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal menutup koneksi:\n" + ex.getMessage();
            }
        }

        return !adaKesalahan;
    }

    // Baca list data karyawan
    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection = null;
        list = new Object[0][0];

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "SELECT ktp, nama FROM tbkaryawan LIMIT ?, ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, mulai);
                    stmt.setInt(2, jumlah);
                    try (ResultSet rset = stmt.executeQuery()) {
                        rset.last();
                        int baris = rset.getRow();
                        rset.beforeFirst();

                        list = new Object[baris][2];
                        int i = 0;
                        while (rset.next()) {
                            list[i][0] = rset.getString("ktp");
                            list[i][1] = rset.getString("nama");
                            i++;
                        }
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat membaca data:\n" + ex.getMessage();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal menutup koneksi database:\n" + ex.getMessage();
            }
        }

        return !adaKesalahan;
    }

    // Hapus data karyawan
    public boolean hapus(String ktp) {
        boolean adaKesalahan = false;
        Connection connection = null;

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "DELETE FROM tbkaryawan WHERE ktp=?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, ktp);
                    int jumlahHapus = stmt.executeUpdate();

                    if (jumlahHapus < 1) {
                        adaKesalahan = true;
                        pesan = "Data karyawan tidak ditemukan.";
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat menghapus data:\n" + ex.getMessage();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal menutup koneksi database:\n" + ex.getMessage();
            }
        }

        return !adaKesalahan;
    }

    // Baca data karyawan berdasarkan KTP (PENTING)
    public boolean baca(String ktpDipilih) {
        boolean adaKesalahan = false;
        Connection connection = null;

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "SELECT * FROM tbkaryawan WHERE ktp = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, ktpDipilih);
                    try (ResultSet rset = stmt.executeQuery()) {
                        if (rset.next()) {
                            this.ktp = rset.getString("ktp");
                            this.nama = rset.getString("nama");
                            this.ruang = rset.getInt("ruang");
                            this.password = rset.getString("password");
                        } else {
                            adaKesalahan = true;
                            pesan = "Karyawan dengan KTP " + ktpDipilih + " tidak ditemukan.";
                        }
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat membaca data:\n" + ex.getMessage();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal menutup koneksi:\n" + ex.getMessage();
            }
        }

        return !adaKesalahan;
    }
}
