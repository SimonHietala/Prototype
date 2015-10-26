package nsimhie.prototype;

import java.sql.Time;

/**
 * Created by nsimhie on 2015-10-22.
 */
public class WorkTask
{
    private String taskName;
    private String locationName;
    private String freeText;
    private Time startTime;
    private Time stopStime;
    private boolean inMotion;

    public WorkTask(String activityName, String locationName, String freeText, Time startTime, Time stopStime) {
        this.taskName = activityName;
        this.locationName = locationName;
        this.freeText = freeText;
        this.startTime = startTime;
        this.stopStime = stopStime;
    }

    public WorkTask(){

    }
    
    public String getActivityName() {
        return taskName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getFreeText() {
        return freeText;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getStopStime() {
        return stopStime;
    }


    public boolean isInMotion() {
        return inMotion;
    }

    public void setActivityName(String activityName) {
        this.taskName = activityName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setStopStime(Time stopStime) {
        this.stopStime = stopStime;
    }

    public void setInMotion(boolean inMotion) {
        this.inMotion = inMotion;
    }
}
