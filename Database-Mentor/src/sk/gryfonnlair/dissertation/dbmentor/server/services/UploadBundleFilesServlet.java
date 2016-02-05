package sk.gryfonnlair.dissertation.dbmentor.server.services;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sk.gryfonnlair.dissertation.dbmentor.server.SessionKeys;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadBundleFilesServlet extends HttpServlet {

    static final String FIELD_ACTION_NAME = "action";
    static final String FIELD_ACTION_VALUE_JDBC = "jdbc";
    static final String FIELD_ACTION_VALUE_MODULE = "module";
    static final String FIELD_JAR_FILE_NAME = "jar_file";

    /**
     * curl -H "action:jdbc" -D - http://127.0.0.1:8888/database_mentor/UploadBundleFiles?gwt.codesvr=127.0.0.1:9997
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("UploadBundleFilesServlet.doGet");
        if (req.getHeader("action") != null) {
            String action = req.getHeader("action");
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            if ("jdbc".equals(action)) {
                resp.setHeader("action", "jdbc");
            } else if ("module".equals(action)) {
                resp.setHeader("action", "module");
            } else {
                resp.setHeader("PRECO_??", "nepoznam action");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            resp.setHeader("PRECO_??", "nezadal si action header");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * curl -X POST -d "action:jdbc" -D - http://127.0.0.1:8888/database_mentor/UploadBundleFiles?gwt.codesvr=127.0.0.1:9997
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // process only multipart requests
        if (ServletFileUpload.isMultipartContent(req)) {

            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            FileItem actionFileItem = null;
            FileItem jarFileItem = null;

            // Parse the request
            try {
                List<FileItem> items = upload.parseRequest(req);

                for (FileItem item : items) {
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();

                    if (FIELD_ACTION_NAME.equals(fieldName)) {
                        actionFileItem = item;
                    } else if (FIELD_JAR_FILE_NAME.equals(fieldName)) {
                        jarFileItem = item;
                    }
                }

                if (actionFileItem == null || jarFileItem == null) {
                    System.err.println("UploadBundleFilesServlet.doPost > NEUPLNY REQUEST ");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }

                if (FIELD_ACTION_VALUE_JDBC.equals(actionFileItem.getString())) {
                    req.getSession().setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN_DRIVER_FILEITEM, jarFileItem);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    System.out.println("###############################UploadBundleFilesServlet#########################\n");
                    System.out.println("UploadBundleFilesServlet.doPost > DRIVER ULOZENY");
                } else if (FIELD_ACTION_VALUE_MODULE.equals(actionFileItem.getString())) {
                    req.getSession().setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN_MODULE_FILEITEM, jarFileItem);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    System.out.println("###############################UploadBundleFilesServlet#########################\n");
                    System.out.println("UploadBundleFilesServlet.doPost > MODULE ULOZENY");
                } else {
                    resp.setHeader("PRECO_??", "nepoznam action");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    System.err.println("UploadBundleFilesServlet.doPost > neznama action");
                }
                return;
            } catch (Exception e) {
                System.err.println("UploadBundleFilesServlet.doPost > EXP: " + e.getMessage());
            }
        } else {
            System.err.println("UploadBundleFilesServlet.doPost > UploadFile Request contents type is not supported by the servlet.");
        }
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}