package com.svm.yaplacaklarlistesi;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class veritabaniIslemleri {

    private String URL = "http://192.168.0.27:8080/api/v1/";
    private RestTemplate restTemplate = new RestTemplate();

    public List<liste> liste(){
        try {
            return restTemplate.exchange(URL + "listele", HttpMethod.GET, null, new ParameterizedTypeReference<List<liste>>(){}).getBody();
        }catch (Exception e){
            return null;
        }
    }

    public boolean ekle(liste liste){
        try {
            Map<String,String> values = new HashMap<String,String>();
            values.put("yapilacak", liste.getYapilacak());
            values.put("durum", String.valueOf(liste.getDurum()));
            JSONObject jsonObject = new JSONObject(values);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
            restTemplate.postForEntity(URL+"liste",entity,null);
            return true;
        }catch (Exception e){
            System.out.println("HATA!!!!: "+e.getMessage());
            return false;
        }
    }

    public boolean sil(int id){
        try {
            restTemplate.delete(URL+"liste/"+id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean duzenle (liste liste){
        try {
            Map<String, String> values = new HashMap<String, String>();
            values.put("yapilacak",liste.getYapilacak());
            values.put("durum", String.valueOf(liste.getDurum()));
            JSONObject jsonObject = new JSONObject(values);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
            restTemplate.put(URL+"listeler/"+liste.getId(), entity);
            System.out.println("DENEME : "+entity);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
