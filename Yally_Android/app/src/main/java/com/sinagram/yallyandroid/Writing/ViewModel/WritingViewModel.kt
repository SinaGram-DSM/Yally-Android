package com.sinagram.yallyandroid.Writing.ViewModel

import androidx.lifecycle.ViewModel
import com.sinagram.yallyandroid.Writing.Data.WritingRepository
import com.sinagram.yallyandroid.Writing.Data.WritingRequest

class WritingViewModel:ViewModel(){
    private val repository = WritingRepository()

    suspend fun writing(part: WritingRequest){
        if(part!=null){
            repository.writing(part)
        }
        else {
            println("part==null")
        }
    }

}