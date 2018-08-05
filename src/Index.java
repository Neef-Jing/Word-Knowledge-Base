/*First page of the webserver. It dose this: 
 * - Displays total words in the database.
 * - Displays translated words and the link to translate.
 * - Displays remembered words and the link to remember word.
 * - Displays generated books in the database.
 * - Upload books to generate words.*/

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBOperation database = new DBOperation("/home/neef/Project/WordKnowledgeBase");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.println("<h1>Word Knowledge Base</h1>"
			+ "<p>Total Words In Database: " + database.GetTotalCount() + "</p>"
			+ "<p>Translated Words In Database: " + database.GetTotalDict() + "</p>"
			+ "<a href='/ServerletTest/TranslateWords'>Translate Words</a>"
			+ "<p>Remembered Words In Database: " + database.GetTotalRemembered() + "</p>"
			+ "<a href='/ServerletTest/RememberWords'>Remember Words</a>"
			+ "<p>Books In Knowledge Base: " + database.GetTotalBook() + "</p>"
			+ "<a href='/ServerletTest/ListBooks'>List Books</a>"
			+ "<form method='post' action='/ServerletTest/UploadBook' enctype='multipart/form-data'>" + 
			"    Select A File:" + 
			"    <input type='file' name='book' />" + 
			"    <br/><br/>" + 
			"    <input type='submit' value='Upload' />");
		try {
			database.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
