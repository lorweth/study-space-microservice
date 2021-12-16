package vn.vnedu.studyspace.answer_store.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AnswerSheetSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("time", table, columnPrefix + "_time"));
        columns.add(Column.aliased("user_login", table, columnPrefix + "_user_login"));

        columns.add(Column.aliased("group_time_table_id", table, columnPrefix + "_group_time_table_id"));
        return columns;
    }
}
