import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UploadBook
 */
@WebServlet("/UploadBook")
public class UploadBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String UPLOAD_DIR = "books";
	
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadBook() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(MEMORY_THRESHOLD);
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(MAX_FILE_SIZE);
			upload.setSizeMax(MAX_REQUEST_SIZE);
			upload.setHeaderEncoding("UTF-8");
			String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIR;
			File uploadDir = new File(uploadPath);
			if(!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			
			List<FileItem> formItem;
			try {
				formItem = upload.parseRequest(request);
				if (formItem != null && formItem.size() > 0) {
					for(FileItem item: formItem) {
						if(!item.isFormField()) {
							String fileName = new File(item.getName()).getName();
							String filePath = uploadPath + File.separator + fileName;
							File storeFile = new File(filePath);
							System.out.println(filePath);
							item.write(storeFile);
							request.setAttribute("message", "File Upload Success!");
							response.setContentType("text/html;charset=UTF-8");
							PrintWriter writer = response.getWriter();
							String bookName = fileName.replaceAll(".txt", "");
							writer.println("<head>"
									+ "<meta http-equiv='refresh' content='1;url=/ServerletTest/StoreBook'>"
									+ "</head>"
									+ "<p>Book '" + bookName + "' Upload Success! <br />"
											+ "File Path Is: " + filePath + "<br />"
											+ " Waiting For Processing...</p>");
							HttpSession session = request.getSession(true);
							session.setAttribute("bookName", bookName);
							session.setAttribute("bookPath", filePath);
							
						}
					}
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//request.getServletContext().getRequestDispatcher("./Index").forward(request, response);
			
		}
	}

}
