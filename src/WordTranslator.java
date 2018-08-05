import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WordTranslator {
	String word;
//	public WordTranslator (String currentWord) {
//		word = currentWord;
//	}
	public String GetWord() {
		return word;
	}
	public String GetTranslation (String sourceWord) {
		try {
			word = sourceWord;
			String url = "https://dictionary.cambridge.org/zhs/词典/英语-汉语-简体/";
			Document dictPage = Jsoup.connect(url + word).get();
			//System.out.println(dictPage.getElementsByClass("sense-block").text());
			Elements dict = dictPage.getElementsByClass("sense-block");
			dict.select("div.extraexamps").empty();
			dict.select("div.examp").empty();
			if(!dict.isEmpty()) {
				return dict.text().replaceAll("词库：同义词和关联词 ","");
			} else {
				return "--NOT-GET-TRANSLATED--";
			}
			//System.out.print(dict.text());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR WHILE LOOKING UP");
			return "--NULL--";
		}
	}
}
