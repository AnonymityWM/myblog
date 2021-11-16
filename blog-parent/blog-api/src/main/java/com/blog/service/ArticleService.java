package com.blog.service;

import com.blog.dao.pojo.Article;
import com.blog.vo.ArticleVo;
import com.blog.vo.Result;
import com.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {

    /**
     * 首页 展示文章列表
     * @param pageParams
     * @return
     */
    public Result listArticlesPage(PageParams pageParams);

    /**
     * 首页 最热文章
     * @param limit
     * @return
     */
    public Result hotArticle(int limit);

    /**
     * 首页 最新文章
     * @param limit
     * @return
     */
    public Result newArticles(int limit);

    /**
     * 归档文章
     * @return
     */
    public Result listArchives();

    /**
     * 根据id查看文章详情
     * @param articleId
     * @return
     */
    public Result findArticleById(Long articleId);
}
