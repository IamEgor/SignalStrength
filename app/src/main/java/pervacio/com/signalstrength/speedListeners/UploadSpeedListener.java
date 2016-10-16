package pervacio.com.signalstrength.speedListeners;

import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.signalstrength.IOnFinish;
import pervacio.com.signalstrength.MyHandler;

public class UploadSpeedListener extends AbstractSpeedListener {

    protected static final String TAG = "[" + UploadSpeedListener.class.getSimpleName() + "]";

    public UploadSpeedListener(MyHandler myHandler) {
        super(myHandler);
    }

    public UploadSpeedListener(MyHandler handler, IOnFinish mOnFinish) {
        super(handler, mOnFinish);
    }

    @Override
    public void onUploadProgress(float percent, SpeedTestReport report) {
        onUpdate(report);
    }

    @Override
    public void onUploadFinished(SpeedTestReport report) {
        onStop();
    }

    @Override
    public void onInterruption() {
        onStop();
    }

}
