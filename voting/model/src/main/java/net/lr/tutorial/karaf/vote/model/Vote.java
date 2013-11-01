package net.lr.tutorial.karaf.vote.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Vote {
	String topic;
	int vote;
	String fromUser;
	Date voteDateTime;
	
	public Vote() {
	}
	
	public Vote(String topic, int vote) {
		this(topic, vote, "Anonymous", new Date());
	}
	
	public Vote(String topic, int vote, String fromUser, Date voteDateTime) {
		super();
		this.topic = topic;
		setVote(vote);
		this.fromUser = fromUser;
		this.voteDateTime = voteDateTime;
	}

	public int getVote() {
		return vote;
	}

	public String getFromUser() {
		return fromUser;
	}

	public Date getVoteDateTime() {
		return voteDateTime;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setVote(int vote) {
		if (!isValid(vote)) {
			throw new IllegalArgumentException("Vote must be a number between 1 and 6");
		}
		this.vote = vote;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public void setVoteDateTime(Date voteDateTime) {
		this.voteDateTime = voteDateTime;
	}

	public static boolean isValid(int vote) {
		return vote >= 1 && vote <= 6;
	}
	
}
