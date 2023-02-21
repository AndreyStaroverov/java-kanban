package Servers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.TaskManager;
import exception.ManagerSaveException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.ConnectException;
import java.rmi.ServerError;
import java.time.Duration;
import java.util.HashMap;

public class KVTaskClient {

    private URI url;
    private HttpClient client;
    private String API_TOKEN;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        try {
            this.url = url;
            this.client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
            this.API_TOKEN = register();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String register() throws IOException, InterruptedException {
        String newUrlString = url + "/register";
        URI newUrl = URI.create(newUrlString);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(newUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
                throw new InterruptedException();
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            throw new InterruptedException();
        }
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
                System.out.println("Успешно добавлено");
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
                throw new InterruptedException();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        String newUrlString = url + "/load/" + key + "?API_TOKEN=" + API_TOKEN;
        URI newUrl = URI.create(newUrlString);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(newUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Успешно загружено");
                return response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return null;
    }
}
