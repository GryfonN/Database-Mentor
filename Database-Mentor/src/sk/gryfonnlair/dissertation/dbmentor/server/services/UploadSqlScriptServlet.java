package sk.gryfonnlair.dissertation.dbmentor.server.services;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
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
 * Date: 3/17/14
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadSqlScriptServlet extends HttpServlet {

    /**
     * curl  http://127.0.0.1:8888/database_mentor/UploadSqlScript?gwt.codesvr=127.0.0.1:9997
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("fungujem", "fungujem");
        System.out.println("UploadSqlScriptServlet.doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // process only multipart requests
        if (ServletFileUpload.isMultipartContent(req)) {

            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try {
                List<FileItem> items = upload.parseRequest(req);
                for (FileItem item : items) {


                    // process only file upload - discard other form item types
                    if (item.isFormField()) {
                        continue;
                    }

                    String fileName = item.getName();
                    String fieldName = item.getFieldName();
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();
                    System.out.println("UploadSqlScriptServlet.doPost > UPLOAD_FILE:" + fileName
                            + ", fieldName:" + fieldName
                            + ", contentType:" + contentType
                            + ", isInMemory:" + isInMemory
                            + ", sizeInBytes" + sizeInBytes);


                    String sqlCodeAsString = ServerUtil.readInputStream(item.getInputStream());
                    System.out.println("################################################################## \n");
//                    System.out.println("UploadSqlScriptServlet.doPost > #CODE : \n" + sqlCodeAsString);
                    req.getSession().setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER_QUICKCODE_UPLOAD_FILE, sqlCodeAsString);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    System.out.println("UploadSqlScriptServlet.doPost > UPLOAD_FILE ulozeny");
                    System.out.println("################################################################## \n");
                    return;
                }
            } catch (Exception e) {
                System.err.println("UploadSqlScriptServlet.doPost > UploadFile EXP: " + e.getMessage());
            }

        } else {
            System.err.println("UploadSqlScriptServlet.doPost > UploadFile Request contents type is not supported by the servlet.");
        }
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
