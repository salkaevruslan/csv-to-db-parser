
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;

import org.hsqldb.Server;

public class Main {
    private static final String DB_NAME = "testdb";

    public static void main(String[] args) {
        String fileName = args[0];
        parseCSVtoDB(fileName);
    }

    private static void parseCSVtoDB(String fileName) {
        List<List<String>> values = Parser.parseValues(fileName); //or Parser.parseValuesWithOpenCSV(fileName);
        int columnsNum = 0;
        for (List<String> line : values) {
            columnsNum = Math.max(columnsNum, line.size());
        }
        if (values.size() == 0) return;
        Server server = null;
        try {
            server = setUpServer();
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:hsqldb:hsql://localhost/" + DB_NAME, "sa", "")) {
                DatabaseMetaData dbm = connection.getMetaData();
                ResultSet tables = dbm.getTables(null, null, TableStatement.TABLE_NAME, null);
                if (tables.next()) {
                    connection.prepareStatement(TableStatement.generateDropTableStatement()).execute();
                }
                String createStatement = TableStatement.generateCreateTableStatement(values.get(0), columnsNum);
                connection.prepareStatement(createStatement).execute();
                for (int i = 1; i < values.size(); i++) {
                    if (isEmptyLine(values.get(i))) continue;
                    String insertStatement = TableStatement.generateInsertStatement(values.get(i), columnsNum);
                    connection.prepareStatement(insertStatement).execute();
                }
                printDbContent(connection, columnsNum);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } finally {
            if (server != null) {
                server.stop();
            }
        }
    }

    @NotNull
    private static Server setUpServer() {
        Server server = new Server();
        server.setSilent(true);
        server.setLogWriter(null);
        server.setDatabaseName(0, DB_NAME);
        server.setDatabasePath(0, "file:" + DB_NAME);
        server.start();
        return server;
    }

    private static void printDbContent(@NotNull Connection connection, int columnsNum) throws SQLException {
        ResultSet rs = connection.prepareStatement(TableStatement.generateSelectAllStatement()).executeQuery();
        while (rs.next()) {
            for (int i = 1; i <= columnsNum; i++) {
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println();
        }
    }


    private static boolean isEmptyLine(@NotNull List<String> values) {
        for (String value : values) {
            if (value != null && !value.equals("")) return false;
        }
        return true;
    }
}
