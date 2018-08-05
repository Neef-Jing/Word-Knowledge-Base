

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sqlite.SQLiteException;

/**
 * Servlet implementation class StoreBook
 */
@WebServlet("/StoreBook")
public class StoreBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StoreBook() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession(true);
		//response.setHeader("Location", "http://localhost:8080/ServerletTest/Index");
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
		if(session.isNew()) {
			writer.println("<head>"
					+ "<meta http-equiv='refresh' content='1;url=/ServerletTest/Index'>"
					+ "</head>"
					+ "<p>ERROR WITH SESSION!</p>");	
		} else {
			String bookName = (String) session.getAttribute("bookName");
			String bookPath = (String) session.getAttribute("bookPath");
//			writer.println("Book Name: " + bookName);
//			writer.println("File Path: " + bookPath);
			System.out.println("Start to process, pleale wait.");
			DBOperation database = new DBOperation("/home/neef/Project/WordKnowledgeBase");
			WordGenerator gen = new WordGenerator(bookPath);
			String[] words = gen.GetUniqueWords();
			String bookWords = "";
			int insertCount = 0;
			for(String word: words) {
				System.out.println("Process Word: " + word);
				bookWords = bookWords + " " + word;
				if(database.InsertWord(word)) {
					insertCount ++;
					System.out.println("Inserted Words: " + insertCount);
				}
			}
			try {
				if(!database.BookExist(bookName)) {
					database.InsertBook(bookName, bookWords);
				}
			} catch (SQLiteException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			try {
					database.connection.close();
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
			}
			writer.println("<head>"
						+ "<meta http-equiv='refresh' content='1;url=/ServerletTest/Index'>"
						+ "</head>"
						+ "Done!");
			
		}
		
		
	}

}
