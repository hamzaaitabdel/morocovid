package ml.bsilent.motira;

import java.nio.LongBuffer;

public class Info {
    Long  confirmed,Excluded,Recoverers,death,news;

    public Info() {
    }

    public Info(long confirmed, long excluded, long recoverers, long death, long news) {
        this.confirmed = confirmed;
        Excluded = excluded;
        Recoverers = recoverers;
        this.death = death;
        this.news = news;
    }

    @Override
    public String toString() {
        return "Info{" +
                "confirmed='" + confirmed + '\'' +
                ", Excluded='" + Excluded + '\'' +
                ", Recoverers='" + Recoverers + '\'' +
                ", death='" + death + '\'' +
                ", news='" + news + '\'' +
                '}';
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public void setExcluded(long excluded) {
        Excluded = excluded;
    }

    public void setRecoverers(long recoverers) {
        Recoverers = recoverers;
    }

    public void setDeath(long death) {
        this.death = death;
    }

    public void setNews(long news) {
        this.news = news;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public long getExcluded() {
        return Excluded;
    }

    public long getRecoverers() {
        return Recoverers;
    }

    public long getDeath() {
        return death;
    }

    public long getNews() {
        return news;
    }
}
