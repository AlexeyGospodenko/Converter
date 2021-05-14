package converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info("args[]={}", Arrays.asList(args));

        if (args.length != 4) {
            LOGGER.info("args must be: -input filenameForConvert -rules filenameWithRegExps");
        } else {
            //Map for save argument values
            Map<String, String> parameters = new LinkedHashMap<>();

            //Fill argument values
            for (int i = 0; i < args.length - 1; i = i + 2) {
                parameters.put(args[i], args[i + 1]);
            }

            String filenameForConvert = parameters.get("-input");
            String filenameWithRules = parameters.get("-rules");

            LOGGER.debug("filenameForConvert={}", filenameForConvert);
            LOGGER.debug("filenameWithRegExps={}", filenameWithRules);

            //If files exists
            if (FileUtils.checkFileExist(filenameForConvert) && FileUtils.checkFileExist(filenameWithRules)) {
                //Load rules from file
                Map<String, String> rules = FileUtils.getRulesFromFile(filenameWithRules);
                LOGGER.info("Loaded rules: " + rules.entrySet().size());
                //Send file and rules for convert
                FileUtils.convertFile(filenameForConvert, rules);
            }
        }
    }

}
