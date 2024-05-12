package com.example.todo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.utils.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDailogCloseListner{
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private DBHelper myDataBase;
    private List<DBToDoModel> list ;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingIcon);
        myDataBase = new DBHelper(MainActivity.this);
        list = new ArrayList<>();
        adapter = new ToDoAdapter(myDataBase,MainActivity.this);
        list = myDataBase.getAllTasks();
        Collections.sort(list);
        adapter.setTasks(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewSlider(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDailogClose(DialogInterface dialogInterface) {
        list = myDataBase.getAllTasks();
        Collections.sort(list);
        adapter.setTasks(list);
        adapter.notifyDataSetChanged();
    }
}