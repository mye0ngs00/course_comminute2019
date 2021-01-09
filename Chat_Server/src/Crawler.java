import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/* 교내 홈페이지 파싱을 위한 크롤러 */
public class Crawler {
	final private String univURL = "http://portal.hs.ac.kr/HsIs/index.jsp";
	private String[][] notice_text;
	private String[][] notice_link;
	
	// 생성자
	public Crawler() {
		this.notice_text = new String[5][7];
		this.notice_link = new String[5][7];
	}
	
	// 파싱
	public void Update() {
		try {
			Document doc = Jsoup.connect(univURL).get();
			Elements notices = doc.select("ul.recentBbsInnerUl").select("li.recentBbsInnerLi");

			for(int i=0; i<notices.size(); i++) {
				notice_text[i/7][i%7] = notices.get(i).text();
				notice_link[i/7][i%7] = notices.get(i).select("a").attr("href");
			}
		}catch(Exception e) {
			System.out.println("crawling err");
		}
	}
	
	public String getNoticeText(int row, int col) {return notice_text[row][col];}
	public String getNoticeLink(int row, int col) {return notice_link[row][col];}
}
