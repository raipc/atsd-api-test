package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.registry.SeriesRegistry;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class CSVInsertMethod extends SeriesMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String METHOD_CSV_INSERT = "/series/csv/";

    protected Boolean csvInsert(String entity, String csv, Map<String, String> tags) throws IOException {

        if (entity != null) {
            SeriesRegistry.getInstance().registerEntity(entity);
        }

        StringBuilder uri = new StringBuilder(METHOD_CSV_INSERT);
        uri.append(entity);
        if (tags != null && tags.size() > 0) {
            uri.append("?");
            for (String key : tags.keySet()) {
                uri.append("&").append(key).append("=").append(tags.get(key));
            }
        }

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, uri.toString(), csv);
        if (200 == response.getCode()) {
            logger.debug("CSV looks inserted");
        } else {
            logger.error("Fail to insert csv");
        }
        return 200 == response.getCode();
    }

    protected Boolean csvInsert(String entity, String csv) throws IOException {
        return csvInsert(entity, csv, null);
    }
}
