/*Some datatabse operations.*/
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sqlite.SQLiteException;

import java.sql.*;

public class DBOperation {
	String databaseName;
	Connection connection = null;
	Statement statement = null;
	int totalCount;
	int totalBook;
	int totalDict;
	int totalRemembered;
	public DBOperation (String dbname) {
		//Connect Database and update some numbers.
		databaseName = dbname;
		try {
	    	Class.forName("org.sqlite.JDBC");
	    	connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".db");
	    	statement = connection.createStatement();
	    	statement.executeUpdate(
	    			"CREATE TABLE IF NOT EXISTS Words ("
	    			+ "Word TEXT PRIMARY KEY NOT NULL,"
	    			+ "Translation TEXT NOT NULL,"
	    			+ "Status INT NOT NULL);");
	    	statement.executeUpdate(
	    			"CREATE TABLE IF NOT EXISTS Book ("
	    			+ "BookName TEXT PRIMARY KEY NOT NULL,"
	    			+ "Words TEXT NOT NULL);");
	    	totalCount = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words;").getInt("TOTAL");
	    	totalDict = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words WHERE Translation IS NOT '--NULL--';").getInt("TOTAL");
	    	totalRemembered = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words WHERE Status IS NOT 0;").getInt("TOTAL");
	    	totalBook = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Book;").getInt("TOTAL");
	    } catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	}
	public boolean InsertWord (String word) {
		//Insert a word to the database.
		boolean success = false;
		try {
			statement.executeUpdate(
					"INSERT INTO Words "
					+ "(Word, Translation, Status)"
					+ "VALUES "
					+ "('" + word + "', '--NULL--', 0);");
			totalCount = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words;").getInt("TOTAL");
			totalRemembered = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words WHERE Status IS NOT 0;").getInt("TOTAL");
			totalDict = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words WHERE Translation IS NOT '--NULL--';").getInt("TOTAL");
			success = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return success;
	}
	public boolean InsertTranslation (String word, String translation) {
		//Insert word and translation.
		boolean success = false;
		try {
			statement.executeUpdate(
					"UPDATE Words "
					+ "SET Translation = '" + translation.replace('\'', '"') + "' "
					+ "WHERE Word = '" + word + "';");
			totalCount = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words;").getInt("TOTAL");
	    	totalDict = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words WHERE Translation IS NOT '--NULL--';").getInt("TOTAL");
	    	totalBook = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Book;").getInt("TOTAL");
			success = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return success;
	}
	public boolean ChangeRemembered (String word) {
		//Change the status of a word.
		boolean success = false;
		try {
			statement.executeUpdate(
					"UPDATE Words "
					+ "SET Status = 1 "
					+ "WHERE Word = '" + word + "';");
			totalRemembered = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Words WHERE Status IS NOT 0;").getInt("TOTAL");
			success = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	public boolean InsertBook (String book, String words) throws SQLiteException {
		//Insert a book and words of the book.
		boolean success = false;
		try {
			statement.executeUpdate(
					"INSERT INTO Book "
					+ "(BookName, Words)"
					+ "VALUES "
					+ "('" + book + "', '" + words + "');");
			totalBook = statement.executeQuery("SELECT COUNT(*) AS TOTAL FROM Book;").getInt("TOTAL");
			success = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return success;
	}
	public int GetTotalCount () {
		//Return the number of total words in the database.
		return totalCount;
	}
	public int GetTotalBook () {
		//Return the number of the books in the database.
		return totalBook;
	}
	public int GetTotalDict () {
		//Return the number of translated words.
		return totalDict;
	}
	public int GetTotalRemembered () {
		//Return the number of remembered words.
		return totalRemembered;
	}
	public String GetTranslation (String word) {
		//Get the translation of a word and store in the database.
		String translation;
		try {
			translation = statement.executeQuery("SELECT Translation FROM Words WHERE Word = '" + word + "' AND Translation IS NOT '--NULL--';").getString("Translation");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			translation = "--NULL--";
		}
		return translation;
	}
	public boolean WordExist (String word) {
		//Check the word if it is in the database.
		boolean exist = false;
		try {
			statement.executeQuery("SELECT Word FROM Words WHERE Word = '" + word + "';").getString("Word");
			exist = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return exist;
	}
	public boolean BookExist (String book) {
		//Check the book if it is in the database.
		boolean exist = false;
		try {
			statement.executeQuery("SELECT BookName FROM Book WHERE BookName = '" + book + "';").getString("BookName");
			exist = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return exist;
	}	
	public boolean CheckWordRemembered (String word) {
		//Check a word if it is remembered.
		boolean remembered = false;
		try {
			statement.executeQuery("SELECT Word FROM Words WHERE Word = '" + word + "' AND Status = 1;").getString("Word");
			remembered = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return remembered;
	}
	public String GetWordNotRemembered (int resultCount)[] {
		//Get the list of words not remembered.
		List<String> wordList = new ArrayList<>();
		try {
			ResultSet list = statement.executeQuery("SELECT Word FROM Words WHERE Translation IS NOT '--NULL--' AND Status = 0 LIMIT " + resultCount + ";");
			while(list.next()) {
				wordList.add(list.getString("Word"));
			}
			String[] result = wordList.toArray(new String[wordList.size()]);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public String GetNoTranslation(int resultCount)[] {
		//Get the list of words not translated.
		List<String> wordList = new ArrayList<>();
		try {
			ResultSet list = statement.executeQuery("SELECT Word FROM Words WHERE Translation IS '--NULL--' LIMIT " + resultCount + ";");
			while(list.next()) {
				wordList.add(list.getString("Word"));
			}
			String[] result = wordList.toArray(new String[wordList.size()]);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public String GetNoDict(int resultCount)[] {
		//Return the list of words which are no translations in the website. 
		List<String> wordList = new ArrayList<>();
		try {
			ResultSet list = statement.executeQuery("SELECT Word FROM Words WHERE Translation IS '--NOT-GET-TRANSLATED--' LIMIT " + resultCount + ";");
			while(list.next()) {
				wordList.add(list.getString("Word"));
			}
			String[] result = wordList.toArray(new String[wordList.size()]);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public String GetBookWord(String book)[] {
		//Return the list of words in a book.
		try {
			String words = statement.executeQuery("SELECT Words FROM Book WHERE BookName = '" + book + "';").getString("Words");
			List<String> wordList = new ArrayList<>();
			String pattern = "[a-z]{2,}";
			Pattern result = Pattern.compile(pattern);
			Matcher finder = result.matcher(words.toLowerCase());
			while (finder.find()) {
				wordList.add(finder.group());
				}
			String[] wordResult = wordList.toArray(new String[wordList.size()]);
			return wordResult;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			String[] result = new String[0];
			return result;
		}
	}
	public String GetBookNames(int resultCount)[] {
		//Get the list of books in the database.
		List<String> bookList = new ArrayList<>();
		try {
			ResultSet list = statement.executeQuery("SELECT BookName FROM Book LIMIT " + resultCount + ";");
			while(list.next()) {
				bookList.add(list.getString("BookName"));
			}
			String[] result = bookList.toArray(new String[bookList.size()]);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
