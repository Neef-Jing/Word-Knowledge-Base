

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RememberWords
 */
@WebServlet("/RememberWords")
public class RememberWords extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RememberWords() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession(true);
		DBOperation database = new DBOperation("/home/neef/Project/WordKnowledgeBase");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		//String[] wordList = database.GetWordNotRemembered(5);
		String sendList = "";
		String storeWords = "";
		if(!session.isNew()) {
			if(session.getAttributeNames() != null) {
				storeWords = (String) session.getAttribute("sendList");
				if(!storeWords.isEmpty()) {
					//writer.println(storeWords);
					String pattern = "[a-z]{2,}";
					Pattern result = Pattern.compile(pattern);
					Matcher finder = result.matcher(storeWords);
					while (finder.find()) {
						if(database.ChangeRemembered(finder.group())) {
							writer.println("<br />" + finder.group() + "<br />");
						}
						}
					
				}
			}
		}
		String[] wordList = database.GetWordNotRemembered(5);
		if(wordList.length == 0) {
			writer.println("<head>"	
				+ "<meta http-equiv='refresh' content='1;url=/ServerletTest/Index'>"
				+ "</head>"
				+ "<p>Words All Remembered!</p>");	
			session.invalidate();
		} else {
			writer.println("<p>Words To Remember: </p>");
			for(int i = 0; i < wordList.length; i++) {
				writer.println("<p>Word: </p>" + wordList[i]
						+ "<p>" + database.GetTranslation(wordList[i]) + "</p>");
				sendList = sendList + " " + wordList[i];
			}
			writer.println("<a href='/ServerletTest/RememberWords'>Go On</a>"
					+ "<a href='/ServerletTest/Index'>Return</a>");
			session.setAttribute("sendList", sendList);
		}
		try {
			database.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
