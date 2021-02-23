package dp.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class PostGreSqlAPI {


    private static String URL = EnvConfig.DB_URL;
    private static String USER = EnvConfig.USER;
    private static String PW = EnvConfig.PASSWORD;

    /**
     * creates the connection to the database and returns a QueryGenerator
     * @return {@link QueryGenerator}
     * @throws SQLException if connection to db fails
     */
    public static QueryGenerator getQueryGenerator() throws SQLException {
        return new QueryGeneratorImpl(connect());
    }

    /**
     * connects to the PostGreSql Database
     *
     * @return the {@link Connection} object
     * @throws SQLException when connecting fails
     */
    private static Connection connect() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(URL, USER, PW);
        if (conn == null) {
            System.err.println("Could not connect to DB");
        } else {
            System.out.println("Connected to DB...");
        }
        return conn;
    }


    public static void setPW(String PW) {
        PostGreSqlAPI.PW = PW;
    }

    public static void setURL(String URL) {
        PostGreSqlAPI.URL = URL;
    }

    public static void setUSER(String USER) {
        PostGreSqlAPI.USER = USER;
    }


}
