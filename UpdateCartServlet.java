import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.util.*;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters
        String bookId = request.getParameter("id");
        String quantityStr = request.getParameter("quantity");
        
        if (bookId == null || bookId.isEmpty() || quantityStr == null || quantityStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int quantity = Integer.parseInt(quantityStr);
            
            // Get the session
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                // Get the cart from the session
                @SuppressWarnings("unchecked")
                Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
                
                if (cart != null) {
                    if (quantity <= 0) {
                        // Remove the item from the cart
                        cart.remove(bookId);
                    } else {
                        // Update the quantity
                        cart.put(bookId, quantity);
                    }
                    
                    // Send success response
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}