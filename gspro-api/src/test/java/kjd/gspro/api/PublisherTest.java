package kjd.gspro.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PublisherTest {
    
    Publisher<Status> publisher;

    @Before
    public void setup() {
        publisher = new Publisher<Status>();
    }

    @Test
    public void subscribe_withoutDuplicate_isSuccessful() {        
        Subscriber<Status> subscriber = subscriber(s -> s.request(-1));
        Subscriber<Status> subscriberSpy = spy(subscriber);

        publisher.subscribe(subscriberSpy);

        verify(subscriberSpy, times(1)).onSubscribe(Mockito.any());
        assertEquals(publisher.subscribers(), 1);
    }

    @Test
    public void subscribe_withDuplicate_error() {
        Subscriber<Status> subscriber = subscriber(s -> s.request(-1));
        Subscriber<Status> subscriberSpy = spy(subscriber);

        publisher.subscribe(subscriberSpy);
        publisher.subscribe(subscriberSpy);

        verify(subscriberSpy, times(1)).onSubscribe(Mockito.any());
        verify(subscriberSpy, times(1)).onError(Mockito.any());
        assertEquals(1, publisher.subscribers());
    }

    @Test
    public void onNext_calledRequestedTimes() {
        Subscriber<Status> subscriber = subscriber(s -> s.request(1l));
        Subscriber<Status> subscriberSpy = spy(subscriber);
        Status status = mock(Status.class);
        
        publisher.subscribe(subscriberSpy);
        publisher.publish(status);
        publisher.publish(status);

        verify(subscriberSpy, times(1)).onSubscribe(Mockito.any());
        verify(subscriberSpy, times(1)).onNext(status);
    }

    @Test
    public void onNext_calledUnlimited_ish() {
        Subscriber<Status> subscriber = subscriber(s -> s.request(-1l));
        Subscriber<Status> subscriberSpy = spy(subscriber);
        Status status = mock(Status.class);
        
        publisher.subscribe(subscriberSpy);
        publisher.publish(status);
        publisher.publish(status);

        verify(subscriberSpy, times(1)).onSubscribe(Mockito.any());
        verify(subscriberSpy, times(2)).onNext(status);
    }

    @Test
    public void cancel_shouldRemoveSubscriber() {
        Subscriber<Status> subscriber = subscriber(s -> s.cancel());
        Subscriber<Status> subscriberSpy = spy(subscriber);

        publisher.subscribe(subscriberSpy);

        verify(subscriberSpy, times(1)).onSubscribe(Mockito.any());
        assertEquals(publisher.subscribers(), 0);
    }

    Subscriber<Status> subscriber(Consumer<Subscription> onSubscribe) {
        return new Subscriber<>(){
            @Override
            public void onSubscribe(Subscription subscription) {
                onSubscribe.accept(subscription);
            }

            @Override
            public void onNext(Status item) {
                // Implement with Provider<Status>
            }

            @Override
            public void onError(Throwable throwable) {
                // Implement with Provider<Status>
            }

            @Override
            public void onComplete() {
                // Implement with Provider<Status>
            }
        };
    }
}
