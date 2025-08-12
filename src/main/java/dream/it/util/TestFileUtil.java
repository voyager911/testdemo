package dream.it.util;

public class TestFileUtil {
    public static String getPath() {
        return TestFileUtil.class
                .getResource("/")
                .getPath()
                .replace("classes/", "");
    }
}