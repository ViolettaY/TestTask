package testtask.queue;
import io.reactivex.subjects.BehaviorSubject;
import lombok.ToString;
import lombok.extern.java.Log;
import testtask.dto.Bid;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@ToString
@Log
public class BidQueue implements Runnable {

    private final Queue<Bid> bidsQueue;
    private final BehaviorSubject<List<Bid>> bidsToAdd;
    private final String name;

    public BidQueue(String name) {
        this.bidsQueue = new LinkedList<>();
        this.bidsToAdd = BehaviorSubject.create();
        this.name = name;
    }


    @Override
    public void run() {
        bidsToAdd.subscribe(this::addToQueue);
    }

    private void addToQueue(List<Bid> bids){
        this.bidsQueue.addAll(bids);
        bids.forEach(bid -> {
            log.info("Added new bid id: " + bid.getId() + ", timestamp: " + bid.getTimestamp() +
                    ", queue “name”: " + name +", payload: " + new String(Base64.getDecoder().decode(bid.getPayLoad())));
        });
    }

    public void put(List<Bid> bids) {
        this.bidsToAdd.onNext(bids);
    }
}
