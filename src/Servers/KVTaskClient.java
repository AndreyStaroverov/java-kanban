package Servers;

import exception.ServerRegisterException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class KVTaskClient {

    private URI url;
    private HttpClient client;
    private String API_TOKEN;

    public KVTaskClient(URI url) throws ServerRegisterException {
            this.url = url;
            this.client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
            this.API_TOKEN = register();
    }


    public String register() throws ServerRegisterException {
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
                throw new ServerRegisterException();
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            throw new ServerRegisterException(e.getMessage(), e);
        }
    }

    public void put(String key, String json) {
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

    public String load(String key) {
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
                throw new ServerRegisterException();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            throw new ServerRegisterException(e.getMessage(), e);
        }
    }
}
