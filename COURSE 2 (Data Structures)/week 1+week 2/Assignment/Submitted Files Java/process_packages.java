import java.util.*;

class Request {

    public int arrival_time;
    public int process_time;

    public Request(int arrival_time, int process_time) {
        this.arrival_time = arrival_time;
        this.process_time = process_time;
    }
}

class Response {

    public boolean dropped;
    public int start_time;

    public Response(boolean dropped, int start_time) {
        this.dropped = dropped;
        this.start_time = start_time;
    }
}

class Buffer {

    private final int size_;
    private final Deque<Integer> finish_time_;

    public Buffer(int size) {
        this.size_ = size;
        this.finish_time_ = new LinkedList<>();
    }

    public Response Process(Request request) {
        // write your code here
        if (finish_time_.isEmpty() && size_ > 0) {
            finish_time_.add(request.arrival_time + request.process_time);
            return new Response(false, request.arrival_time);
        } else {
            while (!finish_time_.isEmpty()) {
                if (finish_time_.getFirst() <= request.arrival_time) {
                    finish_time_.removeFirst();
                } else {
                    break;
                }
            }
            if (finish_time_.size() == size_) {
                return new Response(true, -1);
            } else if (finish_time_.isEmpty()) {
                finish_time_.add(request.arrival_time + request.process_time);
                return new Response(false, request.arrival_time);
            } else {
                int t = finish_time_.getLast();
                finish_time_.add(t + request.process_time);
                return new Response(false, t);
            }
        }
    }
}

class process_packages {
    private static ArrayList<Request> ReadQueries(Scanner scanner) {
        int requests_count = scanner.nextInt();
        ArrayList<Request> requests = new ArrayList<>();
        for (int i = 0; i < requests_count; ++i) {
            int arrival_time = scanner.nextInt();
            int process_time = scanner.nextInt();
            requests.add(new Request(arrival_time, process_time));
        }
        return requests;
    }

    private static ArrayList<Response> ProcessRequests(ArrayList<Request> requests, Buffer buffer) {
        ArrayList<Response> responses = new ArrayList<>();
        for (Request request : requests) {
            responses.add(buffer.Process(request));
        }
        return responses;
    }

    private static void PrintResponses(ArrayList<Response> responses) {
        for (Response response : responses) {
            if (response.dropped) {
                System.out.println(-1);
            } else {
                System.out.println(response.start_time);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int buffer_max_size = scanner.nextInt();
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        ArrayList<Response> responses = ProcessRequests(requests, buffer);
        PrintResponses(responses);
    }
}

