import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler {
	//ũ�Ѹ��� url
	private static String univURL = "http://portal.hs.ac.kr/HsIs/index.jsp";
	//�б� �������� 5by6 matrix
	private static String[][] notice_text = new String[5][7];
	private static String[][] notice_link = new String[5][7];
	
	//������, ���ߤ� ���ʿ��� �� �־ �������� �ϴ� ����.
	Crawler(){}
	
	//correction
	public void Update() {
		try {
			Document doc = Jsoup.connect(univURL).get();
			Elements notices = doc.select("ul.recentBbsInnerUl").select("li.recentBbsInnerLi");

			//�ؽ�Ʈ + ��ũ
			for(int i=0; i<notices.size(); i++) {
				notice_text[i/7][i%7] = notices.get(i).text();
				notice_link[i/7][i%7] = notices.get(i).select("a").attr("href");
			}
		}catch(Exception e) {System.out.println("crawling err");}
	}
	
	public String getNoticeText(int row, int col) {return notice_text[row][col];}
	public String getNoticeLink(int row, int col) {return notice_link[row][col];}
}
