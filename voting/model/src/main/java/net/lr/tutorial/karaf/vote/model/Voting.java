package net.lr.tutorial.karaf.vote.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlType
public class Voting {
	private static final Logger LOG = LoggerFactory.getLogger(Voting.class);
	private String topic;
	private List<Vote> votes;
	
	public Voting() {
		this.votes = new ArrayList<Vote>();
	}

	public Voting(String topic) {
		this();
		this.topic = topic;
	}
	
	@XmlElement
	public double getAverage() {
		int sum = 0;
		for (Vote vote : votes) {
			sum += vote.getVote();
		}
		return new Double(sum) / votes.size();
	}

	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@XmlElement(name="vote")
	public List<Vote> getVotes() {
		return votes;
	}

	public void addVote(Vote vote) {
		this.votes.add(vote);
		LOG.info("Adding vote {} on topic {}", vote.getVote(), vote.getTopic());
	}

	public String getStats() {
		VoteStats stats = new VoteStats();
		for (Vote vote : votes) {
			stats.countVote(vote);
		}
		return stats.getStats();
	}

}
