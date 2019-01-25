package com.novartis.connectors.servlets;
import com.novartis.connectors.prep.GetPageElementsPrep;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andrew Daawin
 */
public class getpageelements extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
             String xml = convertRequest2String(request);
            GetPageElementsPrep prep = new GetPageElementsPrep();
            String res = prep.processXML(xml);
            out.println(res);
        } catch (Exception e) {
            out.print(e.toString());
        } finally { 
            out.close();
        }
    } 

   
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

     private String convertRequest2String(HttpServletRequest request)throws IOException
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("");
	BufferedReader reader = request.getReader();
        String line;
	while((line = reader.readLine()) != null){
            strBuilder.append(line);
	}
        return strBuilder.toString();
    }

}
