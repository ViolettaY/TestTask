package testtask.impl;

import lombok.extern.java.Log;
import testtask.BidHandler;
import testtask.dto.Bid;
import testtask.queue.BidQueue;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Log
//Each queue in separate thread
public class BidHandlerImpl2 implements BidHandler {

    static Map<String, BidQueue> queuesByType = new HashMap<>();

    ExecutorService executorService = initializeExecutorService();

    @Override
    public void handle(List<Bid> bidList) {

        Map<String, List<Bid>> groupedBids = bidList.stream().collect(Collectors.groupingBy(Bid::getType));

        groupedBids.entrySet().forEach(group -> {

            BidQueue queue = createAndSubmitQueue(group.getKey());

            queue.put(group.getValue());

        });

        logQueuesData();
    }

    private BidQueue createAndSubmitQueue(String queueName) {
        BidQueue queue;

        if(queuesByType.containsKey(queueName)){
            queue = queuesByType.get(queueName);
        }

        else {
            queue = new BidQueue(queueName);
            queuesByType.putIfAbsent(queueName, queue);
            executorService.submit(queue);
        }
        return queue;
    }


    private void logQueuesData(){
        queuesByType.entrySet().forEach(queue -> {
            log.info("Queue: " + queue.getKey() + " contains " + queue.getValue());
        });
    }

    private ExecutorService initializeExecutorService(){
        return Executors.newCachedThreadPool();
    }

}
