package movies.nano.udacity.com.udacitypopularmovies.utility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public class MyVolley {

    private static MyVolley mInstance;
    private Context activityContext;
    private RequestQueue mRequestQueue;

    private MyVolley(Context mContext) {

        activityContext = mContext.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }


    public static synchronized MyVolley getInstance(Context mContext){

        if(mInstance == null)
            mInstance = new MyVolley(mContext);

        return mInstance;

    }

    public RequestQueue getRequestQueue(){

        if(mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(activityContext);

        return mRequestQueue;

    }

    public <T> void addToRequestQueue(Request<T> request){

        getRequestQueue().add(request);

    }

}

