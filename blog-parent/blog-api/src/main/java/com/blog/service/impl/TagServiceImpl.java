package com.blog.service.impl;

import com.blog.dao.mapper.TagMapper;
import com.blog.dao.pojo.Tag;
import com.blog.service.TagService;
import com.blog.vo.Result;
import com.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

    /**
     * 查询文章articleId的所有Tag
     * @param articleId
     * @return
     */
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //mybatisplus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    /**
     * 最热标签
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
        // 1. 标签所拥有的文章数量最多 最热标签
        // 2. 查询 根据tag_id 分组 计数，从大到小 排列 取前 limit个
        List<Tag> hotsTags = tagMapper.findHotsTags(limit);
        if (CollectionUtils.isEmpty(hotsTags)){
            return Result.success(Collections.emptyList());
        }
        return Result.success(hotsTags);
    }
}
