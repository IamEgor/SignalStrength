package pervacio.com.signalstrength.ui.fragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pervacio.com.signalstrength.ListenerAndHandlerWrapper;
import pervacio.com.signalstrength.R;
import pervacio.com.signalstrength.Router;
import pervacio.com.signalstrength.WorkerThread;
import pervacio.com.signalstrength.speedListeners.AbstractSpeedListener;
import pervacio.com.signalstrength.speedListeners.DownloadSpeedListener;
import pervacio.com.signalstrength.speedListeners.UploadSpeedListener;
import pervacio.com.signalstrength.utils.ActivityUtils;

import static pervacio.com.signalstrength.utils.Constants.FILE_SIZE;
import static pervacio.com.signalstrength.utils.Constants.FINISH;
import static pervacio.com.signalstrength.utils.Constants.PROGRESS;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_MAX_DURATION;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_REPORT_INTERVAL;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_SERVER_HOST;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_SERVER_PORT;
import static pervacio.com.signalstrength.utils.Constants.SPEED_TEST_SERVER_URI_DL;
import static pervacio.com.signalstrength.utils.Constants.START;

public class PlaceholderFragment2 extends Fragment implements Router.LastListenerFinished {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ProgressBar mWifiDownloadSpeedProgress;
    private ProgressBar mWifiUploadSpeedProgress;
    private TextView downloadRate;
    private TextView uploadRate;
    private ImageView restartDownload;
    private ImageView restartUpload;
    private TextView wifiStr;

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
        restartDownload = (ImageView) rootView.findViewById(R.id.restart_download);
        restartUpload = (ImageView) rootView.findViewById(R.id.restart_upload);
        wifiStr = (TextView) rootView.findViewById(R.id.wifiStr);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<ListenerAndHandlerWrapper> listenerAndHandlers = new ArrayList<>();
//        listenerAndHandlers.add(new ListenerAndHandlerWrapper(mTask1, mCallback1));
        listenerAndHandlers.add(new ListenerAndHandlerWrapper(mTask2, mCallback2));
        WorkerThread mWorkerThread = new WorkerThread(listenerAndHandlers, this);
        mWorkerThread.start();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                int rssi = wifiManager.getConnectionInfo().getRssi();
                int level = WifiManager.calculateSignalLevel(rssi, 5);
                ActivityUtils.runOnMainThread(() -> wifiStr.setText("Level is " + level + " out of 5. rssi = " + rssi));
            }
        };
        timer.schedule(task, 0, 3000);
    }

    @Override
    public void onLastObject() {
        System.gc();
//        ActivityUtils.scheduleOnMainThread(() -> {
        restartDownload.setImageResource(R.drawable.ic_replay_accent_48dp);
        restartUpload.setImageResource(R.drawable.ic_replay_accent_48dp);
        setclick listeners
//        });
    }

    private WorkerThread.WorkerTask mTask1 = (speedTestSocket, handler, onFinish) -> {
        speedTestSocket.addSpeedTestListener(new DownloadSpeedListener(handler, onFinish));
        speedTestSocket.startFixedDownload(
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
                restartDownload.setVisibility(View.GONE);
                break;
            case PROGRESS:
                mWifiDownloadSpeedProgress.setProgress((Integer) msg.obj);
                break;
            case FINISH:
                AbstractSpeedListener.Rate rate = (AbstractSpeedListener.Rate) msg.obj;
                downloadRate.setText(String.format(Locale.getDefault(), "Download rate is \nlast : %1$f\nmedian : %2$f", rate.mLast, rate.mMedian));
                mWifiDownloadSpeedProgress.setVisibility(View.INVISIBLE);
                mWifiDownloadSpeedProgress.setIndeterminate(false);
                restartDownload.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    };

    /**
     * TODO fix OOM exception when is large {@link pervacio.com.signalstrength.utils.Constants#FILE_SIZE}}.
     */
    private WorkerThread.WorkerTask mTask2 = (speedTestSocket, handler, onFinish) -> {
        speedTestSocket.addSpeedTestListener(new UploadSpeedListener(handler, onFinish));
//        speedTestSocket.startFixedUpload(
//                SPEED_TEST_SERVER_HOST,
//                SPEED_TEST_SERVER_PORT,
//                SPEED_TEST_SERVER_URI_DL,
//                FILE_SIZE,
//                SPEED_TEST_MAX_DURATION,
//                SPEED_TEST_REPORT_INTERVAL);
        speedTestSocket.startUpload(
                SPEED_TEST_SERVER_HOST,
                SPEED_TEST_SERVER_PORT,
                SPEED_TEST_SERVER_URI_DL,
                FILE_SIZE,
                SPEED_TEST_REPORT_INTERVAL);
    };

    private Handler.Callback mCallback2 = msg -> {
        switch (msg.arg1) {
            case START:
                mWifiUploadSpeedProgress.setVisibility(View.VISIBLE);
                restartUpload.setVisibility(View.GONE);
                break;
            case PROGRESS:
                mWifiUploadSpeedProgress.setProgress((Integer) msg.obj);
                break;
            case FINISH:
                AbstractSpeedListener.Rate rate = (AbstractSpeedListener.Rate) msg.obj;
                uploadRate.setText(String.format(Locale.getDefault(), "Upload rate is \nlast : %1$f\nmedian : %2$f", rate.mLast, rate.mMedian));
                mWifiUploadSpeedProgress.setVisibility(View.INVISIBLE);
                mWifiUploadSpeedProgress.setIndeterminate(false);
                restartUpload.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    };

}