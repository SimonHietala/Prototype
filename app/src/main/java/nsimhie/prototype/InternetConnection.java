package nsimhie.prototype;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nsimhie on 2015-11-05.
 */
public class InternetConnection
{
    final private String BASE_URL = "http://10.10.10.100:5000";
    //final private String BASE_URL = "http://192.168.55.55:5000";
    private Activity activity;

    public void checkConnectionState()
    {

    }

    public InternetConnection(Activity activity)
    {
        this.activity = activity;
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
                HttpGet request = new HttpGet(BASE_URL + url);
                final HttpResponse response;

                try
                {

                    response = httpClient.execute(request);
                    final String s = IOUtils.toString(response.getEntity().getContent());
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            Toast.makeText(activity, s, Toast.LENGTH_LONG ).show();
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
                HttpPost httpPost = new HttpPost(BASE_URL + url);

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
                } catch (Exception e) {
                    // Log exception
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
