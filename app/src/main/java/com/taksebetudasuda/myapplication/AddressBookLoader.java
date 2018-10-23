package com.taksebetudasuda.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddressBookLoader {
    Context context;

    private static final String GET_ITEMS= "GetAll";
    private static final String LOGIN= "Hello";
    private static final String GET_PHOTO= "GetWPhoto";

    private static final Uri ENDPOINT = Uri.parse("https://contact.taxsee.com/Contacts.svc")
            .buildUpon()
            .build();



    public List<Item> downloadItems() {
        List<Item> i = new ArrayList<>();
        String url = buildUrl(GET_ITEMS);
        try {
            String jsonString = getUrlString(url);
            JSONObject jsonObject = new JSONObject(jsonString);
            parser(jsonObject,i,-1);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return i;
    }

    private void parser(JSONObject jsonObject, List<Item> itemList, int parentId) throws JSONException {
        int currentId = parentId;


        Item item = new Item();
        currentId = jsonObject.getInt("ID");
        item.setId(currentId);
        item.setName(jsonObject.getString("Name"));
        item.setParentId(parentId);
        if (jsonObject.has("Departments") || jsonObject.has("Employees")) {
            item.setHasChild(true);
        }
        if (jsonObject.has("Title")){
            item.setEmploye(true);
            Employe employe = new Employe();
            employe.setParentId(parentId);
            employe.setName(jsonObject.getString("Name"));
            if (jsonObject.has("Phone"))
                employe.setPhone(jsonObject.getString("Phone"));
            if (jsonObject.has("Email"))
                employe.setEmail(jsonObject.getString("Email"));
            employe.setId(currentId);
            employe.setTitle(jsonObject.getString("Title"));
            itemList.add(employe);
        }else
            itemList.add(item);


        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.get(key) instanceof JSONArray) {

                JSONArray array = jsonObject.getJSONArray(key);
                for (int i = 0; i < array.length(); i++)
                    parser(array.getJSONObject(i), itemList, currentId);
            }
        }
    }
    public Bitmap photoLoad(String id){
        String url = buildUrl(GET_PHOTO,id);
        byte[] bitmapBytes = new byte[0];
        try {
            bitmapBytes = getUrlBytes(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Bitmap bitmap = BitmapFactory
                .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        return bitmap;
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String tryLogin(String userName, String password){
        String url = buildUrl(LOGIN,userName,password);
        String result="";
        try {
            result = getUrlString(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildUrl(String method) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendPath(method)
                .appendQueryParameter("login", SharedPref.read(SharedPref.USER_NAME,null))
                .appendQueryParameter("password",  SharedPref.read(SharedPref.PASSWORD,null));

        return uriBuilder.build().toString();
    }

    private String buildUrl(String method,String id) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendPath(method)
                .appendQueryParameter("login", SharedPref.read(SharedPref.USER_NAME,null))
                .appendQueryParameter("password",  SharedPref.read(SharedPref.PASSWORD,null))
                .appendQueryParameter("id", id);

        return uriBuilder.build().toString();
    }

    private String buildUrl(String method,String login, String password) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendPath(method)
                .appendQueryParameter("login", login)
                .appendQueryParameter("password", password);

        return uriBuilder.build().toString();
    }

}
