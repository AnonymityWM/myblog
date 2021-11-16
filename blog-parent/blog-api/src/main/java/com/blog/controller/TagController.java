package com.blog.controller;

import com.blog.service.TagService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// json数据进行交互
@RestController
@RequestMapping("tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 最热标签，返回前6个最热标签
     * @return
     */
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }
}
