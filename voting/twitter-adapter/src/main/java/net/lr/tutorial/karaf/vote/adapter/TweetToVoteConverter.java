package net.lr.tutorial.karaf.vote.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lr.tutorial.karaf.vote.model.Vote;
import twitter4j.Status;

public class TweetToVoteConverter {
	Logger LOG = LoggerFactory.getLogger(TweetToVoteConverter.class);
	
	public Vote convert(Status tweet) {
		try {
			String text = tweet.getText();
			String[] parts = text.split(" ");
			String topic = parts[1];
			int voteNum = Integer.parseInt(parts[2]);
			String fromUser = tweet.getSource();
			return new Vote(topic, voteNum, fromUser, tweet.getCreatedAt());
		} catch (Throwable  e) {
			LOG.info("Invalid tweet " + tweet.getText() + ": " + e.getMessage());
			return null;
		}
	}
}
