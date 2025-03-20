import java.io.IOException;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10 (Jakarta EE 9)
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "myuser";
    private static final String DB_PASSWORD = "xxxx";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            if (authenticateUser(username, password)) {
                // Create a session for the user
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Redirect to main page
                response.sendRedirect("main.html?username=" + username);
            } else {
                // Redirect back to login page with error
                response.sendRedirect("index.html?error=invalid");
            }
        } catch (SQLException e) {
            // Log the error
            getServletContext().log("Database error during login", e);
            // Redirect with a database error message
            response.sendRedirect("index.html?error=database");
        }
    }

    private boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Note: In a production app, you should use password hashing

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // If a result exists, login is successful
            }
        }
    }
}