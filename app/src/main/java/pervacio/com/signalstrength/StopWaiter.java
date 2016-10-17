package pervacio.com.signalstrength;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.atomic.AtomicLong;

import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_REPORT_INTERVAL;

public class StopWaiter extends Thread {

    private AtomicLong atomicLong = new AtomicLong();
    private OnTimerStop mOnTimerStop;

    public StopWaiter(OnTimerStop onTimerStop) {
        this.mOnTimerStop = onTimerStop;
    }

    @Override
    public void run() {
        atomicLong.set(System.currentTimeMillis());
        long atomicLongGet = atomicLong.get();
        long startTime = System.currentTimeMillis();
        long currentTimeMillis = System.currentTimeMillis();
        Log.w("StopWaiter$" + currentThread().getId(), "run: atomicLongGet atomic " + (atomicLongGet - startTime) + ", current = " + (currentTimeMillis - startTime) + ", diff = " + (atomicLongGet - currentTimeMillis));
        while (Math.abs(atomicLongGet - currentTimeMillis) < SPEED_TEST_REPORT_INTERVAL * 2) {
            SystemClock.sleep(SPEED_TEST_REPORT_INTERVAL / 2);
            atomicLongGet = atomicLong.get();
            currentTimeMillis = System.currentTimeMillis();
            Log.w("StopWaiter$" + currentThread().getId(), "run: atomicLongGet atomic " + (atomicLongGet - startTime) + ", current = " + (currentTimeMillis - startTime) + ", diff = " + (atomicLongGet - currentTimeMillis));
        }
        if (mOnTimerStop != null) {
            mOnTimerStop.onTimerStop();
        }
    }

    public void updateTimer() {
        atomicLong.set(System.currentTimeMillis());
    }

    public interface OnTimerStop {
        void onTimerStop();
    }

}