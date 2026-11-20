package br.com.majora.assistent.service.db;

import br.com.majora.assistent.model.request.commands.CommandResult;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseExecuteQuery {

    private final ConnectionFactory conn;

    public DatabaseExecuteQuery(ConnectionFactory conn) {
        this.conn = conn;
    }

    public CommandResult getEventAndInteraction(List<String> listQuery) {
        try {
            String query = String.join(" ", listQuery);

            Map<String, Object> register = new HashMap<>();

            Connection connection = conn.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                for(int i = 1; i <= metaData.getColumnCount(); i++) {
                    String coluna = metaData.getColumnLabel(i);

                    register.put(coluna, rs.getObject(i));
                }
            }

            CommandResult result = new CommandResult();
            result.setSuccess(true);
            result.setData(List.of(register));

            return result;

        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();

            error.put("type", e.getClass().getSimpleName());
            error.put("message", e.getMessage());
            error.put("code", e.getErrorCode());
            error.put("sqlState", e.getSQLState());

            CommandResult result = new CommandResult();
            result.setSuccess(false);
            result.setData(List.of(error));

            return result;

        }
    }
}
