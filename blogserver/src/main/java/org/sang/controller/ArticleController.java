package org.sang.controller;

import org.apache.commons.io.IOUtils;
import org.sang.bean.*;
import org.sang.service.AnswerService;
import org.sang.service.ArticleService;
import org.sang.service.UserService;
import org.sang.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sang on 2017/12/20.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    ArticleService articleService;

    @Autowired
    AnswerService answerService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespBean addNewArticle(Article article) {
        int result = articleService.addNewArticle(article);
        if (result == 1) {
            return new RespBean("success", article.getId() + "");
        } else {
            return new RespBean("error", "提问失败");
        }
    }

    /**
     * 上传图片
     *
     * @return 返回值为图片的地址
     */
    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
    public RespBean uploadImg(HttpServletRequest req, MultipartFile image) {
        StringBuffer url = new StringBuffer();
        String filePath = "/blogimg/" + sdf.format(new Date());
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        url.append(req.getScheme())
                .append("://")
                .append(req.getServerName())
                .append(":")
                .append(req.getServerPort())
                .append(req.getContextPath())
                .append(filePath);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            url.append("/").append(imgName);
            return new RespBean("success", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespBean("error", "上传失败!");
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByState(@RequestParam(value = "state", defaultValue = "-1") Integer state,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "count", defaultValue = "6") Integer count, String keywords) {
        int totalCount = articleService.getArticleCountByState(state, Util.getCurrentUser().getId(), keywords);
        List<Article> articles = articleService.getArticleByState(state, page, count, keywords);
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("articles", articles);
        return map;
    }

    /**
     * 根据 cid 分页查询问题列表数据
     *
     * @param page
     * @param count
     * @param cid
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public Map<String, Object> getArticleByCid(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "count", defaultValue = "6") Integer count, String cid) {
        int totalCount = articleService.getArticleCountByState(-1, Util.getCurrentUser().getId(), cid);
        List<Article> articles = articleService.getArticleByCid(page, count, cid);
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("articles", articles);
        return map;
    }

    /**
     * 「问题详情」页面。获取问题信息，回答数据
     *
     * @param aid
     * @return
     */
    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    public Map<String, Object> getArticleById(@PathVariable Long aid) {
        Article articles = articleService.getArticleById(aid);
        List<Answer> answerList = answerService.getAnswerByAid(aid);

        List<User> userList = new ArrayList<>();
        for (Answer answer : answerList) {
            Long id = answer.getUid();
            User user = userService.getUserById(id);
            userList.add(user);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("articles", articles);
        map.put("answerList", answerList);
        map.put("userList", userList);
        return map;
    }

    /**
     * 删除问题，仅管理员有权，非管理员也不会在前端出现「删除」按钮
     *
     * @param aid
     * @return
     */
    @RequestMapping(value = "/remove/{aid}", method = RequestMethod.PUT)
    public RespBean remove(@PathVariable Long aid) {
        if (articleService.remove(aid)) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    @RequestMapping(value = "/dustbin", method = RequestMethod.PUT)
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    @RequestMapping("/dataStatistics")
    public Map<String, Object> dataStatistics() {
        Map<String, Object> map = new HashMap<>();
        List<String> categories = articleService.getCategories();
        List<Integer> dataStatistics = articleService.getDataStatistics();
        map.put("categories", categories);
        map.put("ds", dataStatistics);
        return map;
    }

    /**
     * 根据提问问题ids，关注问题 ids，查询对应的问题数据
     *
     * @param questionIds   提问问题的ids
     * @param attentionQids 关注问题的ids
     */
    @RequestMapping(value = "/getArticlesByIds", method = RequestMethod.GET)
    public Map<String, Object> getArticlesByIds(@RequestParam String questionIds, @RequestParam String attentionQids) {
        Map<String, Object> map = new HashMap<>();
        String[] questionIdsArray = questionIds.split(",");
        String[] attentionQidsArray = attentionQids.split(",");
        List<Article> questionArticles = articleService.getArticleByIds(questionIdsArray);
        List<Article> attentionArticles = articleService.getArticleByIds(attentionQidsArray);
        map.put("questionArticles", questionArticles);
        map.put("attentionArticles", attentionArticles);
        return map;
    }


}
