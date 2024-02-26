package com.chen.behindimagesmanage.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.StringTypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 15031
 */
public class StringToListTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setString(i, String.join(",", parameter));
        } else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return convertStringToList(columnValue);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return convertStringToList(columnValue);
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return convertStringToList(columnValue);
    }

    private List<String> convertStringToList(String columnValue) {
        if (columnValue != null) {
            String[]columnPart = columnValue.split(",");
            return new ArrayList<>(Arrays.asList(columnPart));

        } else {
            return null;
        }
    }
}
