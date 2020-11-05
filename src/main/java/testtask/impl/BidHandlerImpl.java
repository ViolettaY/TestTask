package testtask.impl;

import lombok.extern.java.Log;
import testtask.BidHandler;
import testtask.dto.Bid;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log
public class BidHandlerImpl implements BidHandler {

    static Map<String, Queue<Bid>> queuesByType = new HashMap<>();

    @Override
    public void handle(List<Bid> bidList) {

        ExecutorService executorService = initializeExecutorService();

        updateQueuesWithNewTypes(bidList);

        bidList.forEach(bid -> {
            Runnable task = () -> {
                Queue<Bid> queue = queuesByType.get(bid.getType());
                addToQueue(queue, bid);
            };
            executorService.submit(task);
        });

        shutDownWithWait(executorService);

        logQueuesData();
    }


    private synchronized void addToQueue(Queue<Bid> queue, Bid bid){
        queue.add(bid);
        log.info("Added new bid id: " + bid.getId() + ", timestamp: " + bid.getTimestamp() + ", queue “name”: " + bid.getType() +", payload: " + new String(Base64.getDecoder().decode(bid.getPayLoad())));
    }


    private void updateQueuesWithNewTypes(List<Bid> bids){
        List<String> bidTypes = bids.stream().map(bid -> bid.getType()).distinct().collect(Collectors.toList());

        bidTypes.forEach(type -> {
            Queue<Bid> queue = new LinkedList<>();
            queuesByType.putIfAbsent(type, queue);
        });
    }

    private void logQueuesData(){
        queuesByType.entrySet().forEach(queue -> {
            log.info("Queue: " + queue.getKey() + " contains " + queue.getValue());
        });
    }


    private ExecutorService initializeExecutorService(){
        return Executors.newCachedThreadPool();
    }


    private void shutDownWithWait(ExecutorService executorService){
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
