package dao;

import model.Category;
import model.Child;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.DBUtil;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChildDBTest {

    private static ChildDB db;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        db = new ChildDB(DBUtil.getConnection());
        new DBUtil().executeFile("init.sql");
    }

    @Test
    void returnGeneratedIdWhenAddChild() throws SQLException {
        int oldCount = DBUtil.totalCount("categories");
        var newChild = new Child("First Name", "LastName", LocalDate.now().minusYears(3));
        newChild = db.add(newChild);
        assertNotNull(newChild.id(), "after add Child should contains id");
    }

    @Test
    void returnGeneratedIdWhenAddChildWithNullBirthDate() throws SQLException {
        int oldCount = DBUtil.totalCount("categories");
        var newChild = new Child("First Name", "LastName", null);
        newChild = db.add(newChild);
        assertNotNull(newChild.id(), "after add Child should contains id");
    }

    @Test
    void returnOneWhenUpdateExistingUser() throws SQLException {
        var firstChild = new Child(null, "some", "some", null);
        boolean actual = db.update(firstChild);
        assertTrue(actual, "Should returns true if it was updated just one record");
    }

    @Test
    void returnTrueWhenDeleteExistingUser() throws SQLException {
        boolean actual = db.delete(4L);
        assertTrue(actual, "Should returns true if it was delete just one record");
    }

    @Test
    public void returnNotEmptyListWhenAtLeastAgeTwenty() throws SQLException {
        Child expected = new Child(2L, "First name 2 child", "Last Name 2 child", LocalDate.parse("2000-01-01"));

        List<Child> actual = db.allAtLeastAge(20);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual).size().isEqualTo(1);
        softly.assertThat(actual)
                .filteredOn(child ->
                        child.id().equals(expected.id())
                                && child.firstName().equals(expected.firstName())
                                && child.lastName().equals(expected.lastName())
                ).hasSize(1);
        softly.assertAll();
    }

    @Test
    public void returnNotEmptyListWhenWithoutBirthDate() throws SQLException {
        Child expected = new Child(1L, "First name child", "Last name child", null);

        List<Child> actual = db.allWithoutBirthDate();
        assertThat(actual)
                .isNotEmpty();
    }

    @Test
    void shouldNotGetNonExistingChild() throws SQLException {
        var child = db.get(1000L);

        assertNull(child, "Should return null if the child does not exist");
    }

}