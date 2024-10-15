package dao;

import exception.EntityNotFoundException;
import model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDB {
    private Connection conn;

    public CategoryDB(Connection conn) {
        this.conn = conn;
    }

    public CategoryDB() {
        try {
            conn = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category add(Category category) throws SQLException {
        String query = "insert into categories (avatar, title) values (?,?);";
        try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            pst.setString(1, category.avatar());
            pst.setString(2, category.title());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            Long id = rs.getLong(1);
            return new Category(id, category.avatar(), category.title());
        }
    }

    public boolean update(Category category) throws SQLException {
        String query = "update categories set avatar = ?, title = ? where id = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query);) {
            pst.setString(1, category.avatar());
            pst.setString(2, category.title());
            pst.setLong(3, category.id());
            int affectedRows = pst.executeUpdate();
            return affectedRows == 1;
        }
    }

    public boolean delete(Long id) throws SQLException {
        String query = "delete from categories where id = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setLong(1, id);
            int affectedRows = pst.executeUpdate();
            return affectedRows == 1;
        }
    }

    public List<Category> titlePart(String titlePart) throws SQLException {
        String query = "SELECT * FROM categories where lower(title) like ?";
        try (PreparedStatement statement = conn.prepareStatement(query);) {
            statement.setString(1, "%" + titlePart.toLowerCase() + "%");
            ResultSet rs = statement.executeQuery();
            return fromResultSet(rs);
        }
    }

    private static List<Category> fromResultSet(ResultSet rs) throws SQLException {
        List<Category> categories = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String avatar = rs.getString("avatar");
            String title = rs.getString("title");
            Category category = new Category(id, avatar, title);

            categories.add(category);
        }
        return categories;
    }

    public Category getById(Long id) throws SQLException {
        String query = "SELECT * FROM categories where id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String avatar = rs.getString("avatar");
                String title = rs.getString("title");
                return new Category(id, avatar, title);
            }
        }
        throw new EntityNotFoundException(Category.class, id);
    }

}
