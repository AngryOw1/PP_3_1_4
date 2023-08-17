package preproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import preproject.models.User;

import java.util.List;

@Component
public class Communication {
    private final String URL = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;
    private String sessionId = null;
    private String responsesBody = "";

    @Autowired
    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public List<User> getAllUsers() {
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        List<User> allUsers = responseEntity.getBody();

        // Получение заголовков ответа
        HttpHeaders headers = responseEntity.getHeaders();

        // Получение значения cookie из заголовка Set-Cookie
        List<String> setCookieHeaders = headers.get(HttpHeaders.SET_COOKIE);
        if (setCookieHeaders != null) {
            for (String setCookieHeader : setCookieHeaders) {
                if (setCookieHeader.startsWith("JSESSIONID=")) {
                    sessionId = setCookieHeader.split(";")[0].substring(11);
                    break;
                }
            }
        }
        return allUsers;
    }

    private void addSessionId(HttpHeaders headers){
        if (sessionId != null) {
            headers.add("Cookie", "JSESSIONID=" + sessionId);
        }
    }

    public void createUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        addSessionId(headers);

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
        responsesBody += responseEntity.getBody();
    }

    public void editUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        addSessionId(headers);

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.PUT, requestEntity, String.class);
        responsesBody += responseEntity.getBody();
    }

    public void deleteUser(Long id) {
        HttpHeaders headers = new HttpHeaders();
        addSessionId(headers);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, requestEntity, String.class);
        responsesBody += responseEntity.getBody();
    }

    public String getResponsesBody() {
        return responsesBody;
    }
}
