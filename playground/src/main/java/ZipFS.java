import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZipFS {

  public static void main(String[] args) {
    try {
      inspect(args[0]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void inspect(String filename) throws IOException {
    FileSystem fs = FileSystems.newFileSystem(Path.of(filename), (ClassLoader) null);
    Path root = fs.getPath("/");
    // Files.walk(root).filter(e -> Files.isRegularFile(e)).forEach(entry -> {
    Files.walk(root)
        .forEach(
            entry -> {
              // System.out.println(entry);
              try (InputStream in = Files.newInputStream(entry)) {
                int ret = -1;
                do {
                  byte[] b = new byte[1024];
                  ret = in.read(b);
                  // System.out.println(ret);
                } while (ret != -1);
              } catch (IOException e) {
                // e.printStackTrace();
              }
            });
  }
}
