package net.lr.tutorial.karaf.cxf.personservice.person;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_XML)
@WebService
public interface PersonService {
    @GET
    @Path("/")
    public Person[] getAll();
    
    @GET
    @Path("/{id}")
    public Person getPerson(String id);
    
    @PUT
    @Path("/{id}")
    public void updatePerson(String id, Person person);
    
    @POST
    @Path("/")
    public void addPerson(Person person);
}
