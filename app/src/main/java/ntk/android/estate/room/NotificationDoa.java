package ntk.android.estate.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
