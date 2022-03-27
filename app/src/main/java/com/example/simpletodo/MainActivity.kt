package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity() : AppCompatActivity(), Parcelable {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. remove  the item from the list
                listOfTasks.removeAt(position)
                //2. notify adapter our date set has changed
                adapter.notifyDataSetChanged()

                saveItems()

            }

        }

       loadItems()


        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        adapter = TaskItemAdapter(listOfTasks,onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //user can enter a task and add it to the list
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. grab the text user has inputted into the addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the text to our list of tasks: listOfTask
            listOfTasks.add(userInputtedTask)

            //notify adapter
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()

        }
    }
    //save the data has created
    //read and write the file

    //get a file
    fun getDataFile(): File {

        //every line gonna be task
        return File(filesDir,"data.txt")
    }
    //load the item by reading every line in file
    fun loadItems(){
        try{
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException:IOException){
            ioException.printStackTrace()
        }
    }

    //save items by writing into file
    fun saveItems() = try{
        org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
    }catch (ioException:IOException){
        ioException.printStackTrace()

}

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }
}

