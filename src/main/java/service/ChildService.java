package service;

import dao.ChildDB;
import exception.ValidationException;
import model.Category;
import model.Child;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

public class ChildService {
    protected Logger log = LoggerFactory.getLogger(ChildService.class);
    private ChildDB db;

    public ChildService(ChildDB db) {
        this.db = db;
    }

    public Child add(Child child) {
        if (isNull(child.birthDate()) || child.birthDate().isAfter(LocalDate.now())){
            throw new ValidationException(String.format("Birthdate %s can't be in future", child.birthDate().toString()));
        }
        try {
            if (isNull(child.id())) {
                var result = db.add(child);
                log.info("Child {} was created", child);
                return result;
            }
            boolean isUpdated = db.update(child);
            if (isUpdated) {
                log.info("Category {} was updated", child);
            } else {
                log.warn("Doesn't update category {} to database", child);
            }
            return child;
        } catch (SQLException e) {
            log.error("Doesn't update category {} to database", child);
            throw new RuntimeException(e);
        }
    }

    public Child delete(long id) {
        try {
            var child = db.get(id);
            if (db.delete(id)) {
                log.info("Child with id {} was deleted", id);
            } else log.error("Doesn't delete child with id {} ", id);
            return child;
        } catch (SQLException e) {
            log.error("Doesn't delete child with id {} ", id);
            throw new RuntimeException(e);
        }
    }

    public List<Child> getAll() {
        try {
            var children = db.getAll();
            log.info("Get all children ");
            return children;
        } catch (SQLException e) {
            log.error("Something wrong with get all children method");
            throw new RuntimeException(e);
        }
    }

    public List<Child> byName(String text) {
        try {
            var children = db.getByName(text);
            log.info("Get children where first name or last name contains {}", text);
            return children;
        } catch (SQLException e) {
            log.error("Something wrong with get children with {} in first name or in last name", text);
            throw new RuntimeException(e);
        }
    }
}
