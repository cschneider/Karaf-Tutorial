package net.lr.tutorial.karaf.vote.model;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("")
public interface VoteService {
	@Path("")
	@GET
	Response getVotings();

	@Path("{topic}")
	@GET
	Voting getVoting(@PathParam("topic") String topic);
	
	@Path("{topic}/stats")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	String getStats(@PathParam("topic") String topic);
	
	@Path("{topic}")
	@DELETE
	Voting removeVoting(@PathParam("topic") String topic);
	
	@Path("")
	@PUT
	void addVote(Vote vote);
	
	@Path("{topic}/vote/{vote}")
	@GET
	void addVote(@PathParam("topic") String topic, @PathParam("vote") int vote);
}
