package service;

import dao.CategoryDB;
import model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.isNull;

public class CategoryService {
    protected Logger log = LoggerFactory.getLogger(CategoryService.class);
    private CategoryDB db;

    public CategoryService(CategoryDB db) {
        this.db = db;
    }

    public Category add(Category category) {
        try {
            if (isNull(category.id())) {
                var result = db.add(category);
                log.info("Category {} was created", category);
                return result;
            }
            boolean isUpdated = db.update(category);
            if (isUpdated) {
                log.info("Category {} was updated", category);
            } else {
                log.warn("Doesn't update category {} to database", category);
            }
            return category;
        } catch (SQLException e) {
            log.error("Doesn't update category {} to database", category);
            throw new RuntimeException(e);
        }
    }

    public Category delete(long id) {
        try {
            var category = db.getById(id);
            if (db.delete(id)) {
                log.info("Category with id {} was deleted", id);
            } else log.error("Doesn't delete category with id {} ", id);
            return category;
        } catch (SQLException e) {
            log.error("Doesn't delete category with id {} ", id);
            throw new RuntimeException(e);
        }
    }

    public List<Category> byTitle(String text) {
        try {
            var categories = db.titlePart(text);
            log.info("Search categories with text {}  ", text);
            return categories;
        } catch (SQLException e) {
            log.error("Something wrong with search categories with text {}", text);
            throw new RuntimeException(e);
        }
    }
}
