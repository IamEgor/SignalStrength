package pervacio.com.signalstrength.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static float getJitter(List<Float> latencies) {
        float jitter = 0f;
        int latenciesCount = latencies.size();
        for (int i = 1; i < latenciesCount; i++) {
            jitter += Math.abs(latencies.get(i) - latencies.get(i - 1));
        }
        return jitter / latenciesCount;
    }

    public static double calculateAverage(List<Float> values) {
        float average = 0f;
        for (float value : values) {
            average += value;
        }
        return average / values.size();
    }

    public static float getMedian(List<Float> values) {
        Collections.sort(values);
        if (values.size() % 2 == 0) {
            return (values.get(values.size() / 2 - 1) + values.get(values.size() / 2)) / 2;
        } else {
            return values.get(values.size() / 2);
        }
    }

    public static List<String> splitOnChars(String text) {
        final int stringSize = 300;
        List<String> strings = new ArrayList<>(text.length() / stringSize + 1);
        int index = 0;
        while (index < text.length()) {
            strings.add(text.substring(index, Math.min(index + stringSize, text.length())));
            index += stringSize;
        }
        return strings;
    }

    public static void logLongMessage(String tag, String longMessage) {
        for (String message : splitOnChars(longMessage)) {
            Log.w(tag, message);
        }
    }

    public static int generateId() {
        return sNextGeneratedId.get();
    }

}
