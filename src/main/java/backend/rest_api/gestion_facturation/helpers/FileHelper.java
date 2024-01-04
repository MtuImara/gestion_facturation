package backend.rest_api.gestion_facturation.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

@Configuration
public class FileHelper {
    public static byte[] getFile(String path) throws IOException {
        ClassPathResource file = new ClassPathResource(path);
        byte[] imageBytes = null;
        try {
            imageBytes = org.springframework.util.StreamUtils.copyToByteArray(file.getInputStream());
        } catch (java.io.IOException e) {
            throw new IOException(MessageHelper.noContent("File doesn't exist"));
        }
        if (imageBytes != null) {
            return imageBytes;
        } else {
            throw new IOException(MessageHelper.noContent("File not found"));
        }
    }

    public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)
            throws IOException {

        Path uploadPath = Paths.get("../webapps/asyst_erp_api/WEB-INF/classes/" + uploadDir);
        Path uploadPath1 = null;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            InputStream inputStream2 = multipartFile.getInputStream();
            Path uploadPathLocal = Paths.get("src/main/resources/");
            Path uploadPathLocal1 = Paths.get("target/classes/");
            if (Files.isDirectory(uploadPathLocal) && Files.isDirectory(uploadPathLocal1)) {
                uploadPath = Paths.get("src/main/resources/" + uploadDir);
                uploadPath1 = Paths.get("target/classes/" + uploadDir);
            }
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                if (!Files.exists(uploadPath1)) {
                    Files.createDirectories(uploadPath1);
                }
            }
            if (!fileName.equalsIgnoreCase("") && fileName != null) {

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                if (uploadPath1 != null) {
                    Path filePath1 = uploadPath1.resolve(fileName);
                    Files.copy(inputStream2, filePath1, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception ioe) {
            throw new IOException("Echec to save " + fileName, ioe);
        }
    }

    public void saveFacture(String uploadDirFile, String facture, MultipartFile file)
            throws IOException {

        Path uploadPath = Paths.get("../webapps/asyst_erp_api/WEB-INF/classes/" + uploadDirFile);
        Path uploadPath1 = null;
        try (InputStream inputStream = file.getInputStream()) {
            Path uploadPathLocal = Paths.get("src/main/resources/");
            Path uploadPathLocal1 = Paths.get("target/classes/");
            if (Files.isDirectory(uploadPathLocal) && Files.isDirectory(uploadPathLocal1)) {
                uploadPath = Paths.get("src/main/resources/" + uploadDirFile);
                uploadPath1 = Paths.get("target/classes/" + uploadDirFile);
            }
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                if (uploadPath1 != null) {
                    Files.createDirectories(uploadPath1);
                }
            }
            if (!facture.equalsIgnoreCase("") && facture != null) {
                Path filePath = uploadPath.resolve(facture);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ioe) {
            throw new IOException("Echec to save " + facture);
        }
    }

    public void deleteDirectoryRecursionJava6(File file) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectoryRecursionJava6(entry);
                }
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

    public static void deleteDirectoryRecursion(File file) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        try {
            file.delete();
        } catch (Exception e) {
            throw new IOException("Failed to delete ");
        }
    }

    private final Path root = Paths.get("uploads");

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
