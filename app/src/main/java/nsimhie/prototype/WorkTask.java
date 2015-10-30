package nsimhie.prototype;

import java.sql.Time;

/**
 * Created by nsimhie on 2015-10-22.
 */
public class WorkTask
{
    private int id;
    private String task;
    private String location;
    private String freeText;
    private String startTime;
    private String stopStime;
    private String time;
    private String notes;
    private String gps;
    private boolean inMotion;
    private boolean edited;

    public WorkTask(String task, String location, String freeText, String startTime, String stopTime) {
        this.task = task;
        this.location = location;
        this.freeText = freeText;
        this.startTime = startTime;
        this.stopStime = stopTime;
    }

    public WorkTask(){

    }

    public int getId() {
        return id;
    }
    
    public String getTask() {
        return task;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStopStime() {
        return stopStime;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {
        return notes;
    }

    public String getGps() {
        return gps;
    }

    public boolean isInMotion() {
        return inMotion;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStopStime(String stopStime) {
        this.stopStime = stopStime;
    }

    public void setInMotion(boolean inMotion) {
        this.inMotion = inMotion;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setId(int id) {
        this.id = id;
    }
}
