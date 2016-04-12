package com.axibase.tsd.api.transport.http;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
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
public class HttpClientPure implements Driver {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientFluent.class);
    private HttpClientContext context;
    private HttpClient httpClient;
    private String url;

    public HttpClientPure(String protocol, String host, int port, String login, String password) {
        httpClient = HttpClientBuilder.create().build();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(login, password));
        context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        url = protocol + "://" + host + ":" + port;
    }

    public AtsdResponse get(String atsdMethod) throws IOException {
        String uri = url + atsdMethod;

        HttpGet request = new HttpGet(uri);
        return parseResponse(httpClient.execute(request, context));
    }

    public AtsdResponse post(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;

        HttpPost request = new HttpPost(uri);
        setHeaders(request, null);
        request.setEntity(new StringEntity(body));

        return parseResponse(httpClient.execute(request, context));
    }

    public AtsdResponse put(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;

        HttpPut request = new HttpPut(uri);
        setHeaders(request, null);
        request.setEntity(new StringEntity(body));

        return parseResponse(httpClient.execute(request, context));
    }

    public AtsdResponse patch(String atsdMethod, String body) throws IOException {
        String uri = url + atsdMethod;
        HttpPatch request = new HttpPatch(uri);
        setHeaders(request, null);
        request.setEntity(new StringEntity(body));

        logger.debug("query url: {}", uri);
        logger.debug("query body: {}", body);
        return parseResponse(httpClient.execute(request, context));
    }

    public AtsdResponse delete(String atsdMethod) throws IOException {
        String uri = url + atsdMethod;

        HttpDelete request = new HttpDelete(uri);
        setHeaders(request, null);

        return parseResponse(httpClient.execute(request, context));
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

    private AtsdResponse parseResponse(HttpResponse httpResponse) throws IOException {
        AtsdResponse atsdResponse;
        int responseCode = httpResponse.getStatusLine().getStatusCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        atsdResponse = new AtsdResponse(responseCode, null, sb.toString());
        return atsdResponse;
    }
}
