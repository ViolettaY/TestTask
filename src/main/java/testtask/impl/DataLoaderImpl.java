package testtask.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import testtask.constants.Constants;
import testtask.DataLoader;
import testtask.dto.Bid;
import testtask.dto.BidObject;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//parses the whole JSON file
public class DataLoaderImpl implements DataLoader{

    public Observable<List<Bid>> loadData() {
        Subject<List<Bid>> subject = PublishSubject.create();
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                () -> subject.onNext(parseJSON()), 0, 1, TimeUnit.MINUTES);
        return subject;
    }

    private List<Bid> parseJSON(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<BidObject> bidObjects = Arrays.asList(mapper.readValue(Paths.get(Constants.JSON_FILE_PATH).toFile(), BidObject[].class));
            return bidObjects.stream().map(BidObject::getBid).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
