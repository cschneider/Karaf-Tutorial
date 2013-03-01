package net.lr.tutorial.karaf.vote.model;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class VotingLink {
	String topic;
	String link;
	
	public VotingLink() {
	}
	
	public VotingLink(String topic, String link) {
		this.topic = topic;
		this.link = link;	
	}

	public String getTopic() {
		return topic;
	}

	public String getLink() {
		return link;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
