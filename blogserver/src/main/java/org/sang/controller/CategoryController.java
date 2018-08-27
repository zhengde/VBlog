package org.sang.controller;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Category;
import org.sang.bean.RespBean;
import org.sang.service.CategoryService;
import org.sang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteById(@PathVariable String ids) {
        boolean result = categoryService.deleteCategoryByIds(ids);
        if (result) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    public RespBean addNewCate(Category category) {
//        int result = categoryService.addCategory(category);
//        if (result == 1) {
//            return new RespBean("success", "添加成功!");
//        }
//        return new RespBean("error", "添加失败!");
//    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public RespBean updateCate(Category category) {
        int i = categoryService.updateCategoryById(category);
        if (i == 1) {
            return new RespBean("success", "修改成功!");
        }
        return new RespBean("error", "修改失败!");
    }

    /**
     * 根据话题 ids 查询话题数据
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getCategoriesByIds", method = RequestMethod.GET)
    public List<Category> getCategoriesByIds(@RequestParam String ids) {
        String[] idArray = ids.split(",");
        return categoryService.getCategoriesByIds(idArray);
    }

    /**
     * 删除话题，仅管理员有权，非管理员也不会在前端出现「删除」按钮
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.PUT)
    public RespBean remove(@PathVariable Long id) {
        if (categoryService.remove(id)) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateCategory(String lastName,String curName) {
        categoryService.updateCategory(lastName,curName);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(String name) {
        categoryService.add(name);
    }


}
