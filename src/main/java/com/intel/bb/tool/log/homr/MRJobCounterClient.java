package com.intel.bb.tool.log.homr;

import com.google.gson.Gson;
import com.intel.bb.tool.log.*;
import com.intel.bb.tool.log.dto.MRJobCounter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoy on 5/6/2016.
 */
public class MRJobCounterClient extends LogFetcherClient {

    public MRJobCounterClient(LogFetcherContext ctx) {
        this.ctx = ctx;
    }

    public String getHistoryServerURL(String appId) {
        return Configuration.get(Constants.YARN_RESTAPI_URL_HS_JOB_COUNTER_NAME)
                .replace("host", Configuration.get(Constants.HISTORY_SERVER_HOST))
                .replace("port", Configuration.get(Constants.HISTORY_SERVER_PORT))
                .replace("jobid", appId);
    }

    protected YarnJobCounterResponse getData4App(String jobid) throws IOException {
        System.out.println("---> Processing job: " + jobid);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = getHttpGet(jobid);
        CloseableHttpResponse response = httpClient.execute(httpget);
        try {
            if (!isSuccess(response)) return new YarnJobCounterResponse(
                    false, String.valueOf(response.getStatusLine().getStatusCode()), null, jobid);
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            String rs = sb.toString().trim();
            MRJobCounter rst = gson.fromJson(rs, MRJobCounter.class);
            return new YarnJobCounterResponse(true, rs, rst, jobid);
        } finally {
            response.close();
        }
    }

    public class YarnJobCounterResponse extends HSResponse {
        private MRJobCounter rs;

        public YarnJobCounterResponse(boolean success, String msg, MRJobCounter rs, String appId) {
            this.success = success;
            this.msg = msg;
            this.rs = rs;
            this.appId = appId;
        }

        public MRJobCounter getRs() {
            return rs;
        }

        public void setRs(MRJobCounter rs) {
            this.rs = rs;
        }
    }

}


