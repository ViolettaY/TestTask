package testtask.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import testtask.DataLoader;
import testtask.constants.Constants;
import testtask.dto.Bid;
import testtask.dto.BidObject;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//parses only objects, added to the bottom of JSON
public class DataLoaderImpl2 implements DataLoader{

    private int position = 0;

    public Observable<List<Bid>> loadData() {
        Subject<List<Bid>> subject = PublishSubject.create();
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                () -> subject.onNext(parseJSON()), 0, 1, TimeUnit.MINUTES);
        return subject;
    }

    private List<Bid> parseJSON() {

        try (FileReader reader = new FileReader(Constants.JSON_FILE_PATH)){

            StringBuilder builder = new StringBuilder();

            reader.skip(position);

            while (true){
                int result = reader.read();
                if (result==-1){
                    break;
                }
                builder.append((char) result);
                position += 1;
            }

            return mapJsonStringToBids(builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<Bid> mapJsonStringToBids(String jsonString){

        ObjectMapper mapper = new ObjectMapper();

        jsonString = jsonString.trim().startsWith("{") ? "[".concat(jsonString) : jsonString;

        List<BidObject> bidObjects = null;

        try {
            bidObjects = (jsonString.length() == 0) ? Collections.emptyList(): Arrays.asList(mapper.readValue(jsonString, BidObject[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return bidObjects.stream().map(BidObject::getBid).collect(Collectors.toList());
    }

}
