package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.FunctionsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public class FunctionsServiceImpl extends RemoteServiceServlet implements FunctionsService {
    @Override
    public String[] getAllFunctionsNames() throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            System.out.println("EXCEPTION PRI SLEEP");
        }

        String[] arrayOfFunctions = new String[]{};

        try {
            arrayOfFunctions = mentorConnectionLair.getAllFunctionsNames();
        } catch (Exception e) {
            System.err.println("FunctionsServiceImpl.getAllFunctionsNames> EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return arrayOfFunctions;
    }

    @Override
    public Map<String, String> getFunctionDetails(String functionName) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        Map<String, String> map = new HashMap<String, String>(0);
        try {
            map = mentorConnectionLair.getFunctionDetailInfo(functionName);
        } catch (Exception e) {
            System.err.println("FunctionsServiceImpl.getFunctionDetails > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return map;
    }

    @Override
    public String getFunctionCode(String functionName) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        String code = "";
        try {
            code = mentorConnectionLair.getFunctionCode(functionName);
        } catch (Exception e) {
            System.err.println("FunctionsServiceImpl.getFunctionCode > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return code;
    }

    @Override
    public List<FunctionArgInfo> getFunctionArguments(String functionName) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        List<FunctionArgInfo> list = null;
        try {
            list = mentorConnectionLair.getFunctionArgumentsInfo(functionName);
        } catch (Exception e) {
            System.err.println("FunctionsServiceImpl.getFunctionArguments > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return list;
    }

    @Override
    public String callFunction(String functionName, FunctionArgInfo[] args) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());
        try {
            return mentorConnectionLair.callFunction(functionName, args);
        } catch (Exception e) {
            throw new RPCServiceException(e.getMessage());
        }
    }
}