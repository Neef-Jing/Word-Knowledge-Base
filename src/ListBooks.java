/*Get the list of all books.*/

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ListBooks
 */
@WebServlet("/ListBooks")
public class ListBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListBooks() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        DBOperation database = new DBOperation("/home/neef/Project/WordKnowledgeBase");
        int totalBookCount = database.GetTotalBook(); 
        String[] bookList = database.GetBookNames(totalBookCount);
        for(int i = 0; i < totalBookCount; i++) {
        	writer.println("<p>" + bookList[i] + "</p>");
        }
        writer.println("<a href='/ServerletTest/Index'>Return</a>");
        try {
			database.connection.close();
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
	}

}
