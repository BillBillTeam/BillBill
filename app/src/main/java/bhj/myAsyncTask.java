package bhj;

import android.os.AsyncTask;

/**
 * Created by ubuntu on 17-10-31.
 */

public class myAsyncTask extends AsyncTask {
    private Callback mCallback;
    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.CallbackOnPerExecute();
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        mCallback.CallbackDoInBackground();
        return null;
    }
    @Override
    protected void onPostExecute(Object o) {
        mCallback.CallbackOnPostExecute();

    }

    public interface Callback {
        void CallbackOnPerExecute();
        void CallbackDoInBackground();
        void CallbackOnPostExecute();

    }
}
