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
	
	public Vote(int vote, String fromUser, Date voteDateTime) {
		super();
		this.vote = vote;
		this.fromUser = fromUser;
		this.voteDateTime = voteDateTime;
	}

	public Vote(int i) {
		this(i, "Anonymous", new Date());
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

	public void setVote(int vote) {
		this.vote = vote;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public void setVoteDateTime(Date voteDateTime) {
		this.voteDateTime = voteDateTime;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public boolean isValid() {
		return vote >= 1 && vote <= 6;
	}
	
}
