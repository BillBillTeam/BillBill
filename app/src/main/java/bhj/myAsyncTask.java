package bhj;

import android.os.AsyncTask;

/**
 * Created by ubuntu on 17-10-31.
 */

public class myAsyncTask extends AsyncTask {
    Callback mCallback;
    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.CallbackOnPerExecuiite();
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        mCallback.CallbackDoInBackground();

        return null;
    }
    @Override
    protected void onPostExecute(Object o) {
        mCallback.CallbackonPostExecute();
        super.onPostExecute(o);
    }

    public interface Callback {
        void CallbackOnPerExecuiite();
        void CallbackDoInBackground();
        void CallbackonPostExecute();

    }
}
