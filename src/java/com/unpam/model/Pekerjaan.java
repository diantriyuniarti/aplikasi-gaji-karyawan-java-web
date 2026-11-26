package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.unpam.view.PesanDialog;

public class Pekerjaan {

    private String kodePekerjaan, namaPekerjaan;
    private int jumlahTugas;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getKodePekerjaan() {
        return kodePekerjaan;
    }

    public void setKodePekerjaan(String kodePekerjaan) {
        this.kodePekerjaan = kodePekerjaan;
    }

    public String getNamaPekerjaan() {
        return namaPekerjaan;
    }

    public void setNamaPekerjaan(String namaPekerjaan) {
        this.namaPekerjaan = namaPekerjaan;
    }

    public int getJumlahTugas() {
        return jumlahTugas;
    }

    public void setJumlahTugas(int jumlahTugas) {
        this.jumlahTugas = jumlahTugas;
    }

    public String getPesan() {
        return pesan;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection = null;

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "INSERT INTO tbpekerjaan(kodepekerjaan, namapekerjaan, jumlahtugas) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, kodePekerjaan);
                    stmt.setString(2, namaPekerjaan);
                    stmt.setInt(3, jumlahTugas);
                    int jumlahSimpan = stmt.executeUpdate();

                    if (jumlahSimpan < 1) {
                        adaKesalahan = true;
                        pesan = "Gagal menyimpan data pekerjaan.";
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat melakukan koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat menyimpan data pekerjaan:\n" + ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    adaKesalahan = true;
                    pesan = "Gagal menutup koneksi database:\n" + ex.getMessage();
                }
            }
        }

        return !adaKesalahan;
    }

    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection = null;
        list = new Object[0][0];

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "SELECT kodepekerjaan, namapekerjaan FROM tbpekerjaan LIMIT ?, ?";
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
                            list[i][0] = rset.getString("kodepekerjaan");
                            list[i][1] = rset.getString("namapekerjaan");
                            i++;
                        }
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat melakukan koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat membaca data pekerjaan:\n" + ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    adaKesalahan = true;
                    pesan = "Gagal menutup koneksi database:\n" + ex.getMessage();
                }
            }
        }

        return !adaKesalahan;
    }

    public boolean hapus(String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection = null;

        try {
            connection = koneksi.getConnection();
            if (connection != null) {
                String sql = "DELETE FROM tbpekerjaan WHERE kodepekerjaan=?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, kodePekerjaan);
                    int jumlahHapus = stmt.executeUpdate();

                    if (jumlahHapus < 1) {
                        adaKesalahan = true;
                        pesan = "Data pekerjaan tidak ditemukan.";
                    }
                }
            } else {
                adaKesalahan = true;
                pesan = "Tidak dapat melakukan koneksi ke server:\n" + koneksi.getPesanKesalahan();
            }
        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Terjadi kesalahan saat menghapus data pekerjaan:\n" + ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    adaKesalahan = true;
                    pesan = "Gagal menutup koneksi database:\n" + ex.getMessage();
                }
            }
        }

        return !adaKesalahan;
    }

    public boolean baca(String kodePekerjaanDipilih) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}