import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.util.*;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Debug logging
        System.out.println("Received parameters:");
        System.out.println("id: " + request.getParameter("id"));
        System.out.println("username: " + request.getParameter("username"));
        System.out.println("X-Requested-With header: " + request.getHeader("X-Requested-With"));
       
        // Get parameters
        String bookId = request.getParameter("id");
        String username = request.getParameter("username");
        String returnUrl = request.getParameter("returnUrl");
        
        // Default return URL if not specified
        if (returnUrl == null || returnUrl.isEmpty()) {
            returnUrl = "main.html?username=" + username;
        }
        
        // Get the session
        HttpSession session = request.getSession(true);
        
        // Initialize the cart as a Map in the session if it doesn't exist
        @SuppressWarnings("unchecked")
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        
        try (
            // Get database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "myuser", "xxxx");  // Update with your database credentials
            
            // Prepare a statement
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM books WHERE id = ?")
        ) {
            if (bookId != null && !bookId.isEmpty()) {
                // Query the book details
                stmt.setString(1, bookId);
                ResultSet rset = stmt.executeQuery();
                
                if (rset.next()) {
                    // Book exists, add to cart
                    String title = rset.getString("title");
                    
                    // Update cart
                    int currentQty = cart.getOrDefault(bookId, 0);
                    cart.put(bookId, currentQty + 1);
                    
                    // Check if it's an AJAX request
                    String requestedWith = request.getHeader("X-Requested-With");
                    if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {
                        // For AJAX requests, return the cart size
                        response.setContentType("text/plain");
                        PrintWriter out = response.getWriter();
                        out.println(cart.size());
                        out.close();
                    } else {
                        // For normal requests, redirect back to the calling page with a success parameter
                        response.sendRedirect(returnUrl + (returnUrl.contains("?") ? "&" : "?") + "added=true");
                    }
                } else {
                    // Book not found
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                }
            } else {
                // No book ID provided
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No book ID provided");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + ex.getMessage());
        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}