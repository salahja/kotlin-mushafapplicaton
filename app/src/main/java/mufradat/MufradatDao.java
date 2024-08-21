package mufradat;

import androidx.room.Dao;
import androidx.room.Query;

import  mufradat.MufradatEntity;

import java.util.List;

@Dao
public interface MufradatDao {

    @Query("SELECT * FROM mufradat_verses WHERE SurahNumber=:id")
    List<MufradatEntity> getShaikTafseer(int id);

    @Query("SELECT * FROM mufradat_verses WHERE SurahNumber=:id and AyahNumber=:aid")
    List<MufradatEntity> getShaikTafseerAya(int id,int aid);
}
