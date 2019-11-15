package com.james.springbootdataes;

import com.james.springbootdataes.elasticsearch.dao.EsRepository;
import com.james.springbootdataes.elasticsearch.pojo.Article;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootDataEsApplication.class)
public class EsTest {

    @Autowired
    private EsRepository esRepository;

    @Test
    public void testAddIndexAndDocument(){
        Article article = new Article();
        article.setId(1l);
        article.setTitle("下载金山词霸APP");
        article.setContent("下载金山词霸APP,体验更多的精彩 立即下载 词典 悦读 听力 每日一句 课程 是否要跟读这个单词 ? 通过发音加深印象,更快记住单词 跟读单词 ...");
        esRepository.index(article);
    }

    @Test
    public void testAddDocument(){
        for (long i = 14;i <= 35;i++){
            Article article = new Article();
            article.setId(i);
            article.setTitle("周星驰" + i);
            article.setContent(i + "2002年凭借喜剧片《少林足球》获得第21届香港电影金像奖" +
                    "最佳男主角奖以及最佳导演奖 [5]  。2003年成为美国《时代周刊》封面人物 [6]" +
                    "  。2005年凭借喜剧动作片《功夫》获得第42届台湾电影金马奖最佳导演奖 [7] " +
                    " 。2008年自导自演的科幻喜剧片《长江7号》获得香港电影年度票房冠军 [8]  " +
                    "。2013年执导古装喜剧片《西游·降魔篇》，该片以2.18亿美元的票房成绩打破华" +
                    "语电影在全球的票房纪录 [9-10]  。2016年担任科幻喜剧片《美人鱼》的导演、" +
                    "编剧、制作人，该片以超过33亿元的票房创下中国内地电影票房纪录 [11-14]  。");
            esRepository.index(article);
        }

    }

    //做修改
    //说明：Lucene内部的修改其实就是先删除再添加，ES底层使用的是Lucene，那么原理相同，那么要做修改其实只要在添加的方法里面做就行了
    @Test
    public void testUpdateDocument(){
        Article article = new Article();
        article.setId(2l);//修改这条，id为2的数据
        article.setTitle("图片及作品列表");//title修改掉
        //content也修改掉
        article.setContent("1992年因香港当年度十五大卖座影片中周星驰占了七个,并且前五名被周星驰包揽,周星驰也连续第三年彻底占领了整个香港市场,更凭又一次打破票房记录的《审死官》获得亚太.");
        esRepository.index(article);
    }

    @Test
    public void testDelete(){
        Optional<Article> optional = esRepository.findById(35l);
        esRepository.delete(optional.get());
        //还有deleteAll方法

    }

    @Test
    public void testFindAll(){
        //查全部，真的给全部
        Iterable<Article> iterable = esRepository.findAll();
        iterable.forEach(article -> System.out.println(article));
    }

    @Test
    public void testFindAllByPageable(){
        //分页查询
        //狗日的，第一个参数是页码，0表示第一页，第二个参数是每页大小
        Pageable pageable = PageRequest.of(2,3);
        Page<Article> page = esRepository.findAll(pageable);
        System.out.println(page.getTotalElements());//总条数
        System.out.println(page.getTotalPages());//总页数
        page.forEach(article -> System.out.println(article));//打印数据
    }

    //自定义方法，规则JPA
    @Test
    public void testFindByTitle(){
        System.out.println("----------------------------------------------");
        //搜索：香港周星驰电影，条数：0，说明：字符串会先被分词再搜索，但是里面的关系是AND
        //搜索：百度周星驰，11条
        List<Article> articles = esRepository.findByTitle("百度周星驰");

        for (Article article : articles) {
            System.out.println(article);
        }
        System.out.println(articles.size());
        System.out.println("----------------------------------------------");
    }


    @Test
    public void testFindByTitleOrContent(){
        List<Article> articleList = esRepository.findByTitleOrContent("周星驰", "时代周刊");
        articleList.forEach(article -> System.out.println(article));
        System.out.println(articleList.size());
    }

    @Test
    public void testFindByTitleOrContent2(){
        Pageable pageable = PageRequest.of(2,3);
        List<Article> articleList = esRepository.findByTitleOrContent("周星驰", "时代周刊",pageable);
        articleList.forEach(article -> System.out.println(article));
        System.out.println(articleList.size());
    }

    //NativeQuerySearch
    @Test
    public void testNativeQuerySearch(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("香港周星驰电影").defaultField("title"))
                .withPageable(PageRequest.of(2, 3))
                .build();
        Page<Article> articles = esRepository.search(query);
        articles.forEach(article -> System.out.println(article));
        System.out.println(articles.getTotalPages());
        System.out.println(articles.getTotalElements());
    }

}
