package ntk.android.estate.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ntk.android.estate.model.Notify;


@Dao
public interface NotificationDoa {

    @Query("SELECT * FROM Notification ORDER BY ID DESC")
    List<Notify> All();

    @Query("SELECT * FROM Notification WHERE IsRead == 0 ORDER BY ID DESC")
    List<Notify> AllUnRead();

    @Insert
    void Insert(Notify notify);

    @Update
    void Update(Notify notify);

    @Delete
    void Delete(Notify notify);

}
