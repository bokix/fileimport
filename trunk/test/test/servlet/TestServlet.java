package test.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import test.bean.Work;
import fileimport.IImportRunner;
import fileimport.ImportRunnerFactroy;
import fileimport.bean.ImportError;
import fileimport.bean.ImportResult;
import fileimport.bean.Mapping;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String configfile = "test.xls.xml";

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(ServletFileUpload.isMultipartContent(req)){
			System.out.println("doImport....");
			doImport(req,resp);
		}else{
			System.out.println("doGetTemple....");
			doGetTemple(req,resp);
		}
		

	}


	private void doGetTemple(HttpServletRequest req, HttpServletResponse resp) {
		IImportRunner runner = null;
		try {

			runner = ImportRunnerFactroy.getRunner(configfile, req);

			HSSFWorkbook book = runner.getTempleteFile();
			
			resp.setHeader("Content-disposition", "attachment; filename=test-temple.xls");
			book.write(resp.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private void doImport(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		
		IImportRunner runner = null;
		List list = null, error = null;
		ImportResult result = null;
		try {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");
			List /* FileItem */items = upload.parseRequest(req);

			runner = ImportRunnerFactroy.getRunner(configfile, req);

			result = runner.importFile(((FileItem) items.get(0))
					.getInputStream());
			List header = runner.getRealHeader();
			System.out.println("--------------");
			if(header!=null){
				for (int i = 0; i < header.size(); i++) {
					Mapping m = (Mapping) header.get(i);
					System.out.println(m);
				}
			}
			System.out.println("--------------");
		} catch (Exception e) {
			e.printStackTrace();
		}

		list = result.getSuccessList();
		error = result.getErrorList();
		resp.setCharacterEncoding("utf-8");

		if (list.size() > 0) {

			resp.getWriter().write("<p>导入成功的数据:</p>");
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Work object = (Work) iterator.next();
				resp.getWriter().write("◆" + object.toString() + "<br/>");

			}
		}
		if (error.size() > 0) {

			resp.getWriter().write("<br/><br/><p>导入出错的数据:</p>");
			for (Iterator iterator = error.iterator(); iterator.hasNext();) {
				ImportError object = (ImportError) iterator.next();
				resp.getWriter().write("◆" + object.toString() + "<br/>");

			}
		}
		
	}
}
