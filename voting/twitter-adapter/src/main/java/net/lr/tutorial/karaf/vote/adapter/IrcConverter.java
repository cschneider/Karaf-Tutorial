package net.lr.tutorial.karaf.vote.adapter;

import java.util.Date;

import org.apache.camel.Header;

import net.lr.tutorial.karaf.vote.model.Vote;

public class IrcConverter {
	public Vote textToVote(String chatLine, @Header("irc.user.nick") String userNick) {
		String[] parts = chatLine.split(" ");
		String topic = parts[0];
		int voteNum = Integer.parseInt(parts[1]);
		return new Vote(topic, voteNum, userNick, new Date());
	}
}
