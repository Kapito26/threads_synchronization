import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class main {
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                String R = generateRoute("RLRFR", 100);
                int count = R.length() - R.replace(R.valueOf('R'), "").length();
                //System.out.println(R);
                synchronized (sizeToFreq) {
                    int ii = 1;
                    if (sizeToFreq.containsKey(count)) {
                        ii = sizeToFreq.get(count) + 1;
                    }
                    sizeToFreq.put(count, ii);
                    sizeToFreq.notify();
                }
            }).start();

        }
        new Thread(() -> {
            int sum = 0;
            synchronized (sizeToFreq) {
                for (int f : sizeToFreq.values()) {
                    sum += f;
                }
                if (sum < 1000) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int maxValueInMap = (Collections.max(sizeToFreq.values()));
                for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {  // Iterate through HashMap
                    if (entry.getValue() == maxValueInMap)
                        System.out.println("Самое частое количество повторений "
                                + entry.getKey()
                                + " (встретилось "
                                + maxValueInMap + " раз)");
                }
                System.out.println("Другие размеры:");
                for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {  // Iterate through HashMap
                    if (entry.getValue() < maxValueInMap)
                        System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
                }
                //System.out.println(sizeToFreq);
            }
        }).start();
    }
}
