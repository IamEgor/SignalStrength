package pervacio.com.signalstrength;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import pervacio.com.signalstrength.utils.Constants;

public class MyHandler extends Handler {

    public MyHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }


    public void publish(@Constants.LoadingStatus int loadingStatus) {
        publish(loadingStatus, null);
    }

    public void publish(@Constants.LoadingStatus int loadingStatus, Object object) {
        Message message = new Message();
        message.arg1 = loadingStatus;
        message.obj = object;
        sendMessage(message);
    }

}