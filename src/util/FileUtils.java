package util;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    private static final String DATA_DIR = "src/database";
    private static final String CSV_FILE = DATA_DIR + "/contacts.csv";
    public static void ensureDataDirectoryExists() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo thư mục data", e);
        }
    }
    
    public static String getContactsFilePath() {
        ensureDataDirectoryExists();
        return CSV_FILE;
    }
}
