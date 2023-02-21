package Servers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;

public class KVTaskClient {

    private URI url;
    private HttpClient client;
    private HashMap<String, String> mapManager = new HashMap<>();
    private String API_TOKEN;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        this.url = url;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.API_TOKEN = register();
    }


    public String register() throws IOException, InterruptedException {
        String newUrlString = url + "/register";
        URI newUrl = URI.create(newUrlString);
        String token = null;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(newUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                token = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return token;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        String newUrlString = url + "/save/" + key + "?API_TOKEN=" + API_TOKEN;
        URI newUrl = URI.create(newUrlString);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(newUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                mapManager.put(key, json);
                System.out.println("Успешно добавлено");
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        String newUrlString = url + "/load/" + key + "?API_TOKEN=" + API_TOKEN;
        URI newUrl = URI.create(newUrlString);

        String bodyOfManager = null;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(newUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                bodyOfManager = mapManager.get(key);
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return bodyOfManager;
    }
}
