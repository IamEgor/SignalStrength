package pervacio.com.signalstrength;

import android.os.HandlerThread;

import java.util.List;

import fr.bmartel.speedtest.SpeedTestSocket;

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
//        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
//        for (ListenerAndHandler listenerAndHandler : mListenerAndHandlers) {
//            WorkerTask mWorkerTask = listenerAndHandler.mWorkerTask;
//            Handler.Callback mCallback = listenerAndHandler.mCallback;
//            MyHandler handler = new MyHandler(Looper.getMainLooper(), mCallback);
//            mWorkerTask.execute(speedTestSocket, handler, null);
////            SystemClock.sleep(SPEED_TEST_MAX_DURATION + 2000);
//        }
        final Router router = new Router(mListenerAndHandlers);
        router.route();
    }

    public interface WorkerTask {
        void execute(SpeedTestSocket speedTestSocket, MyHandler handler, IOnFinish onFinish);
    }

}
