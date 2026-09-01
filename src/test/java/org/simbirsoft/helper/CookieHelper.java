package org.simbirsoft.helper;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;

public class CookieHelper {

    /**
     * Запись текущих кук браузера в файл
     * @param driver WebDriver
     * @param filePath путь для сохранения файла куков
     */
    public static void saveCookies(WebDriver driver, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Cookie cookie : driver.manage().getCookies()) {
                writer.write(cookie.getName() + ";" +
                        cookie.getValue() + ";" +
                        cookie.getDomain() + ";" +
                        cookie.getPath());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Чтение кук из файла и добавление их в сессию браузера
     * @param driver WebDriver
     * @param filePath путь к сохраненным кукам
     */
    public static void loadCookies(WebDriver driver, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    Cookie cookie = new Cookie.Builder(parts[0], parts[1])
                            .domain(parts[2])
                            .path(parts[3])
                            .build();
                    driver.manage().addCookie(cookie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Безопасная загрузка кук (если файл существует) с последующим обновлением страницы
     * @param driver WebDriver
     * @param filePath путь к сохраненным кукам
     */
    public static void loadCookiesAndRefresh(WebDriver driver, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            //Удаление новых кук на странице
            driver.manage().deleteAllCookies();

            loadCookies(driver, filePath);

            //Обновление страницы
            driver.navigate().refresh();
        }
    }
}
