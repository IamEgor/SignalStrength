package pervacio.com.signalstrength;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

import fr.bmartel.speedtest.SpeedTestSocket;

public class Router implements IOnFinish {

    private List<ListenerAndHandler> mListenerAndHandlers;
    private SpeedTestSocket mSpeedTestSocket;
    private int mSerialNumber;

    public Router(List<ListenerAndHandler> listenerAndHandlers) {
        mListenerAndHandlers = listenerAndHandlers;
        mSpeedTestSocket = new SpeedTestSocket();
    }

    @Override
    public void onFinish() {
        Log.w("Router", "onFinish");
        startTask(mSerialNumber++);
        SystemClock.sleep(1000);
    }

    public void route() {
        startTask(mSerialNumber++);
    }

    private void startTask(int serialNumber){
        if (mListenerAndHandlers.size() > serialNumber) {
            ListenerAndHandler listenerAndHandler = mListenerAndHandlers.get(serialNumber);

            WorkerThread.WorkerTask mWorkerTask = listenerAndHandler.mWorkerTask;
            Handler.Callback mCallback = listenerAndHandler.mCallback;
            MyHandler handler = new MyHandler(Looper.getMainLooper(), mCallback);

            mWorkerTask.execute(mSpeedTestSocket, handler, this);
        }
    }

}
