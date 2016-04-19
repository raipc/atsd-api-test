package com.axibase.tsd.api.transport.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public class HTTPClientFluent implements HTTPClient {
    private static final Logger logger = LoggerFactory.getLogger(HTTPClientFluent.class);
    private Executor executor;
    private String url;

    public HTTPClientFluent(String protocol, String host, int port, String login, String password) {
        executor = Executor.newInstance()
                .auth(new HttpHost(host, port), login, password);
        url = protocol + "://" + host + ":" + port;
    }

    public AtsdHttpResponse get(String atsdMethod) throws IOException {
        String uri = url + atsdMethod;

        Request request = Request.Get(uri);
        setHeaders(request, null);
        Response response = executor.execute(
                request
        );

        return parseResponse(response);
    }

    public AtsdHttpResponse post(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;

        Request request = Request.Post(uri);
        setHeaders(request, null);
        request.body(new StringEntity(body));

        return parseResponse(executor.execute(request));
    }

    public AtsdHttpResponse put(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;

        Request request = Request.Put(uri);
        setHeaders(request, null);
        request.body(new StringEntity(body));

        return parseResponse(executor.execute(request));
    }

    public AtsdHttpResponse patch(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;
        Request request = Request.Patch(uri);
        setHeaders(request, null);
        request.body(new StringEntity(body));

        logger.debug("query url: {}", uri);
        logger.debug("query body: {}", body);
        return parseResponse(executor.execute(request));
    }

    public AtsdHttpResponse delete(String atsdMethod) throws IOException {
        String uri = url + atsdMethod;

        Request request = Request.Post(uri);
        setHeaders(request, null);

        return parseResponse(executor.execute(request));
    }

    private void setHeaders(Request request, Map<String, String> headers) {
        request.setHeader("Content Type", "application/json");
        if(null == headers) {
            return;
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.setHeader(header.getKey(), header.getValue());

        }
    }

    private AtsdHttpResponse parseResponse(Response response) throws IOException {
        AtsdHttpResponse atsdHttpResponse;
        HttpResponse httpResponse = response.returnResponse();
        int responseCode = httpResponse.getStatusLine().getStatusCode();

        if (responseCode != 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            atsdHttpResponse = new AtsdHttpResponse(responseCode, null, sb.toString());
        } else {
            atsdHttpResponse = new AtsdHttpResponse(200, null, response.returnContent().asString());
        }
        return atsdHttpResponse;
    }
}
