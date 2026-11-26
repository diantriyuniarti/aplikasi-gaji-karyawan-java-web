package com.unpam.controller;

import com.unpam.model.Karyawan;
import com.unpam.model.Pekerjaan;
import com.unpam.model.Gaji;
import com.unpam.view.MainForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "GajiController", urlPatterns = {"/GajiController"})
public class GajiController extends HttpServlet {

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String userName = (String) session.getAttribute("userName");

        if (userName == null || userName.isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        Karyawan karyawan = new Karyawan();
        Pekerjaan pekerjaan = new Pekerjaan();
        Gaji gaji = new Gaji();

        // Ambil parameter form
        String tombol = request.getParameter("tombol") != null ? request.getParameter("tombol") : "";
        String tombolKaryawan = request.getParameter("tombolKaryawan") != null ? request.getParameter("tombolKaryawan") : "";
        String tombolPekerjaan = request.getParameter("tombolPekerjaan") != null ? request.getParameter("tombolPekerjaan") : "";

        String ktp = request.getParameter("ktp") != null ? request.getParameter("ktp") : "";
        String namaKaryawan = request.getParameter("namaKaryawan") != null ? request.getParameter("namaKaryawan") : "";
        String ruang = request.getParameter("ruang") != null ? request.getParameter("ruang") : "";

        String kodePekerjaan = request.getParameter("kodePekerjaan") != null ? request.getParameter("kodePekerjaan") : "";
        String namaPekerjaan = request.getParameter("namaPekerjaan") != null ? request.getParameter("namaPekerjaan") : "";
        String jumlahTugas = request.getParameter("jumlahTugas") != null ? request.getParameter("jumlahTugas") : "";

        String gajibersih = request.getParameter("gajibersih") != null ? request.getParameter("gajibersih") : "";
        String gajikotor = request.getParameter("gajikotor") != null ? request.getParameter("gajikotor") : "";
        String tunjangan = request.getParameter("tunjangan") != null ? request.getParameter("tunjangan") : "";

        String keterangan = "<br>";

        // Tombol Karyawan
        if (tombolKaryawan.equals("Cari")) {
            if (!ktp.isEmpty()) {
                if (karyawan.baca(ktp)) {
                    ktp = karyawan.getKtp();
                    namaKaryawan = karyawan.getNama();
                    ruang = Integer.toString(karyawan.getRuang());
                } else {
                    keterangan = "<br><font color='red'>KTP tidak ditemukan.</font>";
                }
            } else {
                keterangan = "<br><font color='red'>KTP harus diisi.</font>";
            }
        } else if (tombolKaryawan.equals("Lihat")) {
            response.sendRedirect("LihatKaryawan.jsp");
            return;
        }

        // Tombol Pekerjaan
        if (tombolPekerjaan.equals("Cari")) {
            if (!kodePekerjaan.isEmpty()) {
                if (pekerjaan.baca(kodePekerjaan)) {
                    kodePekerjaan = pekerjaan.getKodePekerjaan();
                    namaPekerjaan = pekerjaan.getNamaPekerjaan();
                    jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
                } else {
                    keterangan = "<br><font color='red'>Kode pekerjaan tidak ditemukan.</font>";
                }
            } else {
                keterangan = "<br><font color='red'>Kode pekerjaan harus diisi.</font>";
            }
        } else if (tombolPekerjaan.equals("Lihat")) {
            response.sendRedirect("LihatPekerjaan.jsp");
            return;
        }

        // Tombol Simpan
        if (tombol.equals("Simpan")) {
            if (!ktp.isEmpty() && !kodePekerjaan.isEmpty()) {
                gaji.setKtp(ktp);
                Object[][] list = {{kodePekerjaan, gajibersih, gajikotor, tunjangan}};
                gaji.setListGaji(list);
                if (gaji.simpan()) {
                    keterangan = "<br><font color='green'>Data berhasil disimpan.</font>";
                    ktp = namaKaryawan = ruang = kodePekerjaan = namaPekerjaan = jumlahTugas = gajibersih = gajikotor = tunjangan = "";
                } else {
                    keterangan = "<br><font color='red'>" + gaji.getPesan() + "</font>";
                }
            } else {
                keterangan = "<br><font color='red'>KTP dan Kode Pekerjaan wajib diisi.</font>";
            }
        }

        // Tombol Hapus
        if (tombol.equals("Hapus")) {
            if (!ktp.isEmpty() && !kodePekerjaan.isEmpty()) {
                if (gaji.hapus(ktp, kodePekerjaan)) {
                    keterangan = "<br><font color='green'>Data berhasil dihapus.</font>";
                    ktp = namaKaryawan = ruang = kodePekerjaan = namaPekerjaan = jumlahTugas = gajibersih = gajikotor = tunjangan = "";
                } else {
                    keterangan = "<br><font color='red'>" + gaji.getPesan() + "</font>";
                }
            } else {
                keterangan = "<br><font color='red'>KTP dan Kode Pekerjaan wajib diisi.</font>";
            }
        }

        // Bangun tampilan HTML dalam layout dashboard
        StringBuilder konten = new StringBuilder();
        konten.append("<h2>Input Gaji Karyawan</h2>");
        konten.append("<form action='GajiController' method='post'><table>");

        konten.append("<tr><td align='right'>KTP</td><td>")
              .append("<input type='text' name='ktp' value='").append(escapeHtml(ktp)).append("' maxlength='15' style='width:120px;'> ")
              .append("<input type='submit' name='tombolKaryawan' value='Cari'> ")
              .append("<input type='submit' name='tombolKaryawan' value='Lihat'></td></tr>");

        konten.append("<tr><td align='right'>Ruang</td><td><input type='text' name='ruang' value='")
              .append(escapeHtml(ruang)).append("' readonly style='width:30px;'></td></tr>");

        konten.append("<tr><td align='right'>Kode Pekerjaan</td><td>")
              .append("<input type='text' name='kodePekerjaan' value='").append(escapeHtml(kodePekerjaan)).append("' style='width:80px;'> ")
              .append("<input type='submit' name='tombolPekerjaan' value='Cari'> ")
              .append("<input type='submit' name='tombolPekerjaan' value='Lihat'></td></tr>");

        konten.append("<tr><td align='right'>Nama Pekerjaan</td><td>")
              .append("<input type='text' name='namaPekerjaan' value='").append(escapeHtml(namaPekerjaan)).append("' readonly style='width:300px;'></td></tr>");

        konten.append("<tr><td align='right'>Jumlah Tugas</td><td>")
              .append("<input type='text' name='jumlahTugas' value='").append(escapeHtml(jumlahTugas)).append("' readonly style='width:50px;'></td></tr>");

        konten.append("<tr><td align='right'>Gaji Bersih</td><td>")
              .append("<input type='text' name='gajibersih' value='").append(escapeHtml(gajibersih)).append("' style='width:120px;'></td></tr>");

        konten.append("<tr><td align='right'>Gaji Kotor</td><td>")
              .append("<input type='text' name='gajikotor' value='").append(escapeHtml(gajikotor)).append("' style='width:120px;'></td></tr>");

        konten.append("<tr><td align='right'>Tunjangan</td><td>")
              .append("<input type='text' name='tunjangan' value='").append(escapeHtml(tunjangan)).append("' style='width:120px;'></td></tr>");

        konten.append("<tr><td></td><td>")
              .append("<input type='submit' name='tombol' value='Simpan'> ")
              .append("<input type='submit' name='tombol' value='Hapus'></td></tr>");

        konten.append("</table></form>");
        konten.append("<b>").append(keterangan).append("</b>");

        new MainForm().tampilkan(konten.toString(), request, response);
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
}
