package net.lr.tutorial.karaf.vote.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlRootElement
public class VotingLinks {
	private List<VotingLink> votings;

	public VotingLinks() {
		this.votings = new ArrayList<VotingLink>();
	}
	
	@XmlElement(name="votingLink")
	public List<VotingLink> getVotingLinks() {
		return votings;
	}
}
