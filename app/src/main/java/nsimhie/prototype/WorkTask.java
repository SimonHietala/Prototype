package nsimhie.prototype;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by nsimhie on 2015-10-22.
 */
public class WorkTask
{
    private int id;

    private String task;
    private String location;
    private String startTime;
    private String stopStime;
    private String time;
    private float timeInSeconds;
    private String notes;
    private String gps;
    private boolean inMotion;
    private boolean edited;

    public WorkTask(String task, String location, String notes, String startTime, String stopTime) {
        this.task = task;
        this.location = location;
        this.notes = notes;
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

    public void recalculateTime()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = df.parse(getStartTime());
            Date stop = df.parse(getStopStime());
            long duration = stop.getTime() - start.getTime();
            setTimeInSeconds(TimeUnit.MILLISECONDS.toSeconds(duration));
            this.setTime(String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            ));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean startStopRight(String start, String stop)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dStart = null;
        Date dStop = null;
        long duration = 0;

        try {
            dStart = df.parse(start);
            dStop = df.parse(stop);
            duration = dStop.getTime() - dStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (duration >= 0);
    }

    public boolean checkDateFormat(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsed;
        try {
            parsed = df.parse(date);
        }
        catch (ParseException e) {
            return false;
            //e.printStackTrace();
        }
        return true;
    }

    public String getCurrentTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public float getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(float timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }
}
