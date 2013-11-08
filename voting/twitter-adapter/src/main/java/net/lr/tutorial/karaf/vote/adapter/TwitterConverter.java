package net.lr.tutorial.karaf.vote.adapter;

import net.lr.tutorial.karaf.vote.model.Vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.DirectMessage;
import twitter4j.Status;

public class TwitterConverter {
	Logger LOG = LoggerFactory.getLogger(TwitterConverter.class);
	
	public Vote convert(DirectMessage message) {
		try {
			String text = message.getText();
			String[] parts = text.split(" ");
			String topic = parts[0];
			int voteNum = Integer.parseInt(parts[1]);
			String fromUser = message.getSender().getName();
			return new Vote(topic, voteNum, fromUser, message.getCreatedAt());
		} catch (Throwable  e) {
			LOG.info("Invalid tweet " + message.getText() + ": " + e.getMessage());
			return null;
		}
	}
	
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
