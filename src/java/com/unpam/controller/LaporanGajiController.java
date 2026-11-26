package com.unpam.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.unpam.model.Gaji;
import com.unpam.view.MainForm;

@WebServlet(name = "LaporanGajiController", urlPatterns = {"/LaporanGajiController"})
public class LaporanGajiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[][] formatTypeData = {
            {"PDF (Portable Document Format)", "pdf", "application/pdf"},
            {"XLSX (Microsoft Excel)", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"XLS (Microsoft Excel 97-2003)", "xls", "application/vnd.ms-excel"},
            {"DOCX (Microsoft Word)", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"ODT (OpenDocument Text)", "odt", "application/vnd.oasis.opendocument.text"},
            {"RTF (Rich Text Format)", "rtf", "text/rtf"}
        };

        HttpSession session = request.getSession(true);
        String userName = (session.getAttribute("userName") != null) ? session.getAttribute("userName").toString() : "";

        String tombol = request.getParameter("tombol") != null ? request.getParameter("tombol") : "";
        String opsi = request.getParameter("opsi") != null ? request.getParameter("opsi") : "";
        String ktp = request.getParameter("ktp") != null ? request.getParameter("ktp") : "";
        String ruang = request.getParameter("ruang") != null ? request.getParameter("ruang") : "0";
        String formatType = request.getParameter("formatType") != null ? request.getParameter("formatType") : formatTypeData[0][0];

        String keterangan = "<br>";
        int noType = 0;

        for (int i = 0; i < formatTypeData.length; i++) {
            if (formatTypeData[i][0].equals(formatType)) {
                noType = i;
                break;
            }
        }

        if (!userName.equals("")) {
            if (tombol.equals("Cetak")) {
                Gaji gaji = new Gaji();
                int ruangDipilih = 0;

                try {
                    ruangDipilih = Integer.parseInt(ruang);
                } catch (NumberFormatException e) {
                    ruangDipilih = 0;
                }

                boolean sukses = gaji.cetakLaporan(
                        opsi, ktp, ruangDipilih,
                        formatTypeData[noType][1],
                        getServletContext().getRealPath("reports/GajiReport.jrxml")
                );

                if (sukses) {
                    byte[] bytes = gaji.getPdfBytes();
                    response.setHeader("Content-Disposition", "inline; filename=GajiReport." + formatTypeData[noType][1]);
                    response.setContentType(formatTypeData[noType][2]);
                    response.setContentLength(bytes.length);

                    OutputStream out = response.getOutputStream();
                    out.write(bytes);
                    out.flush();
                    out.close();
                    return;
                } else {
                    keterangan = gaji.getPesan();
                }
            }

            // Tampilkan form laporan
            StringBuilder konten = new StringBuilder();
            konten.append("<h2>Laporan Gaji</h2>")
                  .append("<form action='LaporanGajiController' method='post'>")
                  .append("<table>");

            // Opsi KTP
            konten.append("<tr><td align='right'><input type='radio' name='opsi' value='KTP' ")
                  .append("KTP".equalsIgnoreCase(opsi) ? "checked" : "").append("></td>")
                  .append("<td>KTP</td><td><input type='text' name='ktp' value='").append(ktp).append("' maxlength='15' size='15'></td></tr>");

            // Opsi Ruang
            konten.append("<tr><td align='right'><input type='radio' name='opsi' value='ruang' ")
                  .append("ruang".equalsIgnoreCase(opsi) ? "checked" : "").append("></td>")
                  .append("<td>Ruang</td><td><select name='ruang'>");

            for (int i = 1; i <= 4; i++) {
                konten.append("<option value='").append(i).append("' ")
                      .append((Integer.toString(i).equals(ruang)) ? "selected" : "").append(">")
                      .append(i).append("</option>");
            }

            konten.append("</select></td></tr>");

            // Opsi Semua
            konten.append("<tr><td align='right'><input type='radio' name='opsi' value='Semua' ")
                  .append((opsi.equals("Semua") || opsi.equals("")) ? "checked" : "").append("></td>")
                  .append("<td>Semua</td><td></td></tr>");

            // Format
            konten.append("<tr><td>Format</td><td colspan='2'><select name='formatType'>");
            for (String[] format : formatTypeData) {
                konten.append("<option value='").append(format[0]).append("' ")
                      .append(format[0].equals(formatType) ? "selected" : "").append(">")
                      .append(format[0]).append("</option>");
            }
            konten.append("</select></td></tr>");

            // Pesan
            konten.append("<tr><td colspan='3'><b>").append(keterangan.replaceAll("\n", "<br>")).append("</b></td></tr>");

            // Tombol
            konten.append("<tr><td colspan='3' align='center'><input type='submit' name='tombol' value='Cetak' style='width:100px;'></td></tr>");

            konten.append("</table></form>");

            new MainForm().tampilkan(konten.toString(), request, response);

        } else {
            response.sendRedirect("LoginController");
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
}
