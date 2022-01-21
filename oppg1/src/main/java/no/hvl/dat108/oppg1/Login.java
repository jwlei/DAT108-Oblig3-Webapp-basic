package no.hvl.dat108.oppg1;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Login", urlPatterns = "/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String password;
    int timeout;
    Vareliste vareliste;
    String excep;

    public void init() {
        password = getServletConfig().getInitParameter("password");
        timeout = Integer.parseInt(getServletConfig().getInitParameter("timeout"));
        vareliste = new Vareliste();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String alternativ = request.getParameter("alternativ");

        if (alternativ == null) {
            alternativ = "1";
        }

        if (alternativ.equals("1")) {
            String passwordPrompt = "Skriv passord: (Hint, det er \"passord\")";

            if (excep != null) {
                if (excep.equals("1")) {
                    passwordPrompt = "Feil passord, prøv igjen. (Hint, det er \"passord\")";
                    excep = "";
                } else if (excep.equals("2")) {
                    passwordPrompt = "Du må være innlogget.";
                    excep = "";
                } else if (excep.equals("3")) {
                    passwordPrompt = "Logget ut.";
                }
            }

            response.setContentType("text/html; charset=ISO-8859-1");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"ISO-8859-1\">");
            out.println("<title>Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<form action=\"" + "Login" + "?postalternativ=1" + "\" method=\"post\">");
            out.println("<fieldset>");
            out.println("<p>" + passwordPrompt + "<br><input type=\"password\" name=\"passord\" /></p>");
            out.println("<p><input type=\"submit\" value=\"Login\" /></p>");
            out.println("</fieldset>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } else if (alternativ.equals("2")) {
            HttpSession session = request.getSession(false);
            if (!Loginprosess.Erloggetinn(request)) {
                excep = "2";
                response.sendRedirect("Login" + "?alternativ=1");
            } else {
                String password = (String) session.getAttribute("password");
                response.setContentType("text/html; charset=ISO-8859-1");
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"ISO-8859-1\">");
                out.println("<title>Vareliste</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Vareliste</h1>");
                out.println("<h3>Her kan du lage din handleliste, legg til og fjern varer.</h3>");
                out.println("<form action=\"" + "Login" + "?postalternativ=2" + "\" method=\"post\">");
                out.println("<p><input type=\"submit\" value=\"Legg til\" name=\"leggtil\"> "
                        + "<input type=\"text\" name=\"vare\"></p></form>");
                //loggut
                out.println("<form action=\"" + "Login" + "?postalternativ=3" + "\" method=\"post\">");
                out.println("<p><input type=\"submit\" value=\"Logg ut\" name=\"loggut\">" + "</p></form>");
                out.println("<br>");

                for (Vare vare : vareliste.getVarer()) {
                    out.println("<form action =\"" + "Login" + "?postalternativ=2" + "\" method =\"post\">");
                    out.print("<p><input type=\"hidden\" name =\"slett\" value=\"" + vareliste.index(vare) + "\">");
                    out.print("<p><input type=\"submit\" value = \"Slett\" > ");
                    out.println(vare.GetVarenavn() + "</p></form>");
                }
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String postalternativ = request.getParameter("postalternativ");

        if (postalternativ.equals("1")) {
            String passord = request.getParameter("passord");

            if (!Loginprosess.valid(password, passord)) {
                excep = "1";
                response.sendRedirect("Login?alternativ=1");

            } else {
                Loginprosess.innlogg(request, timeout);
                response.sendRedirect("Login" + "?alternativ=2");
            }
        } else if (postalternativ.equals("2")) {
            HttpSession session = request.getSession(false);

            if (!Loginprosess.Erloggetinn(request)) {
                excep = "3";
                response.sendRedirect("Login" + "?alternativ=1");
            } else {
                String vareinput = request.getParameter("vare");
                String slettVare = request.getParameter("slett");
                if (slettVare != null) {
                    int doit = Integer.parseInt(Loginprosess.escapeHtml(slettVare));
                    synchronized (vareliste) {
                        vareliste.fjernVare(doit);
                        response.sendRedirect("Login" + "?alternativ=2");
                    }
                } else {
                    if ((vareinput != null) && (!vareinput.isEmpty())) {
                        String vareinput2 = Loginprosess.escapeHtml(vareinput);
                        if (vareinput2.matches("^\\S(.*)$")) {
                            synchronized (vareliste) {
                                vareliste.LeggTilVare(vareinput2);
                            }
                        }
                    }
                    response.sendRedirect("Login" + "?alternativ=2");
                }
            }
        }
    }
}
