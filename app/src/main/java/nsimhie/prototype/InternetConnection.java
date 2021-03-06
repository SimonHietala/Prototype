package nsimhie.prototype;

import android.app.Activity;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Observable;

public class InternetConnection extends Observable {
    final private String BASE_URL;
    private Activity activity;
    private String response = null;
    private String responseUrl = "";

    public String getResponseUrl()
    {
        return responseUrl;
    }

    public String getMyResponse()
    {
        return response;
    }

    public void setMyResponse(String myResponse, String url)
    {
        response = myResponse;
        responseUrl = url;
        setChanged();
        notifyObservers();
    }

    public InternetConnection(Activity activity)
    {
        this.activity = activity;
        this.BASE_URL = activity.getResources().getString(R.string.BASE_URL);
    }

    public void getRequest(final String url)
    {

        Thread t = new Thread(new Runnable()
        {
            String s;
            @Override
            public void run()
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet(BASE_URL + url.replace(" ", "%20"));
                final HttpResponse response;

                try
                {

                    response = httpClient.execute(request);
                    final String s = IOUtils.toString(response.getEntity().getContent());

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setMyResponse(s,url);
                        }
                    });
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void postRequest(final JSONObject jsonObject, final String url)
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpClient httpClient = new DefaultHttpClient();

                // Form url for posting
                HttpPost httpPost = new HttpPost(BASE_URL + url.replace(" ", "%20"));

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
                    entity.setContentType("application/json");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpPost.setEntity(entity);


                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    Log.d("Http Post Response:", response.getStatusLine().toString());
                    Log.d("Http Post Response:", IOUtils.toString(response.getEntity().getContent()));

                } catch (Exception e) {
                    // Log exception
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void putRequest(final JSONObject jsonObject, final String url)
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpClient httpClient = new DefaultHttpClient();

                // Form url for posting
                HttpPut httpPut = new HttpPut(BASE_URL + url.replace(" ", "%20"));

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
                    entity.setContentType("application/json");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpPut.setEntity(entity);


                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPut);
                    // write response to log
                    Log.d("Http Put Response:", response.getStatusLine().toString());
                    Log.d("Http Put Response:", IOUtils.toString(response.getEntity().getContent()));

                } catch (Exception e) {
                    // Log exception
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

}
