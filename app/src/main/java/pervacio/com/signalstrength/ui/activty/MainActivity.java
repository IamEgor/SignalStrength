package pervacio.com.signalstrength.ui.activty;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import pervacio.com.signalmeasurer.PhoneSignalStateListener;
import pervacio.com.signalmeasurer.SignalCriteria;
import pervacio.com.signalstrength.R;

public class MainActivity extends AppCompatActivity implements
        PhoneSignalStateListener.SignalState {

    private TextView mOnRequestMeasurer;
    private TextView mRealTimeMeasurer;
    private GradientDrawable mRealTimeMeasurerDrawable;

    private PhoneSignalStateListener mPhoneStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mOnRequestMeasurer = (TextView) findViewById(R.id.signal_strength_on_request);
        mRealTimeMeasurer = (TextView) findViewById(R.id.signal_strength_real_time);
        mRealTimeMeasurerDrawable = (GradientDrawable) mRealTimeMeasurer.getBackground();
        mRealTimeMeasurerDrawable.setColor(0xFFFFFF);
        mPhoneStateListener = new PhoneSignalStateListener(this, this);
    }

    public void measureStrength(View view) {
        mOnRequestMeasurer.setText(getString(R.string.on_request_string, mPhoneStateListener.getAsu(), mPhoneStateListener.getDbm()));
    }

    @Override
    public void onSignalChanged(SignalCriteria criteria) {
        mRealTimeMeasurer.setText(getString(R.string.real_time_string, criteria.getAsu(), criteria.getTitle()));
        mRealTimeMeasurerDrawable.setColor(criteria.getColor());
    }

    @Override
    public void onFailedToMeasure(String message) {
        mRealTimeMeasurer.setText(message);
    }

}
