package me.NickP0is0n.jTestStudent.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

//класс с информацией и ответами студента
public class Student implements Serializable {
    private static final long serialVersionUID = -2298260858404281332L;

    private String name;
    private String surName;
    private String grade;

    private Date startTime;
    private Date finishTime;

    private ArrayList<Integer[]> tasksResults = new ArrayList<>(); //дин. массив с результатами

    public Student(String name, String surName, String grade) {
        this.name = name;
        this.surName = surName;
        this.grade = grade;
    }

    public void addTask(int taskNumber, int goodNumber) //добавляем выполненное задание с количеством пройденных тестов
    {
        Integer[] data = {taskNumber, goodNumber};
        tasksResults.add(data);
    }

    public int[] getDoneTasks()
    {
        int[] doneTasks = new int[tasksResults.size()];
        for (int i = 0; i < tasksResults.size(); i++) {
            doneTasks[tasksResults.get(i)[0]] = tasksResults.get(i)[1];
        }
        return doneTasks;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getGrade() {
        return grade;
    }

    public ArrayList<Integer[]> getTasksResults() {
        return tasksResults;
    }

}
