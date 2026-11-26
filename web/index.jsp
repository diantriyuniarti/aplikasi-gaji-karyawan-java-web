<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="style.css" rel="stylesheet" type="text/css"/>
        <title>Informasi Gaji Karyawan</title>
    </head>
    <body bgcolor="#808080">
        <%
            String menu = "<br><b>Master Data</b><br>"
                        + "<a href='KaryawanController'>Karyawan</a><br>"
                        + "<a href='PekerjaanController'>Pekerjaan</a><br>"
                        + "<b>Transaksi</b><br>"
                        + "<a href='#'>Gaji</a><br>"
                        + "<b>Laporan</b><br>"
                        + "<a href='#'>Gaji</a><br>"
                        + "<a href='LoginController'>Login</a><br>";

            String topMenu = "<nav><ul>"
                        + "<li><a href='index.jsp'>Home</a></li>"
                        + "<li><a href='#'>Master Data</a>"
                        + "<ul>"
                        + "<li><a href='KaryawanController'>Karyawan</a></li>"
                        + "<li><a href='PekerjaanController'>Pekerjaan</a></li>"
                        + "</ul>"
                        + "</li>"
                        + "<li><a href='#'>Transaksi</a>"
                        + "<ul>"
                        + "<li><a href='#'>Gaji</a></li>"
                        + "</ul>"
                        + "</li>"
                        + "<li><a href='#'>Laporan</a>"
                        + "<ul>"
                        + "<li><a href='#'>Gaji</a></li>"
                        + "</ul>"
                        + "</li>"
                        + "<li><a href='LoginController'>Login</a></li>"
                        + "</ul></nav>";

            String konten = "<br><h1>Selamat Datang</h1>";
            String userName = "";

            if (session != null && !session.isNew()) {
                try {
                    userName = session.getAttribute("userName").toString();
                } catch (Exception ex) {}

                if (!(userName == null || userName.equals(""))) {
                    konten += "<h2>" + userName + "</h2>";

                    try {
                        menu = session.getAttribute("menu").toString();
                    } catch (Exception ex) {}

                    try {
                        topMenu = session.getAttribute("topMenu").toString();
                    } catch (Exception ex) {}
                }
            }
        %>

        <center>
            <table width="80%" bgcolor="#eeeeee">
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <h2 style="margin-bottom:0px; margin-top:0px;">
                            Informasi Gaji Karyawan
                        </h2>
                        <h1 style="margin-bottom:0px; margin-top:0px;">
                            PT Dian
                        </h1>
                        <h4 style="margin-bottom:0px; margin-top:0px;">
                            Jl. Jalan Benosa No. 99 Pamulang, Tangerang Selatan, Banten
                        </h4>
                        <br>
                    </td>
                </tr>
                <tr height="400">
                    <td width="200" align="center" valign="top" bgcolor="#eeeeee">
                        <br>
                        <div id="menu">
                            <%=menu%>
                        </div>
                    </td>
                    <td align="center" valign="top" bgcolor="#ffffff">
                        <%=topMenu%>
                        <br>
                        <div>
                            <%=konten%>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center" bgcolor="#eeeeff">
                        <small>
                            Copyright &copy; 2025 PT Dian<br>
                            Jl. Jalan Benosa No. 99 Pamulang, Tangerang Selatan, Banten<br>
                        </small>
                    </td>
                </tr>
            </table>
        </center>
    </body>
</html>