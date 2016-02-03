package ua.dn.arhathq.snowball;

/**
 * @author Alexander Kuleshov
 */
public class LaunchOptions {

    private String cron;
    private String startAt;

    public LaunchOptions withCron(String cron) {
        this.cron = cron;
        return this;
    }

    public String getCron() {
        return cron;
    }

    public String getStartAt() {
        return startAt;
    }
}
