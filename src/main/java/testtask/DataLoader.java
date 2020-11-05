package testtask;

import io.reactivex.Observable;
import testtask.dto.Bid;
import java.util.List;

public interface DataLoader {

    Observable<List<Bid>> loadData();

}
