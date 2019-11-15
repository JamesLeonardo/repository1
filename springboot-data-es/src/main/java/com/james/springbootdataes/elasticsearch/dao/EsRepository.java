package com.james.springbootdataes.elasticsearch.dao;

import com.james.springbootdataes.elasticsearch.pojo.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsRepository extends ElasticsearchRepository<Article,Long> {
    public List<Article> findByTitle(String title);

    public List<Article> findByTitleOrContent(String title,String content);

    public List<Article> findByTitleOrContent(String title,String content,Pageable pageable);
}
