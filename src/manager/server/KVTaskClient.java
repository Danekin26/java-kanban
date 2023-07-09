package manager.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private String url;
    private String token;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }

    public KVTaskClient(String url) {
        this.url = url;
        this.token = register(url);
    }

    private String register(String url) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            token = response.body();
        } catch (InterruptedException | IOException exc) {
            System.out.println("Не удалось зарегистрировать токен.");
        }
        return token;
    }

    public void put(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8087/save/" + key + "?API_TOKEN=" + token);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (InterruptedException | IOException e){
            System.out.println("Не удалось сохранить состояние менеджера.");
        }
    }

    public String load(String key){
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8087/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (InterruptedException | IOException e){
            System.out.println("Не удалось восстановить состояние менеджера.");
        }
        return response != null ? response.body() : "load()";
    }


}
