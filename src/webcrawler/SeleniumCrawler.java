package webcrawler;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.System.out;

public class SeleniumCrawler {
    public static void main(String[] args) {
        // WebDriver 설정
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver"); // ChromeDriver 경로 설정
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 최대 10초간 기다림

        try {
            // 파일 경로 및 output 파일명 설정
            String resultPath = "/JavaWorkSpace/JavaCrawler/src/webcrawler";
            String fileName = "crawling_result";
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
            String formattedDateTime = now.format(formatter);
            String extension = "md";
            String filePath = String.format("%s/%s_%s.%s", resultPath, fileName, formattedDateTime, extension);

            // file writer 준비
            FileWriter fw = new FileWriter(filePath, true);
            PrintWriter out = new PrintWriter(fw, true);

            // 크롤링 대상 사이트 경로 설정 및 연결 준비
            String URL = "https://www.gartner.com/en/newsroom/";
            int articleCnt = 0;
            for (int i = 1; i < 4; i++) {
                driver.get(URL + "?page=" + i);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".newsroom-article-list .article-title"))); // 대상 요소가 보일 때까지 기다림

                // 사이트 접근 및 관심 대상 콘텐츠 select
                for (WebElement element : driver.findElements(By.cssSelector(".newsroom-article-list .article-title"))) {
                    // 선택된 콘텐츠 모두 준비된 파일에 작성
                    String title = element.getText();
                    String link = element.findElement(By.tagName("a")).getAttribute("href");
                    out.println("### " + ++articleCnt + ". " + title); // title 마크 다운
                    out.println("- Link: " + link + "\n"); // 리스트 항목 마크다운
                    System.out.println(title); // 콘솔 확인
                }
                System.out.println("--------------------------------------");
            }
            System.out.println("크롤링 완료!");
        } catch (Exception e) {
            out.println("크롤링 실패 : ");
            e.printStackTrace();
        } finally {
            driver.quit(); // WebDriver 종료
            out.close();
        }
    }
}
