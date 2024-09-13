package sj.hisnul.newepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mushafconsolidated.Utils
import com.example.utility.QuranGrammarApplication
import database.entity.AllahNames
import kotlinx.coroutines.launch
import sj.hisnul.entity.AllahNamesDetails

class NewDuaModel(
    private val newrepository: NewRepository=Graph.repository
) :ViewModel(){


    val util = Utils(QuranGrammarApplication.context)

    private var AllahSWT: LiveData<List<AllahNames>> = MutableLiveData()
    private var Namesd: LiveData<List<AllahNamesDetails>> = MutableLiveData()
    fun getNames(): LiveData<List<AllahNames>> {


        viewModelScope.launch {
            AllahSWT=        newrepository.allnames
        }


        return AllahSWT
    }

    fun AllahNames(cat: Int): LiveData<List<AllahNamesDetails>> {

        viewModelScope.launch {
            Namesd= newrepository.getNames(cat)
        }


        return Namesd
    }









}

