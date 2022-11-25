package com.qtivate.server.model;
import org.bson.BsonTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Document("tokens")
public class Token {
    @Id
    private String id;
    private Date created_at;
    private String key;

    public Token(String key, Date created_at) {
        this.key = key;
        this.created_at = created_at;
    }

    public boolean isValid(Date date) {
        long diff = date.getTime() - this.created_at.getTime();
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) < 10;
    }

    public boolean isValid() {
        long diff = Calendar.getInstance().getTime().getTime() - this.created_at.getTime();
        // TODO: Mudar 800000 para 8000
        return diff < 800000;
    }
}
