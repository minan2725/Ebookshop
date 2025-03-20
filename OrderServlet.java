import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.util.*;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the MIME type for the response message
        response.setContentType("text/html");
        // Get an output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();

        // Get parameters
        String name = request.getParameter("name"); //// This is the username
        String username = name;
        String email = request.getParameter("user_email");
        String phone = request.getParameter("user_phone");
        String[] bookIds = request.getParameterValues("id");

        // Begin HTML response
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Order Confirmation</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: floralwhite; margin: 0; padding: 0; }");
        out.println(
                ".header { background: linear-gradient(to right, #9702b0, #f9cafe); text-align: center; padding: 20px; font-size: 36px; font-weight: bold; color: black; }");
        out.println(
                ".container { width: 80%; margin: 20px auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        out.println(
                ".success { background-color: #dff0d8; color: #3c763d; padding: 20px; border-radius: 5px; margin-bottom: 20px; }");
        out.println(
                ".error { background-color: #f2dede; color: #a94442; padding: 20px; border-radius: 5px; margin-bottom: 20px; }");
        out.println(
                ".button { display: inline-block; padding: 10px 20px; background-color: #9702b0; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }");
        out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        // Header
        out.println("<div class=\"header\">Order Confirmation</div>");

        // Container
        out.println("<div class=\"container\">");

        if (bookIds == null || bookIds.length == 0) {
            // No books selected
            out.println("<div class=\"error\">");
            out.println("<h2>No Books Selected</h2>");
            out.println("<p>Your cart is empty. Please select books to order.</p>");
            out.println("</div>");
            out.println("<a href=\"main.html?username=" + username + "\" class=\"button\">Back to Shop</a>");
        } else {
            try (
                    // Get database connection
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                            "myuser", "xxxx"); // Update with your database credentials

                    // Create statements
                    Statement stmt = conn.createStatement()) {
                // Start transaction
                conn.setAutoCommit(false);

                try {
                    // Calculate total price
                    double totalPrice = 0.0;

                    // Process each book
                    for (String bookId : bookIds) {
                        // Get quantity for this book
                        String qtyParam = "qty_" + bookId;
                        int quantity = 1; // Default to 1

                        try {
                            quantity = Integer.parseInt(request.getParameter(qtyParam));
                        } catch (NumberFormatException | NullPointerException e) {
                            // Use default quantity 1
                        }

                        // Get book price
                        String priceQuery = "SELECT price FROM books WHERE id = " + bookId;
                        ResultSet priceRS = stmt.executeQuery(priceQuery);

                        if (priceRS.next()) {
                            double price = priceRS.getDouble("price");
                            totalPrice += price * quantity;
                        }

                        // Update book quantity in stock
                        String updateSql = "UPDATE books SET qty = qty - " + quantity + " WHERE id = " + bookId
                                + " AND qty >= " + quantity;
                        int updateCount = stmt.executeUpdate(updateSql);

                        if (updateCount == 0) {
                            // Not enough stock, rollback and show error
                            conn.rollback();

                            out.println("<div class=\"error\">");
                            out.println("<h2>Insufficient Stock</h2>");
                            out.println(
                                    "<p>Sorry, one or more books in your order do not have sufficient stock. Your order has not been processed.</p>");
                            out.println("</div>");
                            out.println(
                                    "<a href=\"viewCart?username=" + username + "\" class=\"button\">Back to Cart</a>");

                            return;
                        }
                    }

                    // Insert into order_records
                    String orderSql = "INSERT INTO order_records (user_name, user_email, user_phone, total_price) " +
                            "VALUES (?, ?, ?, ?)";
                    
                    // Declare orderId here
                    int orderId = -1;

                    try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setString(1, name); // username is used as the name
                        pstmt.setString(2, email);
                        pstmt.setString(3, phone);
                        pstmt.setDouble(4, totalPrice);

                        int orderInsert = pstmt.executeUpdate();

                        
                        ResultSet generatedKeys = pstmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            orderId = generatedKeys.getInt(1);
                        }

                        // Insert into order_items for each book
                        for (String bookId : bookIds) {
                            // Get quantity for this book
                            String qtyParam = "qty_" + bookId;
                            int quantity = 1; // Default to 1

                            try {
                                quantity = Integer.parseInt(request.getParameter(qtyParam));
                            } catch (NumberFormatException | NullPointerException e) {
                                // Use default quantity 1
                            }

                            // Get book price
                            String priceQuery = "SELECT price FROM books WHERE id = " + bookId;
                            ResultSet priceRS = stmt.executeQuery(priceQuery);

                            if (priceRS.next()) {
                                double price = priceRS.getDouble("price");

                                // Insert order item using PreparedStatement
                                String itemSql = "INSERT INTO order_items (order_id, book_id, qty, price) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                                    itemStmt.setInt(1, orderId);
                                    itemStmt.setInt(2, Integer.parseInt(bookId));
                                    itemStmt.setInt(3, quantity);
                                    itemStmt.setDouble(4, price);
                                    itemStmt.executeUpdate();
                                }
                            }
                        }
                    }

                    // Commit transaction
                    conn.commit();

                    // Clear the cart
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.removeAttribute("cart");
                    }

                    // Success message
                    out.println("<div class=\"success\">");
                    out.println("<h2>Order Successful!</h2>");
                    out.println("<p>Thank you for your order, " + name + ".</p>");
                    out.println("<p>Your order has been processed successfully.</p>");

                    if (orderId != -1) {
                        out.println("<p>Order ID: #" + orderId + "</p>");
                    }

                    out.println("<p>A confirmation email will be sent to " + email + ".</p>");
                    out.println("</div>");

                    // Order summary
                    out.println("<h3>Order Summary</h3>");
                    out.println("<table>");
                    out.println("<tr><th>Book</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

                    double displayTotal = 0.0;

                    // Display each ordered book
                    for (String bookId : bookIds) {
                        // Get quantity for this book
                        String qtyParam = "qty_" + bookId;
                        int quantity = 1; // Default to 1

                        try {
                            quantity = Integer.parseInt(request.getParameter(qtyParam));
                        } catch (NumberFormatException | NullPointerException e) {
                            // Use default quantity 1
                        }

                        // Get book details
                        String bookQuery = "SELECT title, price FROM books WHERE id = " + bookId;
                        ResultSet bookRS = stmt.executeQuery(bookQuery);

                        if (bookRS.next()) {
                            String title = bookRS.getString("title");
                            double price = bookRS.getDouble("price");
                            double itemTotal = price * quantity;
                            displayTotal += itemTotal;

                            out.println("<tr>");
                            out.println("<td>" + title + "</td>");
                            out.println("<td>" + quantity + "</td>");
                            out.println("<td>$" + String.format("%.2f", price) + "</td>");
                            out.println("<td>$" + String.format("%.2f", itemTotal) + "</td>");
                            out.println("</tr>");
                        }
                    }

                    out.println("<tr><td colspan=\"3\" style=\"text-align:right; font-weight:bold\">Total:</td><td>$"
                            + String.format("%.2f", displayTotal) + "</td></tr>");
                    out.println("</table>");

                    out.println(
                            "<a href=\"main.html?username=" + username + "\" class=\"button\">Continue Shopping</a>");

                } catch (SQLException ex) {
                    // Rollback on error
                    conn.rollback();
                    throw ex;
                } finally {
                    // Reset auto-commit
                    conn.setAutoCommit(true);
                }

            } catch (SQLException ex) {
                // Handle database errors
                out.println("<div class=\"error\">");
                out.println("<h2>Error Processing Order</h2>");
                out.println("<p>An error occurred while processing your order:</p>");
                out.println("<p>" + ex.getMessage() + "</p>");
                out.println("</div>");
                out.println("<a href=\"viewCart?username=" + username + "\" class=\"button\">Back to Cart</a>");
                ex.printStackTrace();
            }
        }

        out.println("</div>"); // Close container
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the view cart page
        response.sendRedirect("viewCart");
    }
}