package Engine.DayNight;

public class Time {
    /* Instance variables */
    protected int minutesOfDay;
    protected int secondsOfDay;
    protected int secondsInHour = 3600;
    protected int minutesInHour = 60;
    protected int timeOfDay;

    public Time() {
        minutesOfDay = 1440;
        secondsOfDay = minutesOfDay * 60;
        timeOfDay = 0;
    }

    public int getTimeOfDay() {
        return timeOfDay;
    }

    public int getHoursOfDay() {
        return timeOfDay % secondsOfDay / secondsInHour;
    }

    public int getMinutesOfDay() {
        return (timeOfDay / 60) % minutesInHour;
    }

    public int getSecondsOfDay() {
        return timeOfDay % 60;
    }

}
