import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.util.*;

@WebServlet("/viewCart")
public class ViewCartServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the MIME type for the response message
        response.setContentType("text/html");
        // Get an output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();
        
        // Get parameters
        String username = request.getParameter("username");
        
        // Begin HTML response
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>View Cart - H&&M Bookstore</title>");
        out.println("<style>");
        // Copy CSS styles from cart.html
        out.println("body {");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    background-color: floralwhite;");
        out.println("    margin: 0;");
        out.println("    padding: 0;");
        out.println("    display: flex;");
        out.println("    flex-direction: column;");
        out.println("    height: 100vh;");
        out.println("}");
        out.println(".header {");
        out.println("    background: linear-gradient(to right, #9702b0, #f9cafe);");
        out.println("    text-align: center;");
        out.println("    padding: 20px;");
        out.println("    font-size: 36px;");
        out.println("    font-weight: bold;");
        out.println("    color: black;");
        out.println("    position: fixed;");
        out.println("    top: 0;");
        out.println("    left: 0;");
        out.println("    width: 100%;");
        out.println("    z-index: 1000;");
        out.println("}");
        out.println(".scrollable-content {");
        out.println("    flex-grow: 1;");
        out.println("    overflow-y: auto;");
        out.println("    margin-top: 120px;");
        out.println("    margin-bottom: 80px;");
        out.println("    padding: 20px;");
        out.println("}");
        out.println(".user-info {");
        out.println("    width: 80%;");
        out.println("    margin: 20px auto;");
        out.println("    background: white;");
        out.println("    padding: 15px;");
        out.println("    border-radius: 10px;");
        out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);");
        out.println("}");
        out.println(".user-info p {");
        out.println("    font-size: 16px;");
        out.println("    margin: 10px 0;");
        out.println("}");
        out.println(".user-info input {");
        out.println("    padding: 5px;");
        out.println("    width: 100%;");
        out.println("    font-size: 14px;");
        out.println("}");
        out.println(".cart-container {");
        out.println("    width: 80%;");
        out.println("    margin: 20px auto;");
        out.println("    background: white;");
        out.println("    padding: 20px;");
        out.println("    border-radius: 10px;");
        out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);");
        out.println("}");
        out.println(".cart-item {");
        out.println("    display: flex;");
        out.println("    align-items: center;");
        out.println("    justify-content: space-between;");
        out.println("    padding: 10px 0;");
        out.println("    border-bottom: 1px solid #ddd;");
        out.println("}");
        out.println(".cart-item img {");
        out.println("    width: 80px;");
        out.println("    height: auto;");
        out.println("    border-radius: 5px;");
        out.println("    margin-right: 10px;");
        out.println("}");
        out.println(".cart-item-details {");
        out.println("    flex-grow: 1;");
        out.println("}");
        out.println(".cart-item-details h4 {");
        out.println("    margin: 0;");
        out.println("    font-size: 18px;");
        out.println("}");
        out.println(".cart-item-details p {");
        out.println("    margin: 5px 0;");
        out.println("    font-size: 14px;");
        out.println("    color: #555;");
        out.println("}");
        out.println(".quantity-container {");
        out.println("    display: flex;");
        out.println("    align-items: center;");
        out.println("    gap: 10px;");
        out.println("    margin-bottom: 5px;");
        out.println("}");
        out.println(".quantity-btn {");
        out.println("    background-color: orange;");
        out.println("    border: none;");
        out.println("    padding: 5px 10px;");
        out.println("    cursor: pointer;");
        out.println("    font-size: 16px;");
        out.println("    border-radius: 5px;");
        out.println("}");
        out.println(".quantity-input {");
        out.println("    width: 30px;");
        out.println("    text-align: center;");
        out.println("    font-size: 16px;");
        out.println("    border: 1px solid #ddd;");
        out.println("}");
        out.println(".cart-item-price {");
        out.println("    font-size: 16px;");
        out.println("    font-weight: bold;");
        out.println("}");
        out.println(".cart-item input[type=\"checkbox\"] {");
        out.println("    margin-right: 10px;");
        out.println("}");
        out.println(".total-price {");
        out.println("    text-align: right;");
        out.println("    font-size: 20px;");
        out.println("    font-weight: bold;");
        out.println("    margin-top: 20px;");
        out.println("}");
        out.println(".order-button-container {");
        out.println("    display: flex;");
        out.println("    justify-content: flex-end;");
        out.println("    margin-top: 10px;");
        out.println("}");
        out.println(".order-button {");
        out.println("    background-color: #4CAF50;");
        out.println("    color: white;");
        out.println("    padding: 10px 20px;");
        out.println("    border: none;");
        out.println("    border-radius: 5px;");
        out.println("    cursor: pointer;");
        out.println("    font-size: 16px;");
        out.println("}");
        out.println(".order-button:hover {");
        out.println("    background-color: #45a049;");
        out.println("}");
        out.println(".footer {");
        out.println("    text-align: center;");
        out.println("    padding: 20px;");
        out.println("    background-color: #9702b0;");
        out.println("    color: white;");
        out.println("    font-size: 18px;");
        out.println("    position: fixed;");
        out.println("    bottom: 0;");
        out.println("    left: 0;");
        out.println("    width: 100%;");
        out.println("    z-index: 1000;");
        out.println("}");
        out.println(".empty-cart {");
        out.println("    text-align: center;");
        out.println("    padding: 20px;");
        out.println("}");
        out.println(".back-button {");
        out.println("    display: inline-block;");
        out.println("    background-color: #9702b0;");
        out.println("    color: white;");
        out.println("    padding: 10px 20px;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 5px;");
        out.println("    margin-top: 20px;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        // Header
        out.println("<div class=\"header\">&#128092; My cart in H&&M Bookstore</div>");
        
        // Scrollable content
        out.println("<div class=\"scrollable-content\">");
        
        // Get the session
        HttpSession session = request.getSession(false);
        
        // User info section
        out.println("<div class=\"user-info\">");
        out.println("<form id=\"checkout-form\" action=\"order\" method=\"post\">");
        out.println("<p>Name: <input type=\"text\" name=\"name\" value=\"" + (username != null ? username : "") + "\" required></p>");
        out.println("<p>Enter your Email: <input type=\"text\" name=\"user_email\" required></p>");
        out.println("<p>Enter your Phone Number: <input type=\"text\" name=\"user_phone\" required></p>");
        out.println("<input type=\"hidden\" name=\"username\" value=\"" + (username != null ? username : "") + "\">");
        out.println("</form>");
        out.println("</div>");
        
        // Cart container
        out.println("<div class=\"cart-container\">");
        
        if (session == null || session.getAttribute("cart") == null) {
            // Empty cart
            out.println("<div class=\"empty-cart\">");
            out.println("<h2>Your cart is empty</h2>");
            out.println("<p>You have no items in your shopping cart.</p>");
            out.println("<a href=\"main.html?username=" + username + "\" class=\"back-button\">Continue Shopping</a>");
            out.println("</div>");
        } else {
            // Get the cart from the session
            @SuppressWarnings("unchecked")
            Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
            
            if (cart.isEmpty()) {
                // Empty cart
                out.println("<div class=\"empty-cart\">");
                out.println("<h2>Your cart is empty</h2>");
                out.println("<p>You have no items in your shopping cart.</p>");
                out.println("<a href=\"main.html?username=" + username + "\" class=\"back-button\">Continue Shopping</a>");
                out.println("</div>");
            } else {
                double total = 0.0;
                
                try (
                    // Get database connection
                    Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "myuser", "xxxx");  // Update with your database credentials
                    
                    // Create statement
                    Statement stmt = conn.createStatement()
                ) {
                    // For each item in the cart
                    for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                        String bookId = entry.getKey();
                        int quantity = entry.getValue();
                        
                        // Get book details from database
                        ResultSet rset = stmt.executeQuery("SELECT * FROM books WHERE id = " + bookId);
                        
                        if (rset.next()) {
                            String title = rset.getString("title");
                            String author = rset.getString("author");
                            double price = rset.getDouble("price");
                            String coverUrl = rset.getString("cover_image_url");
                            
                            double itemTotal = price * quantity;
                            total += itemTotal;
                            
                            // Add book to order form
                            out.println("<input type=\"hidden\" form=\"checkout-form\" name=\"id\" value=\"" + bookId + "\">");
                            out.println("<input type=\"hidden\" form=\"checkout-form\" name=\"qty_" + bookId + "\" value=\"" + quantity + "\">");
                            
                            // Cart item
                            out.println("<div class=\"cart-item\" data-id=\"" + bookId + "\">");
                            out.println("<input type=\"checkbox\" class=\"cart-checkbox\" data-price=\"" + price + "\" checked>");
                            out.println("<img src=\"" + coverUrl + "\" alt=\"" + title + "\">");
                            out.println("<div class=\"cart-item-details\">");
                            out.println("<h4>" + title + "</h4>");
                            out.println("<p>by " + author + "</p>");
                            out.println("<div class=\"quantity-container\">");
                            out.println("<button class=\"quantity-btn decrease\" data-id=\"" + bookId + "\">-</button>");
                            out.println("<input type=\"text\" class=\"quantity-input\" value=\"" + quantity + "\" readonly>");
                            out.println("<button class=\"quantity-btn increase\" data-id=\"" + bookId + "\">+</button>");
                            out.println("</div>");
                            out.println("</div>");
                            out.println("<div class=\"cart-item-price\">$<span class=\"price\">" + String.format("%.2f", itemTotal) + "</span></div>");
                            out.println("</div>");
                        }
                    }
                    
                    // Total price
                    out.println("<div class=\"total-price\">Total: $<span id=\"total\">" + String.format("%.2f", total) + "</span></div>");
                    
                    // Order button
                    out.println("<div class=\"order-button-container\">");
                    out.println("<a href=\"main.html?username=" + username + "\" class=\"back-button\" style=\"margin-right: 10px;\">Continue Shopping</a>");
                    out.println("<button type=\"submit\" form=\"checkout-form\" class=\"order-button\">Order</button>");
                    out.println("</div>");
                    
                } catch (SQLException ex) {
                    out.println("<p>Error: " + ex.getMessage() + "</p>");
                    ex.printStackTrace();
                }
            }
        }
        
        out.println("</div>"); // End cart container
        out.println("</div>"); // End scrollable content
        
        // Footer
        out.println("<div class=\"footer\">Thank you for shopping with us!</div>");
        
        // JavaScript for cart functionality
        out.println("<script>");
        out.println("document.querySelectorAll(\".cart-item\").forEach((item) => {");
        out.println("    const decreaseBtn = item.querySelector(\".decrease\");");
        out.println("    const increaseBtn = item.querySelector(\".increase\");");
        out.println("    const quantityInput = item.querySelector(\".quantity-input\");");
        out.println("    const priceElement = item.querySelector(\".price\");");
        out.println("    const checkbox = item.querySelector(\".cart-checkbox\");");
        out.println("    const bookId = item.dataset.id;");
        out.println("    let basePrice = parseFloat(checkbox.dataset.price);");
        
        out.println("    function updateTotal() {");
        out.println("        let total = 0;");
        out.println("        document.querySelectorAll(\".cart-item\").forEach((item) => {");
        out.println("            const checkbox = item.querySelector(\".cart-checkbox\");");
        out.println("            const priceElement = item.querySelector(\".price\");");
        out.println("            if (checkbox.checked) {");
        out.println("                total += parseFloat(priceElement.textContent);");
        out.println("            }");
        out.println("        });");
        out.println("        document.getElementById(\"total\").textContent = total.toFixed(2);");
        out.println("    }");
        
        out.println("    function updateQuantity(newQuantity) {");
        out.println("        // Update cart in session");
        out.println("        fetch('updateCart?id=' + bookId + '&quantity=' + newQuantity + '&username=" + username + "', {");
        out.println("            method: 'POST'");
        out.println("        }).then(response => {");
        out.println("            if (response.ok) {");
        out.println("                if (newQuantity <= 0) {");
        out.println("                    // Remove item from display");
        out.println("                    item.remove();");
        out.println("                } else {");
        out.println("                    // Update display");
        out.println("                    quantityInput.value = newQuantity;");
        out.println("                    priceElement.textContent = (basePrice * newQuantity).toFixed(2);");
        out.println("                }");
        out.println("                updateTotal();");
        out.println("                ");
        out.println("                // Update hidden form field");
        out.println("                const qtyField = document.querySelector('input[name=\"qty_' + bookId + '\"]');");
        out.println("                if (qtyField) {");
        out.println("                    qtyField.value = newQuantity;");
        out.println("                }");
        out.println("            }");
        out.println("        });");
        out.println("    }");
        
        out.println("    increaseBtn.addEventListener(\"click\", () => {");
        out.println("        let quantity = parseInt(quantityInput.value);");
        out.println("        quantity++;");
        out.println("        updateQuantity(quantity);");
        out.println("    });");
        
        out.println("    decreaseBtn.addEventListener(\"click\", () => {");
        out.println("        let quantity = parseInt(quantityInput.value);");
        out.println("        if (quantity > 1) {");
        out.println("            quantity--;");
        out.println("            updateQuantity(quantity);");
        out.println("        } else {");
        out.println("            // Remove item if quantity becomes 0");
        out.println("            if (confirm('Remove this item from your cart?')) {");
        out.println("                updateQuantity(0);");
        out.println("            }");
        out.println("        }");
        out.println("    });");
        
        out.println("    checkbox.addEventListener(\"change\", updateTotal);");
        out.println("});");
        
        // Initialize total
        out.println("document.addEventListener('DOMContentLoaded', function() {");
        out.println("    const checkboxes = document.querySelectorAll('.cart-checkbox');");
        out.println("    if (checkboxes.length > 0) {");
        out.println("        // Trigger change event on first checkbox to calculate initial total");
        out.println("        checkboxes[0].dispatchEvent(new Event('change'));");
        out.println("    }");
        out.println("});");
        
        out.println("</script>");
        
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}