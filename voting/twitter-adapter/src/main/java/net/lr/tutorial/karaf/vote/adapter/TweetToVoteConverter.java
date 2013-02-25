package net.lr.tutorial.karaf.vote.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lr.tutorial.karaf.vote.model.Vote;
import twitter4j.Tweet;

public class TweetToVoteConverter {
	Logger LOG = LoggerFactory.getLogger(TweetToVoteConverter.class);
	
	public Vote convert(Tweet tweet) {
		try {
			String text = tweet.getText();
			String[] parts = text.split(" ");
			Vote vote = new Vote();
			vote.setTopic(parts[1]);
			vote.setVote(Integer.parseInt(parts[2]));
			vote.setFromUser(tweet.getFromUser());
			vote.setVoteDateTime(tweet.getCreatedAt());
			return vote;
		} catch (Throwable  e) {
			LOG.info("Invalid tweet " + tweet.getText());
			return null;
		}
	}
}
