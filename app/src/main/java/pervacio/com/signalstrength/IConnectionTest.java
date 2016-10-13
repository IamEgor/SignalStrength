package pervacio.com.signalstrength;

public interface IConnectionTest {

    void onStart();

    void onPublishProgress(int progress);

    void onStop(float rateLast, float rateMedian);
}
