package pervacio.com.signalstrength.speedListeners;

import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.signalstrength.ISpeedListenerFinishCallback;
import pervacio.com.signalstrength.SpeedListenerHandler;

public class DownloadSpeedListener extends AbstractSpeedListener {

    protected static final String TAG = "[" + DownloadSpeedListener.class.getSimpleName() + "]";

    public DownloadSpeedListener(SpeedListenerHandler handler, ISpeedListenerFinishCallback mOnFinish) {
        super(handler, mOnFinish);
    }

    @Override
    public void onDownloadProgress(float percent, SpeedTestReport report) {
        onUpdate(report);
    }

    @Override
    public void onDownloadFinished(SpeedTestReport report) {
        onStop();
    }

}
