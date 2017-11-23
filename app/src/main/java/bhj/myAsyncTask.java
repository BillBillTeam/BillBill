package bhj;

import android.os.AsyncTask;

/**
 * Created by Mary Kt on 17-10-31.
 * 对原生异步类进行包装 变成回调函数实现
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
        /**
         * 预先准备部分
         */
        void CallbackOnPerExecute();

        /**
         * 在后台执行部分
         */
        void CallbackDoInBackground();

        /**
         * 执行结束部分呢
         */
        void CallbackOnPostExecute();

    }
}
