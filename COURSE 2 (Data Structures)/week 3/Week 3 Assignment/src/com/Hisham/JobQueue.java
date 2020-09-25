package com.Hisham;

import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class JobQueue {

    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    static class Thread {
        int index;
        long free_time;

        public Thread(int i) {
            index = i;
            free_time = 0;
        }

        public void addTime(long t) {
            free_time += t;
        }
    }

    Comparator<Thread> comparator = new Comparator<Thread>() {
        @Override
        public int compare(Thread o1, Thread o2) {
            if (o1.free_time == o2.free_time) {
                return Integer.compare(o1.index, o2.index);
            }
            return Long.compare(o1.free_time, o2.free_time);
        }
    };

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        Queue<Thread> pq = new PriorityQueue<>(jobs.length, comparator);
        for (int i = 0; i < numWorkers; i++) {
            pq.add(new Thread(i));
        }
        for (int i = 0; i < jobs.length; i++) {
            int time = jobs[i];
            Thread thread = pq.remove();
            assignedWorker[i] = thread.index;
            startTime[i] = thread.free_time;
            thread.addTime(time);
            pq.add(thread);
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}

