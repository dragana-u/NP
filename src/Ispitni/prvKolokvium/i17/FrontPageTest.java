package Ispitni.prvKolokvium.i17;


import java.util.*;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde

abstract class NewsItem{
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }
    abstract String getTeaser();
    public int when(){
        Date now = new Date();
        long ms = now.getTime() - date.getTime();
        return (int)(ms/1000)/60;
    }
}

class TextNewsItem extends NewsItem{
    String newsText;

    public TextNewsItem(String title, Date date, Category category, String newsText) {
        super(title, date, category);
        this.newsText = newsText;
    }

    @Override
    String getTeaser() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title)
                .append("\n")
                .append(when())
                .append("\n");
        String teaser = newsText;
        if(newsText.length()>80){
            teaser = teaser.substring(0,80);
        }
        stringBuilder.append(teaser)
                .append("\n");
        return stringBuilder.toString();
    }
}

class MediaNewsItem extends NewsItem{
    String url;
    int brojPregledi;

    public MediaNewsItem(String title, Date date, Category category, String url,int brojPregledi) {
        super(title, date, category);
        this.url = url;
        this.brojPregledi = brojPregledi;
    }

    @Override
    String getTeaser() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title)
                .append("\n")
                .append(when())
                .append("\n")
                .append(url)
                .append("\n")
                .append(brojPregledi)
                .append("\n");
        return stringBuilder.toString();
    }
}

class FrontPage{
    List<NewsItem> vesti;
    Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        vesti = new ArrayList<>();
    }
    void addNewsItem(NewsItem newsItem){
        vesti.add(newsItem);
    }

    ArrayList<NewsItem> listByCategory(Category category){
        ArrayList<NewsItem> byCategory = new ArrayList<>();
        for (NewsItem newsItem : vesti) {
            if(newsItem.category.equals(category)){
                byCategory.add(newsItem);
            }
        }
        return byCategory;
    }
    ArrayList<NewsItem> listByCategoryName(String name) throws CategoryNotFoundException {
        int indx = -1;
        for(int i=0;i<categories.length;i++){
            if(categories[i].getName().equals(name)){
                indx = i;
                break;
            }
        }
        if(indx==-1){
            throw new CategoryNotFoundException(String.format("Category %s was not found",name));
        }
        return listByCategory(categories[indx]);
    }

    @Override
    public String toString() {
        StringBuilder tx = new StringBuilder();
        for (NewsItem newsItem : vesti) {
            tx.append(newsItem.getTeaser());
        }
        return tx.toString();
    }
}

class Category implements Comparable<Category>{
    String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }

    public String getName() {
        return name;
    }
}

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(message);
    }
}

