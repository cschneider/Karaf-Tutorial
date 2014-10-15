package net.lr.tutorial.karaf.cxf.personrest.model;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_XML)
public interface PersonService {
    @GET
    @Path("/")
    public Person[] getAll();
    
    @GET
    @Path("/{id}")
    public Person getPerson(@PathParam("id") String id);
    
    @PUT
    @Path("/{id}")
    public void updatePerson(@PathParam("id") String id, Person person);
    
    @POST
    @Path("/")
    public void addPerson(Person person);
}
