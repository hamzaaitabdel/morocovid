package ml.bsilent.motira.Models;

public class Documents {
    private int id;
    private String link;

    public Documents() {
    }

    public Documents(int id, String link) {
        this.id = id;
        this.link = link;
    }

    public int getId() {
        return id;
    }


    public String getLink() {
        return link;
    }

}
