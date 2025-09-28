package ru.gl.docs.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileDownloadController {

    private final Path fileStorageLocation = Paths.get("src/main/resources/static");

    @GetMapping("/download/{filename:.+}")
    public void downloadFile(@PathVariable String filename, HttpServletResponse response) {
        try {
            // Полный путь к файлу
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Получаем оригинальное имя файла для скачивания
                String originalFilename = getFilenameFromPath(filename);

                // Кодируем имя файла для корректного отображения в браузере
                String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.toString())
                        .replace("+", "%20");

                // Определяем Content-Type
                String contentType = determineContentType(originalFilename);

                // Устанавливаем правильные заголовки
                response.setContentType(contentType);
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename);
                response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
                response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
                response.setHeader(HttpHeaders.PRAGMA, "no-cache");
                response.setHeader(HttpHeaders.EXPIRES, "0");

                // Копируем файл в ответ
                Files.copy(filePath, response.getOutputStream());
                response.getOutputStream().flush();

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File not found: " + filename);
            }
        } catch (Exception e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error downloading file: " + e.getMessage());
            } catch (Exception ex) {
                // Игнорируем ошибку записи в ответ
            }
        }
    }

    private String determineContentType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "txt":
                return "text/plain";
            case "zip":
                return "application/zip";
            default:
                return "application/octet-stream";
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return (lastDotIndex > 0) ? filename.substring(lastDotIndex + 1) : "";
    }

    private String getFilenameFromPath(String path) {
        int lastSlashIndex = path.lastIndexOf("/");
        return (lastSlashIndex > 0) ? path.substring(lastSlashIndex + 1) : path;
    }
}
