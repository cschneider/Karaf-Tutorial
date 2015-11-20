package net.lr.tutorial.karaf.cxf.personrest.webui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lr.tutorial.karaf.cxf.personrest.model.Person;
import net.lr.tutorial.karaf.cxf.personrest.model.PersonService;

public class PersonServlet extends HttpServlet {
    private static final long serialVersionUID = -8444651625768440903L;
    private PersonService personService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream os = resp.getOutputStream();
        Person[] persons = personService.getAll();
        os.println("<html><body>");
        os.println("<h2>Persons</h2>");
        os.println("<table>");
        os.println("<tr><th>Id</th><th>Name</th><th>URL</th></tr>");
        for (Person person : persons) {
            String url = (person.getUrl() == null) ? "" : person.getUrl();
            os.println(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", person.getId(), person.getName(), url));
        }
        os.println("</table>");
        os.println("<h2>Add Person</h2>");
        os.println("<form name='input' action='/personuirest' method='post'>");
        os.println("<table>");
        os.println("<tr><td>Id</td><td><input type='text' name='id'/></td></tr>");
        os.println("<tr><td>Name</td><td><input type='text' name='name'/></td></tr>");
        os.println("<tr><td>Homepage URL</td><td><input type='text' name='url'/></td></tr>");
        os.println("<tr><td colspan='2'><input type='submit' value='Add'/></td></tr>");
        os.println("</form>");
        os.println("</table>");
        os.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String url = req.getParameter("url");
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setUrl(url);
        personService.addPerson(person );
        resp.sendRedirect("/personuirest");
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
    
    
}
