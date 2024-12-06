import java.io.File;

public class FilePermissionTest {
    public static void main(String[] args) {
        String filePath = "D:/XOSO_06_12_2024.csv";
        File file = new File(filePath);

        if (file.exists()) {
            System.out.println("File exists.");

            if (file.canRead()) {
                System.out.println("File can be read.");
            } else {
                System.out.println("File cannot be read.");
            }

            if (file.canWrite()) {
                System.out.println("File can be written to.");
            } else {
                System.out.println("File cannot be written to.");
            }

            if (file.canExecute()) {
                System.out.println("File can be executed.");
            } else {
                System.out.println("File cannot be executed.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
