package com.james.springbootdataes.elasticsearch.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "data-blog",type = "_doc")
public class Article {
    @Id
    @Field(type = FieldType.Long,store = true)
    private Long id;
    @Field(type = FieldType.Text,store = true,analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Text,store = true,analyzer = "ik_max_word")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
