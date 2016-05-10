package com.intel.bb.tool.log.spark;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intel.bb.tool.log.Configuration;
import com.intel.bb.tool.log.Constants;
import com.intel.bb.tool.log.LogFetcherClient;
import com.intel.bb.tool.log.LogFetcherContext;
import com.intel.bb.tool.log.dto.SparkAppStages;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yaoy on 5/9/2016.
 */
public class SparkAppStagesClient extends LogFetcherClient {

    public SparkAppStagesClient(LogFetcherContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public String getHistoryServerURL(String appId) {
        return Configuration.get(Constants.SPARK_RESTAPI_URL_HS_STAGES_NAME)
                .replace("host", Configuration.get(Constants.HISTORY_SERVER_HOST))
                .replace("port", Configuration.get(Constants.HISTORY_SERVER_PORT))
                .replace("appid", appId);
    }

    @Override
    protected SparkAppStagesResponse getData4App(String appId) throws IOException {
        System.out.println("---> Processing job: " + appId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = getHttpGet(appId);
        CloseableHttpResponse response = httpClient.execute(httpget);

        try {
            if (!isSuccess(response)) return new SparkAppStagesResponse(
                    false, String.valueOf(response.getStatusLine().getStatusCode()), null, appId);
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            String rs = sb.toString().trim();

            Type collectionType = new TypeToken<Collection<SparkAppStages>>(){}.getType();
            Collection<SparkAppStages> rst = gson.fromJson(rs, collectionType);
            Collections.sort((List<SparkAppStages>) rst, new Comparator<SparkAppStages>() {
                @Override
                public int compare(SparkAppStages o1, SparkAppStages o2) {
                    return Integer.valueOf(o1.getStageId()) < Integer.valueOf(o2.getStageId()) ? -1 : 1;
                }
            });
            return new SparkAppStagesResponse(true, rs, rst, appId);
        } catch (Exception e) {
            System.err.println("Failed to process history server's response.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            response.close();
        }
    }

    public class SparkAppStagesResponse extends HSResponse {
        private Collection<SparkAppStages> rs;

        public SparkAppStagesResponse(boolean success, String msg,
                                        Collection<SparkAppStages> rs, String appId) {
            this.success = success;
            this.msg = msg;
            this.rs = rs;
            this.appId = appId;
        }

        public Collection<SparkAppStages> getRs() {
            return rs;
        }

        public void setRs(Collection<SparkAppStages> rs) {
            this.rs = rs;
        }
    }
}
