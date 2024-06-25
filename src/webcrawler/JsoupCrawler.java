package webcrawler;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupCrawler {
    public static void main(String[] args) {
        try {
            // 파일 경로 및 output 파일명 설정
            String resultPath = "/JavaWorkSpace/Crawler/src/webcrawler";
            String fileName = "crawling_result";
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
            String formattedDateTime = now.format(formatter);
            String extension = "md";
            String filePath = String.format("%s/%s_%s.%s", resultPath, fileName, formattedDateTime, extension);
            System.out.println(filePath);
            System.out.println("--------------------------------------");
            // file writer 준비
            FileWriter fw = new FileWriter(filePath, true);
            PrintWriter out = new PrintWriter(fw, true);

            // 크롤링 대상 사이트 경로 설정 및 연결 준비
            String URL = "https://www.gartner.com/en/newsroom/";
            int articleCnt = 0;
            for (int i=1; i<4; i++) {
                String params = "?page=" + i ;
                Connection conn = Jsoup.connect(URL + params)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36")
                        .referrer("http://www.google.com")
                        .header("Accept", "text/html")
                        .header("Accept-Language", "en-US,en;q=0.9");
                // bot 차단을 뚫기 위한 User-Agent 헤더 설정


                // 사이트 접근 및 관심 대상 콘텐츠 select
                Document doc = conn.get();
                Elements elements = doc.select(".newsroom-article-list .JsoupCrawler"); // 실제 클래스 명에 맞게
                //Elements elements = doc.select(".topics .topictitle");
                for( Element element : elements ) {
                    // 선택된 콘텐츠 모두 준비된 파일에 작성
                    out.println("### " + ++articleCnt + ". " + element.text()); // title 마크 다운
                    out.println("- Link: "+element.select("a[href]").attr("href")+"\n"); // 리스트 항목 마크다운
                    System.out.println(element.text()); // 콘솔 확인
                }
                System.out.println("--------------------------------------");
            }
            System.out.println("크롤링 완료!");
        } catch (Exception e) {
            System.out.println("크롤링 실패 : ");
            //System.out.println("오류 발생 URL: " + URL + params);
            e.printStackTrace();
        }
    }
}