package com.appsnipp.loginsamples.adapter;

import com.appsnipp.loginsamples.model.Task_model.TaskModel;

public interface TaskItemClicked {
    void onItemClickedTask(int position, TaskModel model);
}
