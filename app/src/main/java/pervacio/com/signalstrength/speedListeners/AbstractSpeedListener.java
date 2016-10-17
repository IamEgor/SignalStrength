package pervacio.com.signalstrength.speedListeners;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.speedtest.ISpeedTestListener;
import fr.bmartel.speedtest.SpeedTestError;
import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.signalstrength.ISpeedListenerFinishCallback;
import pervacio.com.signalstrength.SpeedListenerHandler;
import pervacio.com.signalstrength.StopWaiter;
import pervacio.com.signalstrength.utils.CommonUtils;
import pervacio.com.signalstrength.utils.Constants;

import static pervacio.com.signalstrength.utils.Constants.FINISH;

public abstract class AbstractSpeedListener implements
        ISpeedTestListener,
        StopWaiter.OnTimerStop {

    private static final String TAG = AbstractSpeedListener.class.getSimpleName();

    private List<Float> mList;
    private SpeedListenerHandler mHandler;
    private long realStartTime;
    private ISpeedListenerFinishCallback mOnFinish;
    private StopWaiter mStopWaiter;

    protected AbstractSpeedListener(SpeedListenerHandler handler, ISpeedListenerFinishCallback mOnFinish) {
        this.mHandler = handler;
        this.mOnFinish = mOnFinish;
        mList = new ArrayList<>();
        mStopWaiter = new StopWaiter(this);
    }

    @Override
    public void onDownloadFinished(SpeedTestReport report) {
    }

    @Override
    public void onDownloadProgress(float percent, SpeedTestReport report) {
    }

    @Override
    public void onDownloadError(SpeedTestError speedTestError, String errorMessage) {
    }

    @Override
    public void onUploadFinished(SpeedTestReport report) {
        Log.d(TAG, "onUploadFinished() called with: report = [" + report + "]");
    }

    @Override
    public void onUploadError(SpeedTestError speedTestError, String errorMessage) {
    }

    @Override
    public void onUploadProgress(float percent, SpeedTestReport report) {
    }

    @Override
    public void onInterruption() {
    }

    @Override
    public void onTimerStop() {
        Log.d(TAG, "onTimerStop() called");
        onStop();
    }

    protected void onUpdate(SpeedTestReport report) {
        Log.d(TAG, "onUpdate() called with: report = [" + report.getTransferRateBit().floatValue() / 1000 / 1000 + "]" + report.getProgressPercent());
        if (mHandler == null) {
            return;
        }
        if (realStartTime == 0) {
            realStartTime = report.getReportTime();
            mHandler.publish(Constants.START);
            mStopWaiter.start();
        } else if (report.getProgressPercent() == 100) {
            onStop();
        } else {
            float e = report.getTransferRateBit().floatValue() / 1000 / 1000;
            mStopWaiter.updateTimer();
            if (e != 0) {
                mList.add(e);
                mHandler.publish(Constants.PROGRESS, (int) ((report.getReportTime() - realStartTime) / 100) + 1);
            }
        }
    }

    protected void onStop() {
        Log.d(TAG, "onStop() called");
        if (mHandler != null) {
            mHandler.publish(FINISH, new Rate(mList.get(mList.size() - 1), CommonUtils.getMedian(mList)));
        }
        if (mOnFinish != null) {
            mOnFinish.onSpeedListenerFinish(this);
        }
    }

    protected void publish(@Constants.LoadingStatus int loadingStatus, int value) {
        publish(loadingStatus, value, null);
    }

    protected void publish(@Constants.LoadingStatus int loadingStatus, int value, Object object) {
        Message message = new Message();
        message.arg1 = loadingStatus;
        message.arg2 = value;
        message.obj = object;
        mHandler.sendMessage(message);
    }

    public static class Rate {

        public float mLast;
        public float mMedian;

        public Rate(float last, float median) {
            this.mLast = last;
            this.mMedian = median;
        }

        @Override
        public String toString() {
            return "Rate{" +
                    "last=" + mLast +
                    ", median=" + mMedian +
                    '}';
        }

    }

}
