import java.io.IOException;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10 (Jakarta EE 9)
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "myuser";
    private static final String DB_PASSWORD = "xxxx";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            if (registerUser(username, password)) {
                // Redirect to index page with success message instead of creating a session
                response.sendRedirect("index.html?success=signup");
            } else {
                // Redirect back with error (username might already exist)
                response.sendRedirect("index.html?error=usernametaken");
            }
        } catch (SQLException e) {
            // Log the error
            getServletContext().log("Database error during signup", e);
            // Redirect with a database error message
            response.sendRedirect("index.html?error=database");
        }
    }
    
    private boolean registerUser(String username, String password) throws SQLException {
        // First check if username already exists
        String checkSql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
             
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Username already exists
                    return false;
                }
            }
            
            // Username doesn't exist, insert new user
            String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password); // Note: In a production app, you should hash passwords
                
                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0;
            }
        }
    }
}