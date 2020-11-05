package testtask;

import io.reactivex.Observable;
import testtask.dto.Bid;
import testtask.impl.BidHandlerImpl;
import testtask.impl.DataLoaderImpl2;
import testtask.impl.DataLoaderImpl;

import java.util.*;

public class Main {

    private static final DataLoader dataLoader = new DataLoaderImpl2(); // new DataLoaderImpl();

    private static final BidHandler handler = new BidHandlerImpl();

    public static void main(String[] args) {

        Observable<List<Bid>> bids = dataLoader.loadData();

        bids.subscribe(handler::handle);
    }

}
