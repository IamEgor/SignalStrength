package pervacio.com.signalstrength;

import android.os.Handler;

public class ListenerAndHandler {

    public WorkerThread.WorkerTask mWorkerTask;
    public Handler.Callback mCallback;

    public ListenerAndHandler(WorkerThread.WorkerTask workerTask, Handler.Callback callback) {
        this.mWorkerTask = workerTask;
        this.mCallback = callback;
    }

}
