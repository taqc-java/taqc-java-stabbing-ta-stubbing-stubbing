package service;

import dao.CategoryDB;
import model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    CategoryService service;

    @Mock
    CategoryDB dao;

    @BeforeEach
    public void before() {
        service = new CategoryService(dao);
    }

    @Test
    void addNewCategory() throws SQLException {
        Category category = new Category("str1", "str2");
        Category newCategory = new Category(1L, "str1", "str2");
        when(dao.add(category)).thenReturn(newCategory);

        var actual = service.add(category);

        verify(dao, times(1)).add(category);
        assertThat(actual).usingRecursiveComparison().isEqualTo(newCategory);
    }

    @Test
    void updateCategory() throws SQLException {
        Category category = new Category(1L, "str1", "str2");

        var actual = service.add(category);

        verify(dao, times(1)).update(category);
        assertThat(actual).usingRecursiveComparison().isEqualTo(category);
    }

    @Test
    void negativeAddCategory() throws SQLException {
        Category category = new Category("str1", "str2");
        String errorMessage = "Some problem with database or sql query";
        when(dao.add(category)).thenThrow(new SQLException(errorMessage));

        Throwable exception = assertThrows(RuntimeException.class, () -> service.add(category));

        assertThat(exception.getMessage()).contains(errorMessage);
    }

    @Test
    void negativeUpdateCategory() throws SQLException {
        Category category = new Category(1L, "str1", "str2");
        String errorMessage = "Some problem with database or sql query";
        when(dao.update(any(Category.class))).thenThrow(new SQLException(errorMessage));

        Throwable exception = assertThrows(RuntimeException.class, () -> service.add(category));

        assertThat(exception.getMessage()).contains(errorMessage);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void deleteCategory(boolean deletedResult) throws SQLException {
        Category category = new Category(1L, "str1", "str2");
        when(dao.delete(1L)).thenReturn(deletedResult);
        when(dao.getById(1L)).thenReturn(category);

        var actual = service.delete(1L);

        verify(dao, times(1)).delete(1L);
        assertThat(actual).usingRecursiveComparison().isEqualTo(category);
    }

    @Test
    void getByITitle() throws SQLException {
        Category category1 = new Category(1L, "str1", "str2");
        Category category2 = new Category(2L, "str12", "str22");
        when(dao.titlePart("str2")).thenReturn(asList(category2, category1));

        var actual = service.byTitle("str2");

        verify(dao, times(1)).titlePart("str2");
        assertThat(actual).hasSameElementsAs(asList(category1, category2));
    }

    @Test
    void getByTitleEmptyResult() throws SQLException {
        when(dao.titlePart(anyString())).thenReturn(Collections.emptyList());

        var actual = service.byTitle("_______________");

        verify(dao, times(1)).titlePart("_______________");
        assertThat(actual).hasSize(0);
    }

    @Test
    void negativeDeleteCategory() throws SQLException {
        String errorMessage = "Some problem with database or sql query";
        when(dao.delete(1L)).thenThrow(new SQLException(errorMessage));

        Throwable exception = assertThrows(RuntimeException.class, () -> service.delete(1L));

        assertThat(exception.getMessage()).contains(errorMessage);
    }
    @Test
    void negativeSearchCategory() throws SQLException {
        String errorMessage = "Some problem with database or sql query";
        when(dao.titlePart("some")).thenThrow(new SQLException(errorMessage));

        Throwable exception = assertThrows(RuntimeException.class, () -> service.byTitle("some"));

        assertThat(exception.getMessage()).contains(errorMessage);
    }
}