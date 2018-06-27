package util;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Maciej Jankowicz on 27.06.18, 11:34
 * Contact: mj6367@gmail.com
 */
public class Message implements Serializable{
    private static final long serialVersionUID = 1L;

    private String content;
    private String author;
    private LocalDateTime createdTime;

    public Message(String content, String name){
        this.content = content;
        this.author = name;
        this.createdTime = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return author + " > " + content;
    }
}
