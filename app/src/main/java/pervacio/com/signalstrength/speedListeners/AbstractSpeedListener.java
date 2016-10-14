package pervacio.com.signalstrength.speedListeners;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.speedtest.ISpeedTestListener;
import fr.bmartel.speedtest.SpeedTestError;
import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.signalstrength.IOnFinish;
import pervacio.com.signalstrength.MyHandler;
import pervacio.com.signalstrength.utils.CommonUtils;
import pervacio.com.signalstrength.utils.Constants;

import static pervacio.com.signalstrength.utils.Constants.FINISH;

public abstract class AbstractSpeedListener implements ISpeedTestListener {

    protected List<Float> mList;
    protected MyHandler mHandler;
    protected long realStartTime;
    protected IOnFinish mOnFinish;

    public AbstractSpeedListener(MyHandler handler) {
        this.mHandler = handler;
        mList = new ArrayList<>();
    }

    public AbstractSpeedListener(MyHandler handler, IOnFinish mOnFinish) {
        this.mHandler = handler;
        this.mOnFinish = mOnFinish;
        mList = new ArrayList<>();
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

    protected void onUpdate(SpeedTestReport report) {
        if (mHandler == null) {
            return;
        }
        if (realStartTime == 0) {
            realStartTime = report.getReportTime();
            mHandler.publish(Constants.START);
        } else {
            mList.add(report.getTransferRateBit().floatValue() / 1024 / 1024);
        }
        mHandler.publish(Constants.PROGRESS, (int) ((report.getReportTime() - realStartTime) / 100) + 1);
    }

    protected void onStop() {
        if (mHandler != null) {
            mHandler.publish(FINISH, new Rate(mList.get(mList.size() - 1), CommonUtils.getMedian(mList)));
        }
        if (mOnFinish != null){
            mOnFinish.call();
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
