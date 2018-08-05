

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TranslateWords
 */
@WebServlet("/TranslateWords")
public class TranslateWords extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TranslateWords() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		DBOperation database = new DBOperation("/home/neef/Project/WordKnowledgeBase");
		WordTranslator wt = new WordTranslator();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		String[] ntList = database.GetNoTranslation(10);
		String WordList = "";
		for(int i = 0; i < ntList.length; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Translating Word: " + ntList[i]);
			String dict = wt.GetTranslation(ntList[i]);
			database.InsertTranslation(ntList[i], dict);
			WordList = WordList + " " +  ntList[i];
		}
		if(database.GetTotalDict() != database.GetTotalCount()) {
			try {
				database.connection.close();
				writer.println("<head>"
					+ "<meta http-equiv='refresh' content='1;url=/ServerletTest/TranslateWords'>"
					+ "</head>"
					+ "<p>Translated Words: " + WordList + "</p>"
					+ "<a href='/ServerletTest/Index'>Return</a>");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				database.connection.close();
				writer.println("<head>"
					+ "<meta http-equiv='refresh' content='1;url=/ServerletTest/Index'>"
					+ "</head>"
					+ "<p>Translate Words Done!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
