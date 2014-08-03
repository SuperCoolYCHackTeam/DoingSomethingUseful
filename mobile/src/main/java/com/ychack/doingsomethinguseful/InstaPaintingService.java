package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by sohilveljee on 8/2/14.
 */
public class InstaPaintingService {
    private static String ordersUri;
    private static String ordersId;
    private static DefaultHttpClient httpClient;
    private static HttpPost httpPost;
    private static HttpResponse response;
    private static HttpEntity entity;
    private static String responseString;

    public static String instapaintIt(String imagePath) {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
                "4b5de5f85844a6fafd4e39804bf8c20c520f4c88",
                "777e4a9b5048e225fe0d5f322904e969fcf812e3");
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("https://instapainting.com/api/v1/orders");
        httpPost.addHeader(BasicScheme.authenticate(creds, "US-ASCII", false));

        try {
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
            JSONObject obj = new JSONObject(responseString);
            ordersUri = obj.getString("payment_uri");
            ordersId = obj.getString("id");
            System.out.println(ordersId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("https://instapainting.com/api/v1/orders/"+ordersId+"/items");
        httpPost.addHeader(BasicScheme.authenticate(creds, "US-ASCII", false));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addPart("file", new FileBody(new File(imagePath)));
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageLength = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        String imageWidth = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
        builder.addTextBody("height", String.valueOf(Integer.parseInt(imageLength)/10));
        builder.addTextBody("width", String.valueOf(Integer.parseInt(imageWidth)/10));
        builder.addTextBody("coords", "0,0,"+imageWidth+","+imageLength);
        httpPost.setEntity(builder.build());

        try {
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return ordersUri;
    }

}
