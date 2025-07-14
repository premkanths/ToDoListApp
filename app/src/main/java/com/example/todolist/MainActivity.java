package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextTask;
    Button buttonAdd;
    ListView listViewTasks;
    ArrayList<String> taskList;
    ArrayAdapter<String> adapter;

    SharedPreferences sharedPreferences;
    String PREF_KEY = "task_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);

        sharedPreferences = getSharedPreferences("ToDoPrefs", MODE_PRIVATE);

        taskList = loadTasks();

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.taskText, taskList);
        listViewTasks.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString().trim();
                if (!task.isEmpty()) {
                    taskList.add(task);
                    adapter.notifyDataSetChanged();
                    saveTasks();
                    editTextTask.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskList.remove(position);
            adapter.notifyDataSetChanged();
            saveTasks();
            return true;
        });
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray(taskList);
        editor.putString(PREF_KEY, jsonArray.toString());
        editor.apply();
    }

    private ArrayList<String> loadTasks() {
        ArrayList<String> list = new ArrayList<>();
        String json = sharedPreferences.getString(PREF_KEY, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.getString(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
