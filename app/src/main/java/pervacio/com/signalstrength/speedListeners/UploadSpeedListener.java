package pervacio.com.signalstrength.speedListeners;

import android.util.Log;

import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.signalstrength.ISpeedListenerFinishCallback;
import pervacio.com.signalstrength.SpeedListenerHandler;

public class UploadSpeedListener extends AbstractSpeedListener {

    protected static final String TAG = "[" + UploadSpeedListener.class.getSimpleName() + "]";

    public UploadSpeedListener(SpeedListenerHandler handler, ISpeedListenerFinishCallback mOnFinish) {
        super(handler, mOnFinish);
    }

    @Override
    public void onUploadProgress(float percent, SpeedTestReport report) {
        onUpdate(report);
    }

    @Override
    public void onUploadFinished(SpeedTestReport report) {
        Log.d(TAG, "onUploadFinished() called with: report = [" + report + "]");
        onStop();
    }



}
