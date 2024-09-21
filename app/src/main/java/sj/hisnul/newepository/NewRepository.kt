package sj.hisnul.newepository

import androidx.lifecycle.LiveData
import com.example.mushafconsolidated.Entities.NamesDetailsDao
import database.Dao.NamesDao


class NewRepository(

    val namesDetailsDao: NamesDetailsDao,
    val TheNames : NamesDao
) {

    val allnames =TheNames.ALLAH_NAMES_LISTS()
    fun getNames(TheName : Int)=namesDetailsDao.ALLAH_NAMES_DETAILS_DETAILSL(TheName)





}