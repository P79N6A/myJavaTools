package com.commons.http;

import org.springframework.http.HttpEntity;

import java.net.URISyntaxException;

public class SimpleHttpClient {

    /**
     * GET
     */
    public static void get(String url, String username, String apiKey, Map<String, String> header) throws URISyntaxException {
        HttpGet get = new HttpGet();
        get.setURI(new URI(url));
        call(get, username, apiKey, header);
    }

    /**
     * POST
     */
    public static void post(String url, String username, String apiKey, Map<String, String> header, byte[] body) throws URISyntaxException {
        HttpPost post = new HttpPost();
        post.setURI(new URI(url));
        if (body != null) {
            ByteArrayEntity se = new ByteArrayEntity(body);
            post.setEntity(se);
        }
        call(post, username, apiKey, header);
    }

    /**
     * PUT
     */
    public static void put(String url, String username, String apiKey, Map<String, String> header) throws URISyntaxException {
        HttpPut get = new HttpPut();
        get.setURI(new URI(url));
        call(get, username, apiKey, header);
    }

    /**
     * DELETE
     */
    public static void delete(String url, String username, String apiKey, Map<String, String> header) throws URISyntaxException {
        HttpDelete get = new HttpDelete();
        get.setURI(new URI(url));
        call(get, username, apiKey, header);
    }

    /**
     * http call
     */
    private static void call(HttpRequestBase method, String username, String apikey, Map<String, String> header) {
        DefaultHttpClient client = null;
        try {
            client = new DefaultHttpClient();
            addHeader(method, username, apikey, header);
            HttpResponse response = client.execute(method);
            if (response.getStatusLine().getStatusCode() != 200) {
                method.abort();
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String str = EntityUtils.toString(entity);
                System.out.println(str);
            }
        } catch (Exception e) {
            method.abort();
            e.printStackTrace();
        }
    }


    private static void addHeader(AbstractHttpMessage client, String username, String apikey, Map<String, String> headers) throws Exception {
        if (headers != null) {
            for (Iterator<Entry<String, String>> it = headers.entrySet().iterator(); it.hasNext(); ) {
                Entry<String, String> entry = it.next();
                client.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Date date = new Date();
        String dateString = getDate(date);
        String authoriztion = encode(dateString, username, apikey);
        client.addHeader("Date", dateString);
        client.addHeader("Authorization", "Basic " + authoriztion);
    }


}
