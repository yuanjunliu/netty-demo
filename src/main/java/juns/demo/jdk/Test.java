package juns.demo.jdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class Test {
    Executor executor = Executors.newFixedThreadPool(4);

    private class InnerClass {
        List<String> sharedList = new ArrayList<>();

        public void thread(String x) {
            System.out.println(sharedList.hashCode());
            synchronized (sharedList) {
                if (!sharedList.contains(x)) {
                    System.out.println("add " + x);
                    sharedList.add(x);
                }
            }

        }
    }

    public void test() throws InterruptedException {
        InnerClass innerClass = new InnerClass();
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    innerClass.thread(j + "");
                }
            });
        }
        TimeUnit.SECONDS.sleep(5);
//        System.out.println(sharedList);
    }

    public void thread(final List<String> sharedList, String x) {
        System.out.println(sharedList.hashCode());
        synchronized (sharedList) {
            if (!sharedList.contains(x)) {
                System.out.println("add " + x);
                sharedList.add(x);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println((int) Math.ceil(1 * 50.0 / 100));
//        Test t = new Test();
//        Thread t1 = new Thread(() -> {
//            try {
//                t.test();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        Thread t2 = new Thread(() -> {
//            try {
//                t.test();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
    }
}
