package com.unpam.controller;

import com.unpam.model.Pekerjaan;
import com.unpam.view.MainForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PekerjaanController", urlPatterns = {"/PekerjaanController"})
public class PekerjaanController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        Pekerjaan pekerjaan = new Pekerjaan();

        String tombol = getParameter(request, "tombol", "");
        String kodePekerjaan = getParameter(request, "kodePekerjaan", "");
        String namaPekerjaan = getParameter(request, "namaPekerjaan", "");
        String jumlahTugas = getParameter(request, "jumlahTugas", "2");
        String mulaiParam = getParameter(request, "mulai", "0");
        String jumlahParam = getParameter(request, "jumlah", "10");
        String kodePekerjaanDipilih = getParameter(request, "kodePekerjaanDipilih", "");

        int mulai = parseOrDefault(mulaiParam, 0);
        int jumlah = parseOrDefault(jumlahParam, 10);
        String keterangan = "<br>";

        String userName = "";
        Object userAttr = session.getAttribute("userName");
        if (userAttr != null) {
            userName = userAttr.toString();
        }

        if (!userName.isEmpty()) {
            switch (tombol) {
                case "Simpan":
                    if (!kodePekerjaan.isEmpty()) {
                        pekerjaan.setKodePekerjaan(kodePekerjaan);
                        pekerjaan.setNamaPekerjaan(namaPekerjaan);
                        try {
                            pekerjaan.setJumlahTugas(Integer.parseInt(jumlahTugas));
                            if (pekerjaan.simpan()) {
                                kodePekerjaan = "";
                                namaPekerjaan = "";
                                jumlahTugas = "2";
                                keterangan = "Sudah tersimpan";
                            } else {
                                keterangan = "Gagal menyimpan:<br>" + pekerjaan.getPesan();
                            }
                        } catch (NumberFormatException ex) {
                            keterangan = "Jumlah tugas harus berupa angka!";
                        }
                    } else {
                        keterangan = "Gagal menyimpan, kode pekerjaan tidak boleh kosong";
                    }
                    break;
                case "Hapus":
                    if (!kodePekerjaan.isEmpty()) {
                        if (pekerjaan.hapus(kodePekerjaan)) {
                            kodePekerjaan = "";
                            namaPekerjaan = "";
                            jumlahTugas = "2";
                            keterangan = "Data sudah dihapus";
                        } else {
                            keterangan = "Gagal menghapus:<br>" + pekerjaan.getPesan();
                        }
                    } else {
                        keterangan = "Kode pekerjaan masih kosong";
                    }
                    break;
                case "Cari":
                    if (!kodePekerjaan.isEmpty()) {
                        if (pekerjaan.baca(kodePekerjaan)) {
                            kodePekerjaan = pekerjaan.getKodePekerjaan();
                            namaPekerjaan = pekerjaan.getNamaPekerjaan();
                            jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
                        } else {
                            keterangan = "Kode pekerjaan tersebut tidak ada";
                        }
                    } else {
                        keterangan = "Kode pekerjaan masih kosong";
                    }
                    break;
                case "Pilih":
                    if (!kodePekerjaanDipilih.isEmpty()) {
                        if (pekerjaan.baca(kodePekerjaanDipilih)) {
                            kodePekerjaan = pekerjaan.getKodePekerjaan();
                            namaPekerjaan = pekerjaan.getNamaPekerjaan();
                            jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
                        } else {
                            keterangan = "Kode pekerjaan tersebut tidak ada";
                        }
                    } else {
                        keterangan = "Tidak ada yang dipilih";
                    }
                    break;
            }
        }

        String kontenLihat = "";
        if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")) {
            if (tombol.equals("Sebelumnya")) {
                mulai = Math.max(0, mulai - jumlah);
            } else if (tombol.equals("Berikutnya")) {
                mulai += jumlah;
            }

            Object[][] listPekerjaan = pekerjaan.bacaData(mulai, jumlah) ? pekerjaan.getList() : null;
            if (listPekerjaan != null) {
                kontenLihat += "<tr><td colspan='2' align='center'><table>";
                for (int i = 0; i < listPekerjaan.length; i++) {
                    kontenLihat += "<tr>";
                    kontenLihat += "<td><input type='radio' name='kodePekerjaanDipilih' value='" + listPekerjaan[i][0] + "'" + (i == 0 ? " checked" : "") + "></td>";
                    kontenLihat += "<td>" + listPekerjaan[i][0] + "</td>";
                    kontenLihat += "<td>" + listPekerjaan[i][1] + "</td>";
                    kontenLihat += "</tr>";
                }
                kontenLihat += "</table></td></tr>";
                kontenLihat += "<tr><td colspan='2' align='center'><table><tr>";
                kontenLihat += "<td><input type='submit' name='tombol' value='Sebelumnya' style='width: 100px'></td>";
                kontenLihat += "<td><input type='submit' name='tombol' value='Pilih' style='width: 60px'></td>";
                kontenLihat += "<td><input type='submit' name='tombol' value='Berikutnya' style='width: 100px'></td>";
                kontenLihat += "</tr><tr>";
                kontenLihat += "<td>Mulai <input type='text' name='mulai' value='" + mulai + "' style='width: 40px'></td>";
                kontenLihat += "<td>Jumlah <select name='jumlah'>";
                for (int i = 1; i <= 10; i++) {
                    int val = i * 10;
                    kontenLihat += "<option value='" + val + "'" + (val == jumlah ? " selected" : "") + ">" + val + "</option>";
                }
                kontenLihat += "</select></td>";
                kontenLihat += "<td><input type='submit' name='tombol' value='Tampilkan' style='width: 90px'></td>";
                kontenLihat += "</tr></table></td></tr>";
            } else {
                keterangan = pekerjaan.getPesan();
            }
        }

        String konten = "<h2>Master Data Pekerjaan</h2>";
        konten += "<form action='PekerjaanController' method='post'>";
        konten += "<table>";
        konten += "<tr><td align='right'>Kode Pekerjaan</td><td><input type='text' name='kodePekerjaan' value='" + kodePekerjaan + "' maxlength='15' size='15'>";
        konten += "<input type='submit' name='tombol' value='Cari'></td></tr>";
        konten += "<tr><td align='right'>Nama Pekerjaan</td><td><input type='text' name='namaPekerjaan' value='" + namaPekerjaan + "' maxlength='30' size='30'></td></tr>";
        konten += "<tr><td align='right'>Jumlah Tugas</td><td><select name='jumlahTugas'>";
        for (int i = 2; i <= 6; i++) {
            konten += "<option value='" + i + "'" + (i == Integer.parseInt(jumlahTugas) ? " selected" : "") + ">" + i + "</option>";
        }
        konten += "</select></td></tr>";
        konten += "<tr><td colspan='2'><b>" + keterangan + "</b></td></tr>";
        konten += "<tr><td colspan='2' align='center'><table><tr>";
        konten += "<td><input type='submit' name='tombol' value='Simpan' style='width: 100px'></td>";
        konten += "<td><input type='submit' name='tombol' value='Hapus' style='width: 100px'></td>";
        konten += "<td><input type='submit' name='tombol' value='Lihat' style='width: 100px'></td>";
        konten += "</tr></table></td></tr>";
        konten += kontenLihat;
        konten += "</table></form>";

        new MainForm().tampilkan(konten, request, response);
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return (value != null) ? value : defaultValue;
    }

    private int parseOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet untuk mengelola data pekerjaan";
    }
}
