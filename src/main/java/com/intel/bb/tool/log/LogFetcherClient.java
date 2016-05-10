package com.intel.bb.tool.log;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoy on 5/9/2016.
 */
public abstract class LogFetcherClient {

    protected LogFetcherContext ctx;

    protected LogFetcherClient() {}

    public LogFetcherClient(LogFetcherContext ctx) {
        this.ctx = ctx;
    }

    public abstract String getHistoryServerURL(String appId);

    public boolean isSuccess(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    public HttpGet getHttpGet(String appId) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new HttpGet(getHistoryServerURL(appId));
    }

    public void process() {
        try {
            File[] logFiles = ctx.listRequiredLogFiles();
            System.out.println(logFiles.length + " log files found.");

            Map<String, List<HSResponse>> data
                    = new LinkedHashMap<String, List<HSResponse>>();

            for (File logFile : logFiles) {
                String fileName = logFile.getName();
                System.out.println("Processing log file: " + logFile.getName());
                List<String> appIds = ctx.listAppIds(logFile);
                for (String appId : appIds) {
                    HSResponse rs = getData4App(appId);
                    if (data.get(fileName) == null) {
                        data.put(fileName, new LinkedList<HSResponse>());
                    }
                    data.get(fileName).add(rs);
                }
            }
            LogDataWriter writer = ctx.getLogDataWriter();
            writer.exec(data);
            System.out.println("Done.");
        } catch (IOException e) {
            System.err.println("Failed to process log files.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected abstract HSResponse getData4App(String appId) throws IOException;

    public abstract class HSResponse {

        protected boolean success;
        protected String appId;
        protected String msg;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

    }

}
