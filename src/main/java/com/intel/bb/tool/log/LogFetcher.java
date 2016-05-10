package com.intel.bb.tool.log;

/**
 * Created by yaoy on 5/9/2016.
 */
public class LogFetcher {

    public static void main(String[] args) {
        if (args.length < 1 || args[0] == null || args[0].isEmpty()) {
            System.out.println("LogFetcher [properties file]");
            System.exit(-1);
        }
        System.out.println("!!! Please make sure your application logs have been persisted on HDFS !!!");
        LogFetcherContext ctx = new LogFetcherContext(args[0]);
        LogFetcherClient client = ctx.getClient();
        client.process();
    }

}
