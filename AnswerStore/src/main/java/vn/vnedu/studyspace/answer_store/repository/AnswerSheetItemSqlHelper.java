package vn.vnedu.studyspace.answer_store.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AnswerSheetItemSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("question_id", table, columnPrefix + "_question_id"));
        columns.add(Column.aliased("answer_id", table, columnPrefix + "_answer_id"));

        columns.add(Column.aliased("answer_sheet_id", table, columnPrefix + "_answer_sheet_id"));
        return columns;
    }
}
