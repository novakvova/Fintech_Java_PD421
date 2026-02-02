package org.example.services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {
    @Value("${upload.dir}")
    private String uploadDir;

    String extention = "webp";

    Map<String, Integer> sizes = Map.of(
            "small", 32,
            "medium", 100,
            "large", 1000
    );

    public String load(MultipartFile file) {
        if(file.isEmpty()) return "";

        try(var inputStream = file.getInputStream()) {
            String fileName = saveStreamToFile(inputStream);
            return fileName;
        } catch (IOException e) {
            System.out.println("Problem save image " + e);
            return "";
        }

    }
    public String load(String imageUrl) {
        if(!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://"))
            return "";
        try(var inputStream = new URL(imageUrl).openStream()) {
            String fileName = saveStreamToFile(inputStream);
            return fileName;
        } catch (IOException e) {
            System.out.println("Problem save image " + e);
            return "";
        }
    }

    public void remove(String fileName) {
        try {
            for(var folder : sizes.keySet()) {
                Path filePath = Paths.get(uploadDir, folder, fileName);
                Files.deleteIfExists(filePath);
            }
        } catch(Exception e) {
            System.out.println("Problem remove image " + e);
        }
    }

    public String replace(String oldFileName, MultipartFile newFile) {
        var newFileName = load(newFile);
        if(newFileName.isEmpty())
            return oldFileName;
        remove(oldFileName);
        return newFileName;
    }

    private String saveStreamToFile(InputStream stream) throws IOException {
        //Створюємо папку, якщо її немає
        Files.createDirectories(Paths.get(uploadDir));
        //Вказую навзу для файлу
        String fileName = UUID.randomUUID().toString() + "." + extention;
        //Читакю потік для роботи із зображеням
        var bufferedImage = ImageIO.read(stream);
        for(var entry : sizes.entrySet()) { //перебираю нашу колекцію розмірів
            var folder = entry.getKey(); //По ключу отримую назву папки
            var size = entry.getValue(); //По значеню отримую розмір
            Files.createDirectories(Paths.get(uploadDir, folder)); //створю для розміру папку
            String filePath = Paths.get(uploadDir, folder, fileName).toString(); //Форму шлях і ім'я
            Thumbnails.of(bufferedImage) //Використовую Thumbnails і передаю буфер фото
                    .size(size, size) //Вказую, який має бути розмір
                    .outputFormat(extention) //Вказую формат
                    .toFile(filePath); //Збергіаю фото
        }
        return fileName; //Повертаю назву файлу
    }
}
