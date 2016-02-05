package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureCallResult;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.ProceduresService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 10.02.14
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public class ProceduresServiceImpl extends RemoteServiceServlet implements ProceduresService {
    @Override
    public String[] getAllProceduresNames() throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            System.out.println("EXCEPTION PRI SLEEP");
        }

        String[] arrayOfProcedures = new String[]{};
        try {
            arrayOfProcedures = mentorConnectionLair.getAllProceduresNames();
        } catch (Exception e) {
            System.err.println("ProceduresServiceImpl.getAllProceduresNames > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return arrayOfProcedures;
    }

    @Override
    public Map<String, String> getProcedureDetails(String procedureName) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        Map<String, String> map = new HashMap<String, String>(0);
        try {
            map = mentorConnectionLair.getProcedureDetailInfo(procedureName);
        } catch (Exception e) {
            System.err.println("ProceduresServiceImpl.getProcedureDetails > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return map;
    }

    @Override
    public String getProcedureCode(String procedureName) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        String code = "";
        try {
            code = mentorConnectionLair.getProcedureCode(procedureName);
        } catch (Exception e) {
            System.err.println("ProceduresServiceImpl.getProcedureCode > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return code;
    }

    @Override
    public List<ProcedureArgInfo> getProcedureArguments(String procedureName) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        List<ProcedureArgInfo> list = null;
        try {
            list = mentorConnectionLair.getProcedureArgumentsInfo(procedureName);
        } catch (Exception e) {
            System.err.println("ProceduresServiceImpl.getProcedureArguments > EXP: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return list;
    }

    @Override
    public ProcedureCallResult callProcedure(String procedureName, ProcedureArgInfo[] args) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());
        try {
            return mentorConnectionLair.callProcedure(procedureName, args);
        } catch (Exception e) {
            throw new RPCServiceException(e.getMessage());
        }
    }
}