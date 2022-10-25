package Engine.DayNight;

public class RunState {
    public Cycle c = new Cycle();
    private int UPDATE = 1000;
    double drawInterval = 1000000000 / UPDATE;
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;

    public RunState() {
        System.out.println("construcring runState");
    }

    public void runCycle() {
        // while (true) {
        if (c.timeOfDay > c.minutesOfDay * c.minutesInHour) {
            c.timeOfDay -= c.minutesOfDay * c.secondsInHour;
        }
        currentTime = System.nanoTime();
        System.out.println("Last time" + lastTime + " currentTime " + currentTime);
        delta += (currentTime - lastTime) / drawInterval;
        System.out.println("DEBUG delta " + delta);
        lastTime = currentTime;
        if (delta >= 1) {
            int d = (int) delta;
            c.timeOfDay += d;
            c.updateState();
            delta -= d;
        }
        // }
    }

}
