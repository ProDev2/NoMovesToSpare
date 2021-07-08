package nmts.game.time;

public class StopWatch {
    private long start;
    private long now;

    private long delta;
    private long minutes;
    private long seconds;
    private long millis;

    public StopWatch() {
        reset();
    }

    public void reset() {
        start = System.currentTimeMillis();
        update();
    }

    public void update() {
        now = System.currentTimeMillis();
        long d = delta = now - start;

        millis = (d /= 100L) % 10L;
        seconds = (d /= 10L) % 60L;
        minutes = d / 60L;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", minutes, seconds, millis);
    }
}
