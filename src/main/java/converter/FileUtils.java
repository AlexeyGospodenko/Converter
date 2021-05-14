package converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static boolean checkFileExist(String filename) {
        if (Files.exists(Paths.get(filename)) && !Files.isDirectory(Paths.get(filename))) {
            LOGGER.debug("File \"{}\" is exist", filename);
            return true;
        } else {
            LOGGER.info("File \"{}\" does not exist or directory", filename);
            return false;
        }
    }

    public static Map<String, String> getRulesFromFile(String filenameWithRules) throws IOException {
        Map<String, String> rules = new LinkedHashMap<>();
        InputStream is = new FileInputStream(filenameWithRules);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String lineRead;
        int numLine = 1;
        while ((lineRead = br.readLine()) != null) {
            String[] values = lineRead.split("->");
            if (values.length == 2) {
                rules.put(values[0], values[1]);
                LOGGER.debug("Read line {}:=\"{}\"", numLine++, lineRead);
            } else {
                LOGGER.error("Error while trying to insert value into Map<String,String> rules=\"{}\"", lineRead);
            }
        }
        is.close();
        return rules;
    }

    public static void convertFile(String filenameForConvert, Map<String, String> rules) throws IOException {
        LOGGER.info("Start processing file=\"{}\"", filenameForConvert);
        RandomAccessFile raf = new RandomAccessFile(filenameForConvert, "r");

        byte[] buffer = new byte[256];
        long pos = 0;

        //Files can have long lines, so we use byte reading instead of readline
        while (pos < raf.length()) {
            raf.seek(pos);
            if (buffer.length + pos < raf.length()) {
                raf.read(buffer);
                pos += convertString(buffer, false, rules);
            } else {
                buffer = new byte[(int) (raf.length() - pos)];
                raf.read(buffer);
                pos += convertString(buffer, true, rules);
            }
        }
        raf.close();
    }

    public static int convertString(byte[] buffer, boolean last, Map<String, String> rules) {
        String string;
        LOGGER.debug("last={}", last);
        //Reading to the last space, because the word may not be read completely
        if (!last) {
            string = new String(buffer,StandardCharsets.UTF_8);
            System.out.println(string);
            string = string.substring(0, string.lastIndexOf(" "));
            LOGGER.debug("string=\"{}\"", string);
        } else {
            string = new String(buffer,StandardCharsets.UTF_8);
            LOGGER.debug("string=\"{}\"", string);
        }

        for (Map.Entry<String, String> rule : rules.entrySet()) {
            string = string.replaceAll(rule.getKey(), rule.getValue());
        }
        System.out.println(string);
        return string.getBytes(StandardCharsets.UTF_8).length;
    }
}
