package pervacio.com.signalstrength.speedListeners;

import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.signalstrength.MyHandler;
import pervacio.com.signalstrength.utils.CommonUtils;
import pervacio.com.signalstrength.utils.Constants;

import static pervacio.com.signalstrength.utils.Constants.FINISH;

public class DownloadSpeedListener extends AbstractSpeedListener {

    protected static final String TAG = "[" + DownloadSpeedListener.class.getSimpleName() + "]";

    public DownloadSpeedListener(MyHandler myHandler) {
        super(myHandler);
    }

    @Override
    public void onDownloadProgress(float percent, SpeedTestReport report) {
        onUpdate(report);
    }

    @Override
    public void onDownloadFinished(SpeedTestReport report) {
        onStop();
    }

    @Override
    public void onInterruption() {
        onStop();
    }

}
