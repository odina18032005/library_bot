package uz.pdp.backend.service;

import uz.pdp.backend.jsonModels.currency.CurrencyInfo;
import uz.pdp.backend.utils.GsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyService {

    private String BASE_URL = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/";
    private HttpClient client;

    public CurrencyService() {
        this.client = getHttpClient();
    }

    public CurrencyInfo[] getAllCurrenciesByNow() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = getRequest(BASE_URL);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200) {
            String json = response.body();
            CurrencyInfo[] currencyInfos = GsonUtil.gson.fromJson(json, CurrencyInfo[].class);
            return currencyInfos;
        }else {
            throw new RuntimeException("Something wrong");
        }
    }

    public CurrencyInfo[] getCurrencyByName(String name) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = getRequest(BASE_URL+name+"/");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200) {
            String json = response.body();
            CurrencyInfo[] currencyInfos = GsonUtil.gson.fromJson(json, CurrencyInfo[].class);
            return currencyInfos;
        }else {
            throw new RuntimeException("Something wrong");
        }
    }

    public CurrencyInfo[] getCurrencyByNameAndDate(String name, Date date) throws URISyntaxException, IOException, InterruptedException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(date);
        HttpRequest request = getRequest(BASE_URL+name+"/"+dateStr+"/");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200) {
            String json = response.body();
            CurrencyInfo[] currencyInfos = GsonUtil.gson.fromJson(json, CurrencyInfo[].class);
            return currencyInfos;
        }else {
            throw new RuntimeException("Something wrong");
        }
    }


    public HttpRequest getRequest(String URL) throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(URL)).GET().build();
    }
    public HttpClient getHttpClient(){
        return  HttpClient.newBuilder()
                .build();
    }
}
