package com.intel.bb.tool.log;

import java.util.List;
import java.util.Map;

/**
 * Created by yaoy on 5/9/2016.
 */
public interface LogDataWriter {

    void exec(Map<String, List<LogFetcherClient.HSResponse>> data);

}
