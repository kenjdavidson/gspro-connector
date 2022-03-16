package kjd.gspro.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow;

/**
 * Provides standard publishing functionality.
 * <p>
 * 
 * @author kenjdavidson
 */
public class Publisher<T> implements Flow.Publisher<T> {
    private Map<Flow.Subscriber<? super T>, Long> subscribers = new ConcurrentHashMap<>();

    public int subscribers() {
        return subscribers.size();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        if (subscribers.containsKey(subscriber)) {
            subscriber.onError(new IllegalStateException("Already subscribed"));
        } else {
            subscribers.put(subscriber, Long.valueOf(-1));
            subscriber.onSubscribe(new Subscription(subscriber));
        }
    }    

    public void publish(T value) {
        for (Map.Entry<Flow.Subscriber<? super T>,Long> entry : subscribers.entrySet()) {
            Long count = entry.getValue();

            if (count == -1) {
                entry.getKey().onNext(value);
            } else if (count > 0) {
                entry.getKey().onNext(value);
                entry.setValue(count-1);
            }
        }
    }

    public void error(Throwable t) {
        for (Map.Entry<Flow.Subscriber<? super T>,Long> entry : subscribers.entrySet()) {
            entry.getKey().onError(t);
        }
    }

    public void complete() {
        for (Map.Entry<Flow.Subscriber<? super T>,Long> entry : subscribers.entrySet()) {
            entry.getKey().onComplete();
        }

        subscribers.clear();
    }

    public class Subscription implements Flow.Subscription {
        private Flow.Subscriber<? super T> subscriber;

        public Subscription(Flow.Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            subscribers.put(subscriber, n);
        }

        @Override
        public void cancel() {
            subscribers.remove(subscriber);
        }

    }
}
