package net.lr.tutorial.karaf.vote.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlRootElement
public class VoteStats {
	Integer[] stats;
	String[][] table;
	
	public VoteStats() {
		stats = new Integer[]{0,0,0,0,0,0};
		table = new String[7][2];
	}
	
	public void addVote(Vote vote) {
		stats[vote.getVote()-1]++;
	}

	@XmlElement
	public String getStats() {
		StringBuilder sb = new StringBuilder();
		sb.append("[[" + encode("Vote") + "," + encode("Count") + "],");
		for (int c=1; c<=6; c++) {
			sb.append("[" + encode("" + c) + "," + stats[c-1] + "]" );
			sb.append((c==6)?"]":",");
		}
		return sb.toString();
	}
	
	private String encode(String st) {
		return "\""+ st + "\"";
	}
	
	
}
