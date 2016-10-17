package pervacio.com.signalstrength;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import fr.bmartel.speedtest.SpeedTestSocket;
import pervacio.com.signalstrength.speedListeners.AbstractSpeedListener;

public class Router implements ISpeedListenerFinishCallback {

    private List<ListenerAndHandlerWrapper> mListenerAndHandlers;
    private SpeedTestSocket mSpeedTestSocket;
    private LastListenerFinished mLastListenerFinished;
    private int mSerialNumber;

    public Router(List<ListenerAndHandlerWrapper> listenerAndHandlers, LastListenerFinished lastListenerFinished) {
        mListenerAndHandlers = listenerAndHandlers;
        mSpeedTestSocket = new SpeedTestSocket();
        mLastListenerFinished = lastListenerFinished;
    }

    @Override
    public void onSpeedListenerFinish(AbstractSpeedListener speedListener) {
        Log.w("Router", "onSpeedListenerFinish");
        startTask(mSerialNumber++);
        if (mLastListenerFinished != null) {
            new Handler(Looper.getMainLooper()).post(() -> mLastListenerFinished.onLastObject());
        }
//        SystemClock.sleep(1000);
    }

    public void route() {
        startTask(mSerialNumber++);
    }

    /**
     * Create handlers from callbacks and start tasks one by one
     *
     * @param serialNumber number of the task
     */
    private void startTask(int serialNumber) {
        if (mListenerAndHandlers.size() > serialNumber) {
            ListenerAndHandlerWrapper listenerAndHandler = mListenerAndHandlers.get(serialNumber);

            WorkerThread.WorkerTask mWorkerTask = listenerAndHandler.mWorkerTask;
            Handler.Callback mCallback = listenerAndHandler.mCallback;
            SpeedListenerHandler handler = new SpeedListenerHandler(Looper.getMainLooper(), mCallback);

            mWorkerTask.execute(mSpeedTestSocket, handler, this);
        }
    }

    public interface LastListenerFinished {
        void onLastObject();
    }

}
