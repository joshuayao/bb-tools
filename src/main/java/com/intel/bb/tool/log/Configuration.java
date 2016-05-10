package com.intel.bb.tool.log;

import org.apache.http.util.Asserts;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by yaoy on 5/6/2016.
 */
public class Configuration {

    private static Properties props = null;

    public static void init(String propPath) {
        try {
            File propFile = new File(propPath);
            props = new Properties();
            props.load(new FileReader(propFile));
            validate();
        } catch (IOException e) {
            System.out.println("Failed to initial configuration.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void validate() {
        Asserts.notBlank(get(Constants.BB_ENGINE_NAME), "bb.engine");
        Asserts.notBlank(get(Constants.BB_LOG_DIR_NAME), "bb.log.dir");
        Asserts.notBlank(get(Constants.HISTORY_SERVER_HOST), "history.server.host");
        Asserts.notBlank(get(Constants.HISTORY_SERVER_PORT), "history.server.port");
        if (Constants.BB_ENGINE_VALUE_MR.equals(get(Constants.BB_ENGINE_NAME))) {
            Asserts.notBlank(get(Constants.LOG_REGEXP_APP_ID_MR_NAME), "log.regexp.app.id.mr");
            Asserts.notBlank(get(Constants.YARN_RESTAPI_URL_HS_JOB_COUNTER_NAME), "yarn.restapi.url.hs.job.counter");
        } else if (Constants.BB_ENGINE_VALUE_HOS.equals(get(Constants.BB_ENGINE_NAME))) {
            Asserts.notBlank(get(Constants.LOG_REGEXP_APP_ID_SPARK_NAME), "log.regexp.app.id.spark");
            Asserts.notBlank(get(Constants.SPARK_RESTAPI_URL_HS_STAGES_NAME), "spark.restapi.url.hs.stages");
        }
    }

    public static String get(String key) {
        if (props == null) {
            throw new RuntimeException("Configuration is not initialed. Please call Configuration#init(String) first.");
        }
        return props.getProperty(key);
    }


}
