package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class Bakery {
    private int numThreads = 10;
    volatile ArrayList<Integer> number = new ArrayList<>(numThreads);
    volatile ArrayList<Boolean> entering = new ArrayList<>(numThreads);

    public void lock(int i) {
        entering.set(i, true);
        int maxNumber = Collections.max(number);
        number.set(i, maxNumber+1);
        entering.set(i, false);
        for (int j = 0; j < number.size(); j++) {
                while (entering.get(j)) ;
                while ((number.get(j) != 0) && (number.get(i) > number.get(j) || (number.get(i) == number.get(j) && j < i)));
            }
    }

    public void unlock(int i){
        number.set(i, 0);
    }

    public class r implements Runnable {
       private int j;

        public r(int j) {
            this.j = j;
        }

        public void run() {
            while (true) {
                lock(j);
                try {
                    System.out.printf("t" + j + " is sleeping%n");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                unlock(j);
            }
        }
    }

    Bakery(){
        for (int i = 0; i < numThreads; i++){
            number.add(0);
            entering.add(false);
        }
         r[] r = new r[numThreads];
        for (int j = 0; j<number.size(); j++){
            r[j] = new r(j);
            new Thread(r[j]).start();
        }
    }


}
