import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

class TC1_VerifyLogin {

    @Test
    void testCheckLogin() throws Exception {
        System.setProperty("webdriver.edge.driver", "D:/UnitTest/Driver/msedgedriver.exe");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();  
        String testDate = formatter.format(date);
        String testerName = "Suppha1ak";
       
        String path = "D:/testdataSupphalak.xlsx";
        FileInputStream fs = new FileInputStream(path);

        //Creating a workbook
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        
        for (int i = 1; i <= rowCount; i++) {
            Row currentRow = sheet.getRow(i);

            if (currentRow == null || currentRow.getCell(0) == null || currentRow.getCell(0).toString().trim().isEmpty()) {
                break; // Exit loop if the row is empty or has no data in the first cell
            }
            
            WebDriver driver = new EdgeDriver();
            driver.get("http://localhost:5173/");
            driver.findElement(By.id("GetStarted")).click();
            Thread.sleep(3000);
            String testcaseid = currentRow.getCell(0).toString();
            String username;
            String password;
            if (testcaseid.equals("tc103")) {
                username = "";
                password = "";
            } else {
                username = currentRow.getCell(1).toString();            
                password = currentRow.getCell(2).toString();        
            }
            driver.findElement(By.name("email")).sendKeys(username);
            driver.findElement(By.name("password")).sendKeys(password);
            Thread.sleep(3000);
            driver.findElement(By.id("Login")).click();
            Thread.sleep(3000);
            driver.findElement(By.id("avatarButton")).click();
            Thread.sleep(3000);
            String actual;
            String expected = currentRow.getCell(3) != null ? currentRow.getCell(3).toString() : "";
            if (testcaseid.equals("tc101")) {
                actual = driver.findElement(By.xpath("/html/body/div/div[3]/div[1]/nav/div/div[1]/button/div/div[2]/div[1]/div[2]")).getText();
            } else {
                actual = driver.findElement(By.xpath("/html/body/div/div[3]/div[1]/nav/div/div[1]/button/div/div[2]/div[1]/div[2]")).getText();
            }
            Cell cell = currentRow.createCell(4);
            cell.setCellValue(actual);
            assertEquals(expected, actual);
            Cell resultCell = currentRow.createCell(5);
            resultCell.setCellValue(actual.equals(expected) ? "Pass" : "Fail");
            currentRow.createCell(6).setCellValue(testDate);
            currentRow.createCell(7).setCellValue(testerName);
            
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
            driver.close();
        }
        
        workbook.close();
    }
}
