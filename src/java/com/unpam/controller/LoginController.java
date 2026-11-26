package com.unpam.controller;

import com.unpam.model.Enkripsi;
import com.unpam.model.Karyawan;
import com.unpam.view.MainForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        String userName = (session.getAttribute("userName") != null)
                ? session.getAttribute("userName").toString()
                : "";

        String konten = "";
        String pesan = "";

        if (userName == null || userName.isEmpty()) {
            String userId = request.getParameter("userId");
            String password = request.getParameter("password");

            if (userId != null) {
                if (userId.trim().isEmpty()) {
                    pesan = "<br><font style='color:red'>User ID harus diisi</font>";
                } else {
                    Karyawan karyawan = new Karyawan();
                    Enkripsi enkripsi = new Enkripsi();

                    if (karyawan.baca(userId)) {
                        String passwordEncrypted = "";
                        try {
                            passwordEncrypted = enkripsi.hashMD5(password);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        if (passwordEncrypted.equals(karyawan.getPassword())) {
                            session.setAttribute("userName", karyawan.getNama().isEmpty() ? "No Name" : karyawan.getNama());

                            // Sidebar Menu
                            String menu = "<br><b>Master Data</b><br>"
                                    + "<a href=KaryawanController>Karyawan</a><br>"
                                    + "<a href=PekerjaanController>Pekerjaan</a><br><br>"
                                    + "<b>Transaksi</b><br>"
                                    + "<a href=GajiController>Gaji</a><br><br>"
                                    + "<b>Laporan</b><br>"
                                    + "<a href=LaporanGajiController>Gaji</a><br><br>"
                                    + "<a href=LogoutController>Logout</a><br><br>";
                            session.setAttribute("menu", menu);

                            // Top Menu
                            String topMenu = "<nav><ul>"
                                    + "<li><a href='.'>Home</a></li>"
                                    + "<li><a href='#'>Master Data</a><ul>"
                                    + "<li><a href=KaryawanController>Karyawan</a></li>"
                                    + "<li><a href=PekerjaanController>Pekerjaan</a></li>"
                                    + "</ul></li>"
                                    + "<li><a href='#'>Transaksi</a><ul>"
                                    + "<li><a href=GajiController>Gaji</a></li>"
                                    + "</ul></li>"
                                    + "<li><a href='#'>Laporan</a><ul>"
                                    + "<li><a href=LaporanGajiController>Gaji</a></li>"
                                    + "</ul></li>"
                                    + "<li><a href=LogoutController>Logout</a></li>"
                                    + "</ul></nav>";
                            session.setAttribute("topMenu", topMenu);

                            session.setMaxInactiveInterval(60 * 30); // 30 menit
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            pesan = "<br><font style='color:red'>User ID atau Password salah</font>";
                        }
                    } else {
                        pesan = "<br><font style='color:red'>User ID atau Password salah</font>";
                    }
                }
            }

            // Tampilkan ulang form login
            konten = "<br><form action='LoginController' method='post'>"
                    + "<table>"
                    + "<tr><td>User ID</td><td><input type='text' name='userId'></td></tr>"
                    + "<tr><td>Password</td><td><input type='password' name='password'></td></tr>"
                    + "<tr><td colspan='2' align='center'><input type='submit' value='Login'></td></tr>"
                    + "</table>"
                    + "</form>" + pesan;

        } else {
            // Sudah login → redirect ke halaman utama
            response.sendRedirect("index.jsp");
            return;
        }

        // Tampilkan halaman login dengan konten dan pesan
        new MainForm().tampilkan(konten, request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);  // ✅ dukung akses GET juga
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);  // ✅ dukung akses POST (login)
    }

    @Override
    public String getServletInfo() {
        return "Login Controller";
    }
}
