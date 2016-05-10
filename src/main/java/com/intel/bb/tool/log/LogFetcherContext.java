package com.intel.bb.tool.log;

import com.intel.bb.tool.log.homr.MRJobCounterClient;
import com.intel.bb.tool.log.homr.MRJobCounterExcelWriter;
import com.intel.bb.tool.log.spark.SparkAppStagesClient;
import com.intel.bb.tool.log.spark.SparkAppStagesExcelWriter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yaoy on 5/6/2016.
 */
public class LogFetcherContext {

    public LogFetcherContext(String confFilePath) {
        Configuration.init(confFilePath);
    }

    public File[] listRequiredLogFiles() {
        File logDir = new File(Configuration.get(Constants.BB_LOG_DIR_NAME));
        System.out.println("Log dir: " + logDir.getPath());
        return logDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File logFile) {
                String fileName = logFile.getName();
                if (!logFile.isFile()
                        || logFile.isHidden()
                        || !(fileName.contains("test")
                        || fileName.contains("populateMetastore"))
                        || fileName.startsWith(".")
                        || !fileName.endsWith(".log")) {
                    return false;
                }
                return true;
            }
        });
    }

    public List<String> listAppIds(File logFile)
            throws IOException {
        List<String> appIds = new LinkedList<String>();
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        String line = null;
        String engineName = Configuration.get(Constants.BB_ENGINE_NAME);
        String regExp4AppId = Configuration.get(Constants.LOG_REGEXP_APP_ID_MR_NAME);
        if (Constants.BB_ENGINE_VALUE_MR.equals(engineName)) {
            regExp4AppId = Configuration.get(Constants.LOG_REGEXP_APP_ID_MR_NAME);
        } else if (Constants.BB_ENGINE_VALUE_HOS.equals(engineName)) {
            regExp4AppId = Configuration.get(Constants.LOG_REGEXP_APP_ID_SPARK_NAME);
        }
        while ((line = br.readLine()) != null) {
            if (line.startsWith(regExp4AppId)) {
                String appId = "unknown";
                try {
                    if (Constants.BB_ENGINE_VALUE_MR.equals(engineName)) {
                        appId = line.split(",")[0].replace(regExp4AppId, "").trim();
                    } else if (Constants.BB_ENGINE_VALUE_HOS.equals(engineName)) {
                        appId = line.replace(regExp4AppId, "").trim();
                    }
                } catch (Exception e) {
                    System.out.println("Cannot parse the app id from line: " + line);
                    continue;
                }
                appIds.add(appId);
            }
        }
        return appIds;
    }

    public LogFetcherClient getClient() {
        String engineName = Configuration.get(Constants.BB_ENGINE_NAME);
        if (Constants.BB_ENGINE_VALUE_MR.equals(engineName)) {
            return new MRJobCounterClient(this);
        } else if (Constants.BB_ENGINE_VALUE_HOS.equals(engineName)) {
            return new SparkAppStagesClient(this);
        }
        throw new RuntimeException("Unsupported engine: " + engineName);
    }

    public LogDataWriter getLogDataWriter() {
        String engineName = Configuration.get(Constants.BB_ENGINE_NAME);
        if (Constants.BB_ENGINE_VALUE_MR.equals(engineName)) {
            return new MRJobCounterExcelWriter();
        } else if (Constants.BB_ENGINE_VALUE_HOS.equals(engineName)) {
            return new SparkAppStagesExcelWriter();
        }
        throw new RuntimeException("Unsupported engine: " + engineName);
    }

}
