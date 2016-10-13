package pervacio.com.signalstrength;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;

import java.util.List;

import fr.bmartel.speedtest.SpeedTestSocket;

import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_MAX_DURATION;

public class WorkerThread extends HandlerThread {

    public static final String TAG = WorkerThread.class.getSimpleName();

    private List<ListenerAndHandler> mListenerAndHandlers;

    public WorkerThread(List<ListenerAndHandler> listenerAndHandlers) {
        super(TAG);
        mListenerAndHandlers = listenerAndHandlers;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        for (ListenerAndHandler listenerAndHandler : mListenerAndHandlers) {
            WorkerTask mWorkerTask = listenerAndHandler.mWorkerTask;
            Handler.Callback mCallback = listenerAndHandler.mCallback;
            MyHandler handler = new MyHandler(Looper.getMainLooper(), mCallback);
            mWorkerTask.execute(speedTestSocket, handler);
//            SystemClock.sleep(SPEED_TEST_MAX_DURATION + 1000);
        }
    }

    public interface WorkerTask {
        void execute(SpeedTestSocket speedTestSocket, MyHandler handler);
    }

}
