package com.unpam.controller;

import com.unpam.model.Enkripsi;
import com.unpam.model.Karyawan;
import com.unpam.view.MainForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "KaryawanController", urlPatterns = {"/KaryawanController"})
public class KaryawanController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        Karyawan karyawan = new Karyawan();
        Enkripsi enkripsi = new Enkripsi();

        // Ambil parameter dari form
        String tombol = getParam(request, "tombol");
        String ktp = getParam(request, "ktp");
        String nama = getParam(request, "nama");
        String ruangStr = getParam(request, "ruang", "1");
        String password = getParam(request, "password");
        String mulaiParam = getParam(request, "mulai", "0");
        String jumlahParam = getParam(request, "jumlah", "10");
        String ktpDipilih = getParam(request, "ktpDipilih");

        int mulai = parseInt(mulaiParam, 0);
        int jumlah = parseInt(jumlahParam, 10);
        String keterangan = "<br>";

        String userName = (session.getAttribute("userName") != null)
                ? session.getAttribute("userName").toString() : "";

        if (!userName.isEmpty()) {
            try {
                switch (tombol) {
                    case "Simpan":
                        if (ktp.isEmpty() || nama.isEmpty() || ruangStr.isEmpty() || password.isEmpty()) {
                            keterangan = "Semua field harus diisi.";
                        } else {
                            try {
                                int ruang = Integer.parseInt(ruangStr);
                                String passwordEncrypted = enkripsi.HashMD5(password);

                                karyawan.setKtp(ktp);
                                karyawan.setNama(nama);
                                karyawan.setRuang(ruang);
                                karyawan.setPassword(passwordEncrypted);

                                if (karyawan.simpan()) {
                                    keterangan = "Data sudah tersimpan.";
                                    ktp = "";
                                    nama = "";
                                    ruangStr = "1";
                                    password = "";
                                } else {
                                    keterangan = karyawan.getPesan();
                                }
                            } catch (NumberFormatException e) {
                                keterangan = "Ruang harus berupa angka.";
                            }
                        }
                        break;

                    case "Hapus":
                        if (!ktp.isEmpty()) {
                            if (karyawan.hapus(ktp)) {
                                keterangan = "Data sudah dihapus.";
                                ktp = "";
                                nama = "";
                                ruangStr = "1";
                                password = "";
                            } else {
                                keterangan = karyawan.getPesan();
                            }
                        } else {
                            keterangan = "KTP masih kosong.";
                        }
                        break;

                    case "Cari":
                        if (!ktp.isEmpty()) {
                            if (karyawan.baca(ktp)) {
                                ktp = karyawan.getKtp();
                                nama = karyawan.getNama();
                                ruangStr = String.valueOf(karyawan.getRuang());
                                password = karyawan.getPassword();
                            } else {
                                keterangan = "KTP " + ktp + " tidak ditemukan.";
                                nama = "";
                                ruangStr = "1";
                                password = "";
                            }
                        } else {
                            keterangan = "KTP harus diisi.";
                        }
                        break;

                    case "Pilih":
                        if (!ktpDipilih.isEmpty()) {
                            if (karyawan.baca(ktpDipilih)) {
                                ktp = karyawan.getKtp();
                                nama = karyawan.getNama();
                                ruangStr = String.valueOf(karyawan.getRuang());
                                password = karyawan.getPassword();
                            } else {
                                keterangan = "KTP " + ktpDipilih + " tidak ditemukan.";
                            }
                        } else {
                            keterangan = "Tidak ada data yang dipilih.";
                        }
                        break;
                }
            } catch (Exception e) {
                keterangan = "Terjadi kesalahan: " + e.getMessage();
            }
        }

        // Tampilkan daftar karyawan jika tombol navigasi ditekan
        String kontenLihat = "";
        if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")) {
            kontenLihat = "<tr><td colspan='2' align='center'><table>";

            if (tombol.equals("Sebelumnya")) {
                mulai = Math.max(mulai - jumlah, 0);
            } else if (tombol.equals("Berikutnya")) {
                mulai += jumlah;
            }

            try {
                if (karyawan.bacaData(mulai, jumlah)) {
                    Object[][] list = karyawan.getList();
                    for (int i = 0; i < list.length; i++) {
                        kontenLihat += "<tr><td><input type='radio' name='ktpDipilih' value='" + list[i][0] + "'"
                                + (i == 0 ? " checked" : "") + "></td><td>" + list[i][0] + "</td><td>" + list[i][1] + "</td></tr>";
                    }
                } else {
                    keterangan = karyawan.getPesan();
                }
            } catch (Exception e) {
                keterangan = "Terjadi kesalahan: " + e.getMessage();
            }

            // Navigasi dan input
            kontenLihat += "</table></td></tr><tr><td colspan='2' align='center'><table><tr>"
                    + "<td><input type='submit' name='tombol' value='Sebelumnya' style='width: 100px'></td>"
                    + "<td><input type='submit' name='tombol' value='Pilih' style='width: 60px'></td>"
                    + "<td><input type='submit' name='tombol' value='Berikutnya' style='width: 100px'></td></tr>"
                    + "<tr><td>Mulai <input type='text' name='mulai' value='" + mulai + "' style='width: 40px'></td>"
                    + "<td>Jumlah <select name='jumlah'>";
            for (int i = 1; i <= 10; i++) {
                int val = i * 10;
                kontenLihat += "<option value='" + val + "'" + (jumlah == val ? " selected" : "") + ">" + val + "</option>";
            }
            kontenLihat += "</select></td>"
                    + "<td><input type='submit' name='tombol' value='Tampilkan' style='width: 80px'></td></tr>"
                    + "</table></td></tr>";
        }

        // Bangun tampilan HTML form
        String konten = "<h2>Master Data Karyawan</h2>"
                + "<form action='KaryawanController' method='post'><table>"
                + "<tr><td align='right'>KTP</td><td><input type='text' name='ktp' value='" + ktp + "' maxlength='15' size='15'>"
                + "<input type='submit' name='tombol' value='Cari'></td></tr>"
                + "<tr><td align='right'>Nama</td><td><input type='text' name='nama' value='" + nama + "' maxlength='30' size='30'></td></tr>"
                + "<tr><td align='right'>Ruang</td><td><select name='ruang'>";
        for (int i = 1; i <= 13; i++) {
            konten += "<option value='" + i + "'" + (Integer.parseInt(ruangStr) == i ? " selected" : "") + ">" + i + "</option>";
        }
        konten += "</select></td></tr>"
                + "<tr><td align='right'>Password</td><td><input type='password' name='password' value='" + password + "' maxlength='30' size='30'></td></tr>"
                + "<tr><td colspan='2'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll(":", ",") + "</b></td></tr>"
                + "<tr><td colspan='2' align='center'><table><tr>"
                + "<td><input type='submit' name='tombol' value='Simpan' style='width: 100px'></td>"
                + "<td><input type='submit' name='tombol' value='Hapus' style='width: 100px'></td>"
                + "<td><input type='submit' name='tombol' value='Lihat' style='width: 100px'></td>"
                + "</tr></table></td></tr>"
                + kontenLihat + "</table></form>";

        new MainForm().tampilkan(konten, request, response);
    }

    private String getParam(HttpServletRequest req, String key) {
        return getParam(req, key, "");
    }

    private String getParam(HttpServletRequest req, String key, String defaultValue) {
        String val = req.getParameter(key);
        return (val != null) ? val : defaultValue;
    }

    private int parseInt(String val, int defaultValue) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    public String getServletInfo() {
        return "Karyawan Controller Servlet";
    }
}
