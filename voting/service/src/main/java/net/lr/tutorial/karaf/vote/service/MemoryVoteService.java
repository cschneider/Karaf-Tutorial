package net.lr.tutorial.karaf.vote.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import net.lr.tutorial.karaf.vote.model.Vote;
import net.lr.tutorial.karaf.vote.model.VoteService;
import net.lr.tutorial.karaf.vote.model.Voting;

public class MemoryVoteService implements VoteService {
	Map<String, Voting> votingMap;
	
	public MemoryVoteService() {
		this.votingMap = new HashMap<String, Voting>();
		addVote("test1", 1);
	}

	@Override
	public Voting getVoting(String topic) {
		if (this.votingMap.containsKey(topic)) {
			return votingMap.get(topic);
		} else {
			Voting voting = new Voting(topic);
			this.votingMap.put(topic, voting);
			return voting;
		}
	}

	@Override
	public Voting removeVoting(String topic) {
		return this.votingMap.remove(topic);
	}

	@Override
	public void addVote(String topic, int voteNum) {
		getVoting(topic).addVote(new Vote(topic, voteNum));
	}

	@Override
	public Response getVotings() {
		Link[] links = new Link[votingMap.size()];
		int c = 0;
		for (Voting voting : votingMap.values()) {
			links[c++] = Link.fromResourceMethod(VoteService.class, "getVoting",  voting.getTopic()).build(); 
		}
		return Response.ok().links(links).build();
	}

	@Override
	public void addVote(Vote vote) {
		if (vote != null) {
			getVoting(vote.getTopic()).addVote(vote);
		}
	}

	@Override
	public String getStats(String topic) {
		return getVoting(topic).getStats();
	}

}
