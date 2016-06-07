package com.axibase.tsd.api.transport.http;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public class HTTPClientPure implements HTTPClient {
    private static final Logger logger = LoggerFactory.getLogger(HTTPClientPure.class);
    private HttpClientContext context;
    private org.apache.http.client.HttpClient httpClient;
    private String url;

    public HTTPClientPure(String protocol, String host, int port, String login, String password) {
        httpClient = HttpClientBuilder.create().build();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(login, password));
        context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        url = protocol + "://" + host + ":" + port;
    }

    public AtsdHttpResponse get(String atsdMethod) throws IOException {
        String uri = url + atsdMethod;

        HttpGet request = new HttpGet(uri);
        logger.debug("> {}", uri);
        AtsdHttpResponse atsdHttpResponse = parseResponse(httpClient.execute(request, context));
        request.releaseConnection();
        return atsdHttpResponse;
    }

    public AtsdHttpResponse post(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;

        HttpPost request = new HttpPost(uri);
        setHeaders(request, null);
        request.setEntity(new StringEntity(body));
        logger.debug("> {}\n> {}", uri, body);
        AtsdHttpResponse atsdHttpResponse = parseResponse(httpClient.execute(request, context));
        request.releaseConnection();
        return atsdHttpResponse;
    }

    public AtsdHttpResponse put(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;

        HttpPut request = new HttpPut(uri);
        setHeaders(request, null);
        request.setEntity(new StringEntity(body));
        logger.debug("> {}\n> {}", uri, body);
        AtsdHttpResponse atsdHttpResponse = parseResponse(httpClient.execute(request, context));
        request.releaseConnection();
        return atsdHttpResponse;
    }

    public AtsdHttpResponse patch(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;
        HttpPatch request = new HttpPatch(uri);
        setHeaders(request, null);
        request.setEntity(new StringEntity(body));

        logger.debug("> {}\n> {}", uri, body);
        AtsdHttpResponse atsdHttpResponse = parseResponse(httpClient.execute(request, context));
        request.releaseConnection();
        return atsdHttpResponse;
    }

    public AtsdHttpResponse delete(String atsdMethod) throws IOException {
        String uri = url + atsdMethod;

        HttpDelete request = new HttpDelete(uri);
        setHeaders(request, null);
        logger.debug("> {}", uri);
        AtsdHttpResponse atsdHttpResponse = parseResponse(httpClient.execute(request, context));
        request.releaseConnection();
        return atsdHttpResponse;
    }

    private void setHeaders(HttpRequestBase request, Map<String, String> headers) {
        request.addHeader("Content Type", "application/json");
        if (null == headers) {
            return;
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());

        }
    }

    private AtsdHttpResponse parseResponse(HttpResponse httpResponse) throws IOException {
        AtsdHttpResponse atsdHttpResponse;
        int responseCode = httpResponse.getStatusLine().getStatusCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        atsdHttpResponse = new AtsdHttpResponse(responseCode, null, sb.toString());
        if (responseCode == 200) {
            logger.debug("< code: {}\n< header: {}\n< body: {}", atsdHttpResponse.getCode(), atsdHttpResponse.getHeaders(), atsdHttpResponse.getBody());
        } else {
            logger.warn("< code: {}\n< header: {}\n< body: {}", atsdHttpResponse.getCode(), atsdHttpResponse.getHeaders(), atsdHttpResponse.getBody());

        }
        return atsdHttpResponse;
    }
}
