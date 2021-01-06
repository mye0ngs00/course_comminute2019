import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler {
	//크롤링할 url
	private static String univURL = "http://portal.hs.ac.kr/HsIs/index.jsp";
	//학교 공지사항 5by6 matrix
	private static String[][] notice_text = new String[5][7];
	private static String[][] notice_link = new String[5][7];
	
	//생성자, 나중ㅇ ㅔ필요할 수 있어서 까먹을까봐 일단 만듦.
	Crawler(){}
	
	//correction
	public void Update() {
		try {
			Document doc = Jsoup.connect(univURL).get();
			Elements notices = doc.select("ul.recentBbsInnerUl").select("li.recentBbsInnerLi");

			//텍스트 + 링크
			for(int i=0; i<notices.size(); i++) {
				notice_text[i/7][i%7] = notices.get(i).text();
				notice_link[i/7][i%7] = notices.get(i).select("a").attr("href");
			}
		}catch(Exception e) {System.out.println("crawling err");}
	}
	
	public String getNoticeText(int row, int col) {return notice_text[row][col];}
	public String getNoticeLink(int row, int col) {return notice_link[row][col];}
}
