package sk.gryfonnlair.dissertation.dbmentor.bundle;

import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.*;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;
import sk.gryfonnlair.dissertation.dbmentor.bundle.rules.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 1/8/14
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultMysqlConnectionLair extends MentorConnectionLair {

    public DefaultMysqlConnectionLair(String db, String user, String pass, String url) {
        super(db, user, pass, url);
    }

    @Override
    public Connection createConnection(String... connectionProperties)
            throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        String driver = "com.mysql.jdbc.Driver";
        StringBuffer sb = new StringBuffer("jdbc:mysql://").append(getUrl()).append('/').append(getDb());
        if (connectionProperties != null && connectionProperties.length > 0) {
            sb.append('?');
            for (int i = 0; i < connectionProperties.length; i++) {
                if (i != 0) {
                    sb.append(';');
                }
                sb.append(connectionProperties[i]);
            }

        }
        Class.forName(driver).newInstance();
        System.out.println("DefaultMysqlConnectionLair.createConnection > MySQL Class.forName DONE !");
        return DriverManager.getConnection(sb.toString(), getUser(), getPass());
    }

    //#QUICKCODE
    @Override
    public QuickCodeCallResult executeQuickCode(String sqlCode) throws Exception {
        Connection conn = null;
        try {
            conn = createConnection("allowMultiQueries=true");
        } catch (Exception e) {
            System.err.println("DefaultMysqlConnectionLair.executeQuickCode > crete connection");
            throw new Exception("Could not create connection to database");
        }
        QuickCodeCallResult result = new QuickCodeCallResult();
        System.out.println("DefaultMysqlConnectionLair.executeQuickCode > ########EXECUTE ########################");

        List<String> sqls = parseQuickSqlCode(sqlCode);

        try {
            conn.setAutoCommit(false); //transaction block start

            for (String sqlStatment : sqls) {
                System.out.println("DefaultMysqlConnectionLair.executeQuickCode > Vykonavam : " + sqlStatment);
                Statement statement = conn.createStatement();
                statement.execute(sqlStatment);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet != null) {
                    result.getResultSetTableList().add(getMCLResultSetTable(resultSet));
                    statement.getMoreResults();
                    resultSet = statement.getResultSet();
                }
            }
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return result;
    }

    //#PROCEDURY
    @Override
    public String[] getAllProceduresNames() throws Exception {
        List<String> list = new ArrayList<String>(0);

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SHOW PROCEDURE STATUS WHERE db = \'").append(getDb()).append("\';").toString());

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                System.out.println("DefaultMysqlConnectionLair.getAllProceduresNames > name: " + name);
                list.add(name);
            }

            conn.close();
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public Map<String, String> getProcedureDetailInfo(String procedureName) throws Exception {
        Map<String, String> map = new HashMap<String, String>(0);

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SHOW PROCEDURE STATUS WHERE db = \'")
                            .append(getDb())
                            .append("\' AND Name = \'")
                            .append(procedureName).append("\';")
                            .toString());

            System.out.println("DefaultMysqlConnectionLair.getProcedureDetailInfo > citam info pre: " + procedureName);
            //mal by byt jeden riadok
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String definer = resultSet.getString("Definer");
                String modified = resultSet.getString("Modified");
                String created = resultSet.getString("Created");
                String securityType = resultSet.getString("Security_type");
                String comment = resultSet.getString("Comment");

                //trochu dummy ale tak
                map.put("Name", name);
                map.put("Definer", definer);
                map.put("Modified", modified);
                map.put("Created", created);
                map.put("Security-type", securityType);
                map.put("Comment", comment);
            }
            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return map;
    }

    @Override
    public String getProcedureCode(String procedureName) throws Exception {
        String code = "";

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SHOW CREATE PROCEDURE ").append(getDb()).append('.').append(procedureName).append(';').toString());

            //mal by byt jeden riadok
            if (resultSet.next()) {
                code = resultSet.getString("Create Procedure");
                System.out.println("DefaultMysqlConnectionLair.getProcedureCode > citam kod pre: " + procedureName);
            }

            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return code;
    }

    @Override
    public List<ProcedureArgInfo> getProcedureArgumentsInfo(String procedureName) throws Exception {
        List<ProcedureArgInfo> list = new ArrayList<ProcedureArgInfo>();

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SELECT *  FROM information_schema.parameters WHERE SPECIFIC_NAME = \'")
                            .append(procedureName)
                            .append("\' AND ROUTINE_TYPE='PROCEDURE';")
                            .toString());
            System.out.println("DefaultMysqlConnectionLair.getProcedureArgumentsInfo > citam kod pre: " + procedureName);

            while (resultSet.next()) {
                ProcedureArgInfo arg = new ProcedureArgInfo();
                //position
                int argPos = resultSet.getInt("ORDINAL_POSITION");
                arg.setPosition(argPos);
                //name
                String argName = resultSet.getString("PARAMETER_NAME");
                arg.setName(argName);
                //type IN, OUT, INOUT (NULL for RETURNS)
                String argType = resultSet.getString("PARAMETER_MODE");
                arg.setType("IN".equalsIgnoreCase(argType) ? 1 :
                        "INOUT".equalsIgnoreCase(argType) ? 2 :
                                "OUT".equalsIgnoreCase(argType) ? 3 :
                                        "NULL".equalsIgnoreCase(argType) ? 4 : 5);
                //dataTypeName VARCHAR ?
                String argDataTypeName = resultSet.getString("DATA_TYPE");
                arg.setDataTypeName(argDataTypeName);
                //doplnok
                int argDataType = getSqlType(argDataTypeName);
                arg.setDataTypeForSQL(argDataType);
                arg.setNullEnable(false);

                list.add(arg);
                System.out.println("DefaultMysqlConnectionLair.getProcedureArgumentsInfo > parameter: " + argName + ", typ: " + argType);
            }
            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return list;
    }

    @Override
    public ProcedureCallResult callProcedure(String procedureName, ProcedureArgInfo[] args) throws Exception {
        Connection conn = createConnection("noAccessToProcedureBodies=true");

        //vyskladanie callu
        StringBuilder callBuilder = new StringBuilder("{call ");
        callBuilder.append(procedureName).append('(');
        for (int i = 0; i < args.length; i++) {
            callBuilder.append(i == 0 ? "?" : ",?");
        }
        callBuilder.append(")}");
        System.out.println("DefaultMysqlConnectionLair.callProcedure > CALL: " + callBuilder.toString());

        ProcedureCallResult result = new ProcedureCallResult();
        try {
            //nabuchanie hodnot
            CallableStatement cs = conn.prepareCall(callBuilder.toString());
            for (ProcedureArgInfo i : args) {
                cs.setString(i.getPosition(), i.getValue());
                System.out.println("DefaultMysqlConnectionLair.callProcedure > CALL cs setString: " + i.getPosition() + "=" + i.getValue());
                if (2 <= i.getType() && i.getType() <= 4) {
                    cs.registerOutParameter(i.getPosition(), i.getDataTypeForSQL());
                    System.out.println("DefaultMysqlConnectionLair.callProcedure > CALL cs: *** dataType"
                            + i.getDataTypeForSQL() + " >" + i.getDataTypeName());
                }
            }

            System.out.println("DefaultMysqlConnectionLair.callProcedure > execute result: " + cs.execute());

            //check OUT a INOUT argumetov
            getReturnArgsFromCallableStatement(cs, args, result);
            //check ak je nejaky resultset
            ResultSet resultSet = cs.getResultSet();
            while (resultSet != null) {
                result.getResultSetTableList().add(getMCLResultSetTable(resultSet));
                cs.getMoreResults();
                resultSet = cs.getResultSet();
            }
            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return result;
    }

    //#FUNCKIE
    @Override
    public String[] getAllFunctionsNames() throws Exception {
        List<String> list = new ArrayList<String>(0);

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SHOW FUNCTION STATUS WHERE db = \'").append(getDb()).append("\';").toString());

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                System.out.println("DefaultMysqlConnectionLair.getAllFunctionsNames > name: " + name);
                list.add(name);
            }

            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public Map<String, String> getFunctionDetailInfo(String functionName) throws Exception {
        Map<String, String> map = new HashMap<String, String>(0);

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SHOW FUNCTION STATUS" +
                            " WHERE Db = \'").append(getDb()).append("\' AND Name = \'" + functionName + "\';")
                            .toString());
            //mal by byt jeden riadok
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String definer = resultSet.getString("Definer");
                String modified = resultSet.getString("Modified");
                String created = resultSet.getString("Created");
                String securityType = resultSet.getString("Security_type");
                String comment = resultSet.getString("Comment");

                //trochu dummy ale tak
                map.put("Name", name);
                map.put("Definer", definer);
                map.put("Modified", modified);
                map.put("Created", created);
                map.put("Security-type", securityType);
                map.put("Comment", comment);
            }
            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return map;
    }

    @Override
    public String getFunctionCode(String functionName) throws Exception {
        String code = "";

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SHOW CREATE FUNCTION ").append(getDb()).append('.').append(functionName).append(';').toString());

            //mal by byt jeden riadok
            if (resultSet.next()) {
                code = resultSet.getString(3);
                System.out.println("DefaultMysqlConnectionLair.getFunctionCode > citam kod pre: " + functionName);
            }

            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return code;
    }

    @Override
    public List<FunctionArgInfo> getFunctionArgumentsInfo(String functionName) throws Exception {
        List<FunctionArgInfo> list = new ArrayList<FunctionArgInfo>();

        Connection conn = createConnection();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new StringBuilder("SELECT *  FROM information_schema.parameters WHERE SPECIFIC_NAME = \'")
                            .append(functionName)
                            .append("\' AND ROUTINE_TYPE='FUNCTION';")
                            .toString());
            System.out.println("DefaultMysqlConnectionLair.getFunctionArgumentsInfo > citam kod pre: " + functionName);

            while (resultSet.next()) {
                FunctionArgInfo arg = new FunctionArgInfo();
                //position
                int argPos = resultSet.getInt("ORDINAL_POSITION");
                arg.setPosition(argPos);
                //name
                String argName = resultSet.getString("PARAMETER_NAME");
                arg.setName(argName);
                //type IN, OUT, INOUT (NULL for RETURNS)
                String argType = resultSet.getString("PARAMETER_MODE");
                arg.setType("IN".equalsIgnoreCase(argType) ? 1 :
                        "INOUT".equalsIgnoreCase(argType) ? 2 :
                                "OUT".equalsIgnoreCase(argType) ? 3 :
                                        argType == null ? 4 : 5);
                //dataTypeName VARCHAR ?
                String argDataTypeName = resultSet.getString("DATA_TYPE");
                arg.setDataTypeName(argDataTypeName);
                //doplnok
                int argDataType = getSqlType(argDataTypeName);
                arg.setDataTypeForSQL(argDataType);
                arg.setNullEnable(false);

                list.add(arg);
                System.out.println("DefaultMysqlConnectionLair.getFunctionArgumentsInfo > parameter: " + argName + ", typ: " + argType);
            }

            conn.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return list;
    }

    @Override
    public String callFunction(String functionName, FunctionArgInfo[] args) throws Exception {
        Connection conn = createConnection("noAccessToProcedureBodies=true");

        String result = "";
        try {
            //vyskladanie callu {? = call CREATE_A_PERSON (?)}
            StringBuilder callBuilder = new StringBuilder("select ");
            callBuilder.append(functionName).append('(');
            //preto -1 lebo return je tie FunctionArgInfo
            for (int i = 0; i < args.length - 1; i++) {
                callBuilder.append(i == 0 ? "?" : ",?");
            }
            callBuilder.append(")");
            System.out.println("DefaultMysqlConnectionLair.callFunction > CALL: " + callBuilder.toString());

            //nabuchanie hodnot
            CallableStatement cs = conn.prepareCall(callBuilder.toString());
            for (FunctionArgInfo i : args) {
                //funkcia ma vzdy IN parametre podla toho som napisal toto skladanie, 1 pozicia je return vzdy a ostatne
                //arg su posunute
                if (i.getType() == 1) {
                    cs.setString(i.getPosition(), i.getValue());
                    System.out.println("DefaultMysqlConnectionLair.callFunction > CALL cs setString: " + (i.getPosition()) + "=" + i.getValue());
                } else if (i.getType() == 4) {//RETURN
                    //robim cez select
//                cs.registerOutParameter(1, i.getDataTypeForSQL());
//                System.out.println("DefaultMysqlConnectionLair.callFunction > CALL cs: setRETURN" + i.getDataTypeForSQL() + " >" + i.getDataTypeName());
                } else {
                    System.err.println("DefaultMysqlConnectionLair.callFunction > CALL WTF ZA ARGUMENT ??");
                }
            }

            System.out.println("DefaultMysqlConnectionLair.callFunction > execute result: " + cs.execute());

            //check jednej RETURN hodnoty
            //check ajedneho resultu

            ResultSet resultSet = cs.getResultSet();
            if (resultSet != null) {
                result = getReturnValueForFunction(resultSet);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return result;
    }

    //#DEBUGGER

    /**
     * Podla implementacie ake mam pravidla v baliku
     *
     * @return pole pravidiel
     */
    @Override
    public DebuggerRuleInfo[] getDebuggerActiveRules() {
        return new DebuggerRuleInfo[]{
                new RuleGroupBy().getDebuggerRuleInfo(),
                new RuleHaving().getDebuggerRuleInfo(),
                new RuleInnerSelect().getDebuggerRuleInfo(),
                new RuleJoin().getDebuggerRuleInfo(),
                new RuleLimit().getDebuggerRuleInfo(),
                new RuleOrderBy().getDebuggerRuleInfo(),
                new RuleUnion().getDebuggerRuleInfo(),
                new RuleWhere().getDebuggerRuleInfo()
        };
    }

    /**
     * Parsuje string podla pravidiel a podla ich poradia v list 0,1...
     *
     * @param sqlStatement sql statement to parse
     * @param chosenRules  pole pravidiel v poradi ako m sa maju aplikovat
     * @return Map<String,List<String>> < K= RULE.name, V= List<String> sqls k nemu>
     */
    @Override
    public Map<String, List<String>> parseDebuggerSQLStatement(String sqlStatement, DebuggerRuleInfo[] chosenRules) throws Exception {
        Map<String, List<String>> hashMap = new HashMap<String, List<String>>();

        List<String> innerList = new ArrayList<String>();
        innerList.add(sqlStatement);

        for (int i = 0; i < chosenRules.length; i++) {
            List<String> tempList = new ArrayList<String>();
            DatabaseMentorRule rule = createRule(chosenRules[i].getName());
            for (String prvok : innerList) {
                List<String> megaTemp = rule.applyRule(prvok);
                if (!megaTemp.isEmpty()) {
                    for (String string : megaTemp) {
                        tempList.add(string);
                    }
                }
            }
            innerList = tempList;
            hashMap.put(rule.getDebuggerRuleInfo().getName(), tempList);
        }
        return hashMap;
    }

    /**
     * Volam ju po kliknuti na rozdeleny select, ci ze na cast a ma spustit jednoducho select na db
     *
     * @param sqlStatement sql stmt
     * @return zaobalena MCLResultSetTable + cas za kt sa vykonal prizkaz
     */
    @Override
    public DebuggerRunResult runSimpleStatement(String sqlStatement) throws Exception {
        Connection conn = null;
        try {
            conn = createConnection("allowMultiQueries=true");
        } catch (Exception e) {
            System.err.println("DefaultMysqlConnectionLair.runSimpleStatement > create connection");
            throw new Exception("Could not create connection to database");
        }
        DebuggerRunResult result = new DebuggerRunResult();

        try {
            Statement statement = conn.createStatement();
            long startTime = System.currentTimeMillis();
            statement.execute(sqlStatement);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet != null) {
                result.setTable(getMCLResultSetTable(resultSet));
                statement.getMoreResults();
                resultSet = statement.getResultSet();
                if (resultSet != null) {
                    System.err.println("DefaultMysqlConnectionLair.runSimpleStatement > I have next table, reimplement dto + method");
                }
            }
            long millisSeconds = (System.currentTimeMillis() - startTime);
            result.setExecuteTime(Long.toString(millisSeconds));

            conn.close();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return result;
    }


    /**
     * Na zaklade tokenu mi vytvori ako factory pravidlo
     *
     * @param ruleToken token string
     * @return implementacia pravidla
     */
    @Override
    public DatabaseMentorRule createRule(String ruleToken) throws ClassNotFoundException {
        DatabaseMentorRule[] rules = new DatabaseMentorRule[]{
                new RuleGroupBy(),
                new RuleHaving(),
                new RuleInnerSelect(),
                new RuleJoin(),
                new RuleLimit(),
                new RuleOrderBy(),
                new RuleUnion(),
                new RuleWhere()
        };
        for (DatabaseMentorRule rule : rules) {
            if (ruleToken.equalsIgnoreCase(rule.getDebuggerRuleInfo().getName())) {
                return rule;
            }
        }
        throw new ClassCastException("I do not know this rule" + ruleToken);
    }

    private int getSqlType(String name) {
        if ("BIT".equalsIgnoreCase(name)) {
            return Types.BIT;
        } else if ("TINYINT".equalsIgnoreCase(name)) {
            return Types.TINYINT;
        } else if ("SMALLINT".equalsIgnoreCase(name)) {
            return Types.SMALLINT;
        } else if ("INTEGER".equalsIgnoreCase(name)) {
            return Types.INTEGER;
        } else if ("BIGINT".equalsIgnoreCase(name)) {
            return Types.FLOAT;
        } else if ("REAL".equalsIgnoreCase(name)) {
            return Types.REAL;
        } else if ("DOUBLE".equalsIgnoreCase(name)) {
            return Types.DOUBLE;
        } else if ("NUMERIC".equalsIgnoreCase(name)) {
            return Types.NUMERIC;
        } else if ("DECIMAL".equalsIgnoreCase(name)) {
            return Types.DECIMAL;
        } else if ("CHAR".equalsIgnoreCase(name)) {
            return Types.CHAR;
        } else if ("VARCHAR".equalsIgnoreCase(name)) {
            return Types.VARCHAR;
        } else if ("LONGVARCHAR".equalsIgnoreCase(name)) {
            return Types.LONGVARCHAR;
        } else if ("DATE".equalsIgnoreCase(name)) {
            return Types.DATE;
        } else if ("TIME".equalsIgnoreCase(name)) {
            return Types.TIME;
        } else if ("TIMESTAMP".equalsIgnoreCase(name)) {
            return Types.TIMESTAMP;
        } else if ("BINARY".equalsIgnoreCase(name)) {
            return Types.BINARY;
        } else if ("VARBINARY".equalsIgnoreCase(name)) {
            return Types.VARBINARY;
        } else if ("LONGVARBINARY".equalsIgnoreCase(name)) {
            return Types.LONGVARBINARY;
        } else if ("NULL".equalsIgnoreCase(name)) {
            return Types.NULL;
        } else if ("OTHER".equalsIgnoreCase(name)) {
            return Types.OTHER;
        } else if ("JAVA_OBJECT".equalsIgnoreCase(name)) {
            return Types.JAVA_OBJECT;
        } else if ("DISTINCT".equalsIgnoreCase(name)) {
            return Types.DISTINCT;
        } else if ("STRUCT".equalsIgnoreCase(name)) {
            return Types.STRUCT;
        } else if ("ARRAY".equalsIgnoreCase(name)) {
            return Types.ARRAY;
        } else if ("BLOB".equalsIgnoreCase(name)) {
            return Types.BLOB;
        } else if ("CLOB".equalsIgnoreCase(name)) {
            return Types.CLOB;
        } else if ("REF".equalsIgnoreCase(name)) {
            return Types.REF;
        } else if ("DATALINK".equalsIgnoreCase(name)) {
            return Types.DATALINK;
        } else if ("BOOLEAN".equalsIgnoreCase(name)) {
            return Types.BOOLEAN;
        } else if ("ROWID".equalsIgnoreCase(name)) {
            return Types.ROWID;
        } else if ("NCHAR".equalsIgnoreCase(name)) {
            return Types.NCHAR;
        } else if ("NVARCHAR".equalsIgnoreCase(name)) {
            return Types.NVARCHAR;
        } else if ("LONGNVARCHAR".equalsIgnoreCase(name)) {
            return Types.LONGNVARCHAR;
        } else if ("NCLOB".equalsIgnoreCase(name)) {
            return Types.NCLOB;
        } else if ("SQLXML".equalsIgnoreCase(name)) {
            return Types.SQLXML;
        } else {
            return Types.OTHER;
        }
    }

    private void getReturnArgsFromCallableStatement(CallableStatement cs, ProcedureArgInfo[] argsToCheck, ProcedureCallResult result)
            throws SQLException {
        //check OUT a INOUT argumetov
        for (ProcedureArgInfo i : argsToCheck) {
            switch (i.getType()) {
                case 1://IN nevychadza
                    break;
                case 2:
                    System.out.println("DefaultMysqlConnectionLair.getReturnArgsFromCallableStatement > " +
                            "INOUT: @" + i.getName() + "=" + cs.getString(i.getPosition()));
                    result.getArguments_inout().put(i.getName(), cs.getString(i.getPosition()));
                    break;
                case 3:
                    System.out.println("DefaultMysqlConnectionLair.getReturnArgsFromCallableStatement > " +
                            "OUT: @" + i.getName() + "=" + cs.getString(i.getPosition()));
                    result.getArguments_out().put(i.getName(), cs.getString(i.getPosition()));
                    break;
                case 4:
                    System.out.println("DefaultMysqlConnectionLair.getReturnArgsFromCallableStatement > " +
                            "RETURN AS OUT: @" + i.getName() + "=" + cs.getString(i.getPosition()));
                    result.getArguments_out().put(i.getName(), cs.getString(i.getPosition()));
                    break;
                default:
                    System.err.println("DefaultMysqlConnectionLair.getReturnArgsFromCallableStatement > nenznami typ argumentu");
                    break;
            }
        }
    }

    private String getReturnValueForFunction(ResultSet resultSet) throws SQLException {
        //mal by byt len jeden riadok
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<String> parseQuickSqlCode(String quickSqlCode) {
        List<String> list = new ArrayList<String>();
        String cast = "";

        String[] kusy = quickSqlCode.split("\n");

        for (int i = 0; i < kusy.length; i++) {
            if (kusy[i].startsWith("--") || kusy[i].startsWith("#")) {
                continue;
            } else if (kusy[i].contains("DELIMITER")) {
                while (!kusy[i].contains("DELIMITER ;")) {
                    i++;
                }
            } else if (kusy[i].contains("/*!")) {
                list.add(kusy[i]);
            } else if (kusy[i].contains("/*")) {
                if (!cast.equals("")) {
                    cast = cast + kusy[i].substring(0, kusy[i].indexOf("/*")) + "\n";
                } else {
                    list.add(kusy[i].substring(0, kusy[i].indexOf("/*")));
                }
                while (!kusy[i].contains("*/")) {
                    i++;
                }
            } else if (kusy[i].contains(";")) {
                if (!cast.equals("")) {
                    list.add(cast + kusy[i].substring(0, kusy[i].indexOf(";") + 1));
                    cast = "";
                } else {
                    list.add(kusy[i].substring(0, kusy[i].indexOf(";") + 1));
                }
            } else if (!kusy[i].contains(";") && kusy[i].contains("--")) {
                cast = cast + kusy[i].substring(0, kusy[i].indexOf("--")) + "\n";
            } else if (!kusy[i].contains(";")) {
                cast = cast + kusy[i] + "\n";
                if (i == kusy.length - 1)
                    list.add(cast);
            }
        }
        return list;
    }
}
