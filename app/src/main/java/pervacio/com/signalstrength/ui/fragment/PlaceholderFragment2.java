package pervacio.com.signalstrength.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import pervacio.com.signalstrength.ListenerAndHandler;
import pervacio.com.signalstrength.R;
import pervacio.com.signalstrength.WorkerThread;
import pervacio.com.signalstrength.speedListeners.AbstractSpeedListener;
import pervacio.com.signalstrength.speedListeners.DownloadSpeedListener;
import pervacio.com.signalstrength.speedListeners.UploadSpeedListener;

import static pervacio.com.signalstrength.utils.Constants.FINISH;
import static pervacio.com.signalstrength.utils.Constants.PROGRESS;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_MAX_DURATION;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_REPORT_INTERVAL;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_SERVER_HOST;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_SERVER_PORT;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_SERVER_URI_DL;
import static pervacio.com.signalstrength.utils.Constants.START;

public class PlaceholderFragment2 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ProgressBar mWifiDownloadSpeedProgress;
    private ProgressBar mWifiUploadSpeedProgress;
    private TextView downloadRate;
    private TextView uploadRate;

    public PlaceholderFragment2() {
    }

    public static PlaceholderFragment2 newInstance(int sectionNumber) {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_pager2, container, false);
        mWifiDownloadSpeedProgress = (ProgressBar) rootView.findViewById(R.id.wifiDownloadSpeedProgress);
        mWifiUploadSpeedProgress = (ProgressBar) rootView.findViewById(R.id.wifiUploadSpeedProgress);
        downloadRate = (TextView) rootView.findViewById(R.id.download_rate);
        uploadRate = (TextView) rootView.findViewById(R.id.upload_rate);
        ArrayList<ListenerAndHandler> listenerAndHandlers = new ArrayList<>();
        listenerAndHandlers.add(new ListenerAndHandler(mTask1, mCallback1));
        listenerAndHandlers.add(new ListenerAndHandler(mTask2, mCallback2));
        WorkerThread mWorkerThread = new WorkerThread(listenerAndHandlers);
        mWorkerThread.start();
        return rootView;
    }

    private WorkerThread.WorkerTask mTask1 = (speedTestSocket, handler) -> {
        speedTestSocket.addSpeedTestListener(new DownloadSpeedListener(handler));
        speedTestSocket.startFixedUpload(
                SPEED_TEST_SERVER_HOST,
                SPEED_TEST_SERVER_PORT,
                SPEED_TEST_SERVER_URI_DL,
                SPEED_TEST_MAX_DURATION,
                SPEED_TEST_REPORT_INTERVAL);
    };

    private Handler.Callback mCallback1 = msg -> {
        switch (msg.arg1) {
            case START:
                mWifiDownloadSpeedProgress.setVisibility(View.VISIBLE);
                break;
            case PROGRESS:
                mWifiDownloadSpeedProgress.setProgress((Integer) msg.obj);
                break;
            case FINISH:
                AbstractSpeedListener.Rate rate = (AbstractSpeedListener.Rate) msg.obj;
                downloadRate.setText(String.format(Locale.getDefault(), "Download rate is \nlast : %1$f\nmedian : %2$f", rate.mLast, rate.mMedian));
                mWifiDownloadSpeedProgress.setVisibility(View.INVISIBLE);
                mWifiDownloadSpeedProgress.setIndeterminate(false);
                break;
        }
        return true;
    };

    private WorkerThread.WorkerTask mTask2 = (speedTestSocket, handler) -> {
        speedTestSocket.addSpeedTestListener(new UploadSpeedListener(handler));
        speedTestSocket.startFixedUpload(
                SPEED_TEST_SERVER_HOST,
                SPEED_TEST_SERVER_PORT,
                SPEED_TEST_SERVER_URI_DL,
                SPEED_TEST_MAX_DURATION ,
                SPEED_TEST_REPORT_INTERVAL);
    };

    private Handler.Callback mCallback2 = msg -> {
        switch (msg.arg1) {
            case START:
                mWifiUploadSpeedProgress.setVisibility(View.VISIBLE);
                break;
            case PROGRESS:
                mWifiUploadSpeedProgress.setProgress((Integer) msg.obj);
                break;
            case FINISH:
                AbstractSpeedListener.Rate rate = (AbstractSpeedListener.Rate) msg.obj;
                uploadRate.setText(String.format(Locale.getDefault(), "Download rate is \nlast : %1$f\nmedian : %2$f", rate.mLast, rate.mMedian));
                mWifiUploadSpeedProgress.setVisibility(View.INVISIBLE);
                mWifiUploadSpeedProgress.setIndeterminate(false);
                break;
        }
        return true;
    };

}