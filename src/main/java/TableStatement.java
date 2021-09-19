import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TableStatement {
    public static final String TABLE_NAME = "TEST_TABLE";

    @NotNull
    public static String generateDropTableStatement() {
        return "drop table " + TableStatement.TABLE_NAME + ";";
    }

    @NotNull
    public static String generateSelectAllStatement() {
        return "select * from " + TableStatement.TABLE_NAME + ";";
    }

    @NotNull
    public static String generateCreateTableStatement(@NotNull List<String> values, int columnsNum) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(TABLE_NAME).append(" (");
        boolean isFirst = true;
        for (String value : values) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append(value).append(" VARCHAR(100)");
        }
        for (int i = 0; i < columnsNum - values.size(); i++) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append("C").append(i).append(" VARCHAR(100)");
        }
        sb.append(");");
        return sb.toString();
    }

    @NotNull
    public static String generateInsertStatement(@NotNull List<String> values, int columnsNum) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(TABLE_NAME).append(" values (");
        boolean isFirst = true;
        for (String value : values) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append("'").append(value).append("'");
        }
        for (int i = 0; i < columnsNum - values.size(); i++) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append("NULL");
        }
        sb.append(");");
        return sb.toString();
    }
}
