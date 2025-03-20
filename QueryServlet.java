import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.util.Map;

@WebServlet("/query")
public class QueryServlet extends HttpServlet {

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get an output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      
      // Get query parameters
      String[] authors = request.getParameterValues("author");
      String priceRange = request.getParameter("price");
      String username = request.getParameter("username");

      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html lang=\"en\">");
      out.println("<head>");
      out.println("<meta charset=\"UTF-8\">");
      out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
      out.println("<title>Book Query Results - H&&M Bookstore</title>");
      out.println("<style>");
      // Copy CSS styles from main.html
      out.println("body {");
      out.println("    font-family: Arial, sans-serif;");
      out.println("    background-color: floralwhite;");
      out.println("    margin: 0;");
      out.println("    padding: 0;");
      out.println("    display: flex;");
      out.println("    flex-direction: column;");
      out.println("    min-height: 100vh;");
      out.println("}");
      
      out.println(".header {");
      out.println("    background: linear-gradient(to right, #9702b0, #f9cafe);");
      out.println("    text-align: center;");
      out.println("    padding: 20px;");
      out.println("    font-size: 48px;");
      out.println("    font-weight: bold;");
      out.println("}");
      
      out.println(".nav-bar {");
      out.println("    display: flex;");
      out.println("    justify-content: flex-end;");
      out.println("    padding: 10px 20px;");
      out.println("    background-color: floralwhite;");
      out.println("}");
      
      out.println(".nav-bar a {");
      out.println("    text-decoration: none;");
      out.println("    color: #9702b0;");
      out.println("    font-size: 18px;");
      out.println("    font-weight: bold;");
      out.println("    padding: 10px 15px;");
      out.println("    border-radius: 5px;");
      out.println("    background-color: white;");
      out.println("    transition: background-color 0.3s ease;");
      out.println("    margin-left: 10px;");
      out.println("}");
      
      out.println(".nav-bar a:hover {");
      out.println("    background-color: #9702b0;");
      out.println("    color: white;");
      out.println("}");
      
      // Container to hold book cards
      out.println(".container {");
      out.println("    width: 80%;");
      out.println("    margin: 20px auto;");
      out.println("    display: grid;");
      out.println("    grid-template-columns: repeat(3, 1fr);");
      out.println("    gap: 20px;");
      out.println("    padding: 20px 0;");
      out.println("    flex-grow: 1;");
      out.println("}");
      
      // Book card styling
      out.println(".book-card {");
      out.println("    background: white;");
      out.println("    padding: 20px;");
      out.println("    border-radius: 40px;");
      out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);");
      out.println("    text-align: center;");
      out.println("}");
      
      out.println(".book-card img {");
      out.println("    width: 60%;");
      out.println("    height: auto;");
      out.println("    border-radius: 10px;");
      out.println("}");
      
      out.println(".book-card h3 {");
      out.println("    font-size: 24px;");
      out.println("    margin: 10px 0 5px;");
      out.println("}");
      
      out.println(".book-card p {");
      out.println("    font-size: 16px;");
      out.println("    margin: 5px 0;");
      out.println("    color: #555;");
      out.println("}");
      
      out.println("p.price {");
      out.println("    font-size: 20px;");
      out.println("    font-weight: bold;");
      out.println("    margin-top: 5px;");
      out.println("}");
      
      // Button container
      out.println(".button-container {");
      out.println("    display: flex;");
      out.println("    justify-content: space-between;");
      out.println("    margin-top: 5px;");
      out.println("}");
      
      out.println(".add-to-cart, .favourites-button {");
      out.println("    border: none;");
      out.println("    padding: 10px;");
      out.println("    border-radius: 5px;");
      out.println("    cursor: pointer;");
      out.println("}");
      
      out.println(".add-to-cart {");
      out.println("    font-size: 16px;");
      out.println("    background-color: #FFDE00;");
      out.println("}");
      
      out.println(".add-to-cart:hover {");
      out.println("    background-color: darkgoldenrod;");
      out.println("    color: white;");
      out.println("}");
      
      out.println(".favourites-button {");
      out.println("    background-color: white;");
      out.println("    border: 2px solid #ff4d4d;");
      out.println("    color: #ff4d4d;");
      out.println("    border-radius: 50%;");
      out.println("    width: 35px;");
      out.println("    height: 35px;");
      out.println("    display: flex;");
      out.println("    align-items: center;");
      out.println("    justify-content: center;");
      out.println("    font-size: 16px;");
      out.println("}");
      
      out.println(".favourites-button:hover {");
      out.println("    background-color: #ff4d4d;");
      out.println("    color: white;");
      out.println("}");
      
      // Footer styling
      out.println(".footer {");
      out.println("    text-align: center;");
      out.println("    padding: 20px;");
      out.println("    background: linear-gradient(to right, #9702b0, #f9cafe);");
      out.println("    font-size: 24px;");
      out.println("    color: white;");
      out.println("}");
      
      // Notification styling
      out.println(".notification {");
      out.println("    position: fixed;");
      out.println("    top: 20px;");
      out.println("    left: 50%;");
      out.println("    transform: translateX(-50%);");
      out.println("    background-color: #4CAF50;");
      out.println("    color: white;");
      out.println("    padding: 15px;");
      out.println("    border-radius: 5px;");
      out.println("    box-shadow: 0 0 10px rgba(0,0,0,0.2);");
      out.println("    z-index: 1000;");
      out.println("    display: none;");
      out.println("}");
      
      // Query results specific styles
      out.println(".query-title {");
      out.println("    text-align: center;");
      out.println("    margin: 20px 0;");
      out.println("    color: #9702b0;");
      out.println("}");
      
      out.println(".back-button {");
      out.println("    display: block;");
      out.println("    width: 200px;");
      out.println("    margin: 20px auto;");
      out.println("    padding: 10px;");
      out.println("    background-color: #9702b0;");
      out.println("    color: white;");
      out.println("    text-align: center;");
      out.println("    text-decoration: none;");
      out.println("    border-radius: 5px;");
      out.println("}");
      
      out.println(".back-button:hover {");
      out.println("    background-color: #7a0290;");
      out.println("}");
      
      out.println("</style>");
      out.println("</head>");
      out.println("<body>");
      
      // Notification div for add to cart success
      out.println("<div id=\"notification\" class=\"notification\">Item added to cart successfully!</div>");
      
      // Header
      out.println("<div class=\"header\">&#x1F4D6; H&&M Bookstore - Search Results</div>");
      
      // Navigation Bar
      out.println("<div class=\"nav-bar\">");
      out.println("<a href=\"viewCart?username=" + username + "\" id=\"viewCartLink\">View Cart <span class=\"cart-badge\" id=\"cartCount\">0</span></a>");
      out.println("<a href=\"#favourites\">Favourites</a>");
      out.println("</div>");
      
      out.println("<h2 class=\"query-title\">Search Results</h2>");
      
      // Get cart count for the badge
      HttpSession session = request.getSession(false);
      int cartCount = 0;
      if (session != null) {
         @SuppressWarnings("unchecked")
         Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
         if (cart != null) {
            cartCount = cart.size();
         }
      }
      
      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");
         
         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
         StringBuilder sqlStr = new StringBuilder("SELECT * FROM books WHERE 1=1"); // 1=1 is always true
         
         // Add author filter if specified
         if (authors != null && authors.length > 0) {
            sqlStr.append(" AND author IN (");
            for (int i = 0; i < authors.length; i++) {
               if (i > 0) sqlStr.append(",");
               sqlStr.append("'").append(authors[i]).append("'");
            }
            sqlStr.append(")");
         }
         
         // Add price filter if specified and not "all"
         if (priceRange != null && !priceRange.isEmpty() && !priceRange.equals("all")) {
            sqlStr.append(" AND price < ").append(priceRange);
         }
         
         // Add condition to show only books with available stock
         sqlStr.append(" AND qty > 0 ORDER BY price DESC");
         
         ResultSet rset = stmt.executeQuery(sqlStr.toString());

         // Process the query result set
         int count = 0;
         out.println("<div class=\"container\">");
         
         while(rset.next()) {
            // Print a book card for each record
            out.println("<div class=\"book-card\">");

            // Use the cover_image_url from the database
            String coverUrl = rset.getString("cover_image_url");
            out.println("<img src='" + coverUrl + "' alt='Book Cover'>");

            out.println("<h3>" + rset.getString("title") + "</h3>");
            out.println("<p>by " + rset.getString("author") + "</p>");
            out.println("<p class=\"price\">$" + rset.getDouble("price") + "</p>");
            
            // Add buttons within a container like in main.html
            out.println("<div class=\"button-container\">");
            // Add to cart button with onclick event - using the same format as main.html
            out.println("<button onclick=\"addToCart(" + rset.getString("id") + ")\" class=\"add-to-cart\">Add to Cart</button>");
            // Add favorites button
            out.println("<button class=\"favourites-button\">&#x2764;&#xFE0F;</button>");
            out.println("</div>");
            
            out.println("</div>");
            count++;
         }
         
         out.println("</div>");
         
         // Add a back button to return to main page
         out.println("<a href='main.html?username=" + username + "' class='back-button'>Back to Book Shop</a>");
         
         // Footer
         out.println("<div class=\"footer\">Thank you for shopping with us!</div>");
         
      } catch(SQLException ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }
      
      // Add script for notification and add to cart functionality
      out.println("<script>");
      // Username variable
      out.println("var username = '" + username + "';");
      
      // Update cart count on page load
      out.println("window.onload = function() {");
      out.println("    document.getElementById('cartCount').textContent = '" + cartCount + "';");
      out.println("    // Check for success parameter to show notification");
      out.println("    const urlParams = new URLSearchParams(window.location.search);");
      out.println("    if (urlParams.get('added') === 'true') {");
      out.println("        showNotification();");
      out.println("    }");
      out.println("};");
      
      // Function to show notification - copied from main.html
      out.println("function showNotification() {");
      out.println("    const notification = document.getElementById('notification');");
      out.println("    notification.style.display = 'block';");
      out.println("    setTimeout(function() {");
      out.println("        notification.style.display = 'none';");
      out.println("    }, 3000);");
      out.println("}");
      
      // Function to add to cart - copied from main.html
      out.println("function addToCart(bookId) {");
      out.println("    console.log(`Sending request to add book ID: ${bookId}, Username: ${username}`);");
      out.println("    const params = new URLSearchParams();");
      out.println("    params.append('id', bookId);");
      out.println("    params.append('username', username || '');");
      out.println("    fetch('addToCart', {");
      out.println("        method: 'POST',");
      out.println("        body: params,");
      out.println("        headers: {");
      out.println("            'X-Requested-With': 'XMLHttpRequest',");
      out.println("            'Content-Type': 'application/x-www-form-urlencoded'");
      out.println("        }");
      out.println("    })");
      out.println("    .then(response => {");
      out.println("        if (response.ok) {");
      out.println("            return response.text();");
      out.println("        }");
      out.println("        throw new Error('Network response was not ok.');");
      out.println("    })");
      out.println("    .then(count => {");
      out.println("        console.log(`Received cart count: ${count}`);");
      out.println("        document.getElementById('cartCount').textContent = count;");
      out.println("        showNotification();");
      out.println("    })");
      out.println("    .catch(error => {");
      out.println("        console.error('Error adding to cart:', error);");
      out.println("    });");
      out.println("    return false;");
      out.println("}");
      
      out.println("</script>");
      
      out.println("</body></html>");
      out.close();
   }
}