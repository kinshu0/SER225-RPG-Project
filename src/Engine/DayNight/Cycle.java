package Engine.DayNight;

public class Cycle extends Time {
    protected State state;
    private static final int MIDNIGHT = 0;
    private static final int NOON = 12;
    private static final int SUNSET = 1120 * 60;
    private static final int SUNRISE = 400 * 60;

    public Cycle() {
        super();
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        state = newState;
    }

    public void updateState() {
        // while (timeOfDay >= 0 && timeOfDay <= minutesOfDay * minutesInHour) {
        if (timeOfDay >= 0 && timeOfDay <= minutesOfDay * minutesInHour) {
            int h = getHoursOfDay();
            int m = getMinutesOfDay();
            int s = getSecondsOfDay();
            System.out.printf("%02d:%02d:%02d (%s)\n", h, m, s, state);

            if (timeOfDay == 0) {
                timeOfDay = MIDNIGHT;
            }
            if (timeOfDay > 0 && timeOfDay < 360 * minutesInHour) {
                setState(State.NIGHT);
            } else if (timeOfDay > 360 * minutesInHour && timeOfDay < SUNRISE) {
                setState(State.DAWN);
            } else if (timeOfDay > SUNRISE && timeOfDay < SUNSET) {
                setState(State.DAY);
            } else if (timeOfDay > SUNSET && timeOfDay < 1160 * minutesInHour) {
                setState(State.DUSK);
            } else {
                setState(State.NIGHT);
            }

        }
    }
}
