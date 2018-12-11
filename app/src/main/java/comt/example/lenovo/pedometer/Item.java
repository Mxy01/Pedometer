package comt.example.lenovo.pedometer;

public class Item {
    String title;
    String content;
    int medalImage;

    public Item(String title,String content,int medalImage)
    {
        this.medalImage=medalImage;
        this.title=title;
        this.content=content;
    }
    public String getTitle()
    {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getMedalImage()
    {
        return medalImage;
    }

}
