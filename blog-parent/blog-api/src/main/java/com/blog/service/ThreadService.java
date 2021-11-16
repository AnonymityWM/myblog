package com.blog.service;

import com.blog.dao.mapper.ArticleMapper;
import com.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

        //期望此操作在线程池 执行 不会影响原有的主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        int viewCounts = article.getViewCounts();
        Long id = article.getId();
        articleMapper.updateViewCounts(id,viewCounts+1);

//        try {
//            Thread.sleep(5000);
//            System.out.println("更新完成了....");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
