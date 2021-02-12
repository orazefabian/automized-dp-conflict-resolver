package dp.api.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class QueryGeneratorImpl implements QueryGenerator {

    private final String QUERY_JAR = "";
    private final String QUERY_CLASSES_JAR = "";
    private final String QUERY_METHODS_FROM_CLASS = "";
    private final Connection connection;

    public QueryGeneratorImpl(Connection connect) {
        this.connection = connect;
    }

    //TODO: adapt queries to db

    @Override
    public String getJar(String jar) {
        StringBuilder builder = new StringBuilder();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_JAR);
            preparedStatement.setString(1, jar);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                builder.append(resultSet.getString(""));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    public List<String> getClassesFromJar(String jar) {
        return null;
    }

    @Override
    public List<String> getMethodsFromClass(String jar, String nameClass) {
        return null;
    }
}
