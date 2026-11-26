package com.unpam.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MainForm {

    public void tampilkan(String konten, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        String userName = "";

        String menu = "<br><b>Master Data</b><br>"
                + "<a href='KaryawanController'>Karyawan</a><br>"
                + "<a href='PekerjaanController'>Pekerjaan</a><br><br>"
                + "<b>Transaksi</b><br>"
                + "<a href='GajiController'>Gaji</a><br><br>"
                + "<b>Laporan</b><br>"
                + "<a href='LaporanGaji.jsp'>Gaji</a><br><br>"
                + "<a href='LoginController'>Login</a><br><br>";

        String topMenu = "<nav><ul>"
                + "<li><a href='MainForm'>Home</a></li>"
                + "<li><a href='#'>Master Data</a><ul>"
                + "<li><a href='KaryawanController'>Karyawan</a></li>"
                + "<li><a href='PekerjaanController'>Pekerjaan</a></li>"
                + "</ul></li>"
                + "<li><a href='#'>Transaksi</a><ul>"
                + "<li><a href='GajiController'>Gaji</a></li>"
                + "</ul></li>"
                + "<li><a href='#'>Laporan</a><ul>"
                + "<li><a href='LaporanGaji.jsp'>Gaji</a></li>"
                + "</ul></li>"
                + "<li><a href='LoginController'>Login</a></li>"
                + "</ul></nav>";

        if (!session.isNew()) {
            try {
                userName = session.getAttribute("userName").toString();
            } catch (Exception ex) {
                userName = "";
            }

            if (!((userName == null) || userName.equals(""))) {
                if (konten.equals("")) {
                    konten = "<br><h1>Selamat Datang</h1><h2>" + userName + "</h2>";
                }
            }
        }

        try {
            String sessionMenu = (String) session.getAttribute("menu");
            if (sessionMenu != null) {
                menu = sessionMenu;
            }
        } catch (Exception ex) {}

        try {
            String sessionTopMenu = (String) session.getAttribute("topMenu");
            if (sessionTopMenu != null) {
                topMenu = sessionTopMenu;
            }
        } catch (Exception ex) {}

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link href='style.css' rel='stylesheet' type='text/css'/>");
            out.println("<title>Informasi Gaji Karyawan</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"#808080\">");
            out.println("<center>");
            out.println("<table width='80%' bgcolor='#eeeeee'>");

            out.println("<tr><td colspan='2' align='center'><br>");
            out.println("<h2 style='margin-bottom:0px; margin-top:0px;'>Informasi Gaji Karyawan</h2>");
            out.println("<h1 style='margin-bottom:0px; margin-top:0px;'>PT Dian</h1>");
            out.println("<h4 style='margin-bottom:0px; margin-top:0px;'>Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten</h4><br>");
            out.println("</td></tr>");

            out.println("<tr height='400'>");
            out.println("<td width='200' align='center' valign='top' bgcolor='#eeffee'>");
            out.println("<div id='menu'>" + menu + "</div>");
            out.println("</td>");
            out.println("<td align='center' valign='top' bgcolor='#ffffff'>");
            out.println(topMenu + "<br>" + konten);
            out.println("</td></tr>");

            out.println("<tr><td colspan='2' align='center' bgcolor='#eeeeff'>");
            out.println("<small>");
            out.println("Copyright &copy; 2017 PT Dian<br>");
            out.println("Jl. Benosa No. 99 Pamulang, Tangerang Selatan, Banten<br>");
            out.println("</small>");
            out.println("</td></tr>");

            out.println("</table>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
