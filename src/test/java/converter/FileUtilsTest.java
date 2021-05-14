package converter;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileUtilsTest {
    public final static String RULES_FILENAME = "RulesUnitTest.txt";
    public final static String RULES_FILENAME_CONTENT = "Вася->Петя\nОля->Анжелика\n";
    public final static String CONVERT_FILENAME = "ConvertUnitTest.txt";
    public final static String CONVERT_FILENAME_CONTENT = "Вася\nОля\n";
    public final static LinkedHashMap<String, String > EXPECTED_MAP = new LinkedHashMap() {{
        put("Вася", "Петя");
        put("Оля", "Анжелика");
    }};
    public final static String EXPECTED_CONVERTED_STRING = "Петя\nАнжелика";

    @BeforeAll
    public static void init() {
        Path filenameWithRules = Paths.get(RULES_FILENAME);
        Path filenameForConvert = Paths.get(CONVERT_FILENAME);
        try {
            Files.deleteIfExists(filenameWithRules);
            FileOutputStream fos = new FileOutputStream(filenameWithRules.toFile());
            fos.write(RULES_FILENAME_CONTENT.getBytes(StandardCharsets.UTF_8));
            fos.close();
            fos = new FileOutputStream(filenameForConvert.toFile());
            fos.write(CONVERT_FILENAME_CONTENT.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileCheck() {
        Assertions.assertTrue(FileUtils.checkFileExist(RULES_FILENAME));
        Assertions.assertTrue(FileUtils.checkFileExist(CONVERT_FILENAME));
    }

    @Test
    public void testConvertString() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        PrintStream old = System.out;
        System.setOut(ps);
        FileUtils.convertString(CONVERT_FILENAME_CONTENT.getBytes(StandardCharsets.UTF_8),
                true,
                EXPECTED_MAP);
        System.out.flush();
        System.setOut(old);
        bos.close();
        String string = bos.toString().substring(0, bos.toString().length() - 3);
        Assertions.assertEquals(EXPECTED_CONVERTED_STRING, string);
    }

    @Test
    public void testGetRulesFromFile() {
        Map<String, String> rules = new LinkedHashMap<>();
        try {
            rules = FileUtils.getRulesFromFile(RULES_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(rules, EXPECTED_MAP);
    }
}
