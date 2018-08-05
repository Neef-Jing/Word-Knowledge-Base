import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class WordGenerator {
	int totalWordCount = 0;
	int uniqueWordCount = 0;
	File book;
	String bookName;
	public WordGenerator(String filename) {
		book = new File(filename);
		bookName = filename;
	}
	public String GetBookName() {
		return bookName;
	}
	public void ChangeBookName(String newName) {
		bookName = newName;
	}
	public int GetWordCount() {
		return totalWordCount;
	};
	public String GetContent() {
		Long booksize = book.length();
		byte[] content_temp = new byte[booksize.intValue()];
		try {
			FileInputStream readbook = new FileInputStream(book);
			readbook.read(content_temp);
			readbook.close();
			String content = new String(content_temp, "UTF-8");
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	public String GetTotalWords()[] {
		String content = GetContent();
		List<String> wordList = new ArrayList<>();
		String pattern = "[a-z]{2,}";
		Pattern result = Pattern.compile(pattern);
		Matcher finder = result.matcher(content.toLowerCase());
		while (finder.find()) {
			wordList.add(finder.group());
			}
		totalWordCount = wordList.size();
		String[] wordResult = wordList.toArray(new String[totalWordCount]);
		Arrays.sort(wordResult);
		return wordResult;
	}
	public String GetUniqueWords()[] {
		String[] totalWords = GetTotalWords();
		List<String> uniqueWords = new ArrayList<>();
		uniqueWords.add(totalWords[0]);
		for(int i = 0; i < (totalWords.length - 1); i++) {
			if(!totalWords[i].equals(totalWords[i+1])) {
				uniqueWords.add(totalWords[i+1]);
			}
		}
		uniqueWordCount = uniqueWords.size();
		String[] result = uniqueWords.toArray(new String[uniqueWordCount]);
		return result;
	}
}