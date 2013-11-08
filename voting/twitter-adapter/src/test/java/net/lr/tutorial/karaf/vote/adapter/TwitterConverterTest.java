package net.lr.tutorial.karaf.vote.adapter;

import java.util.Date;

import net.lr.tutorial.karaf.vote.model.Vote;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import twitter4j.Status;

public class TwitterConverterTest {

	@Test
	public void testConvertStatus() {
		TwitterConverter converter = new TwitterConverter();
		
		Status tweet = EasyMock.createMock(Status.class);
		EasyMock.expect(tweet.getSource()).andReturn("schneider_chris").anyTimes();
		Date now = new Date();
		EasyMock.expect(tweet.getCreatedAt()).andReturn(now).anyTimes();
		EasyMock.expect(tweet.getText()).andReturn("#camel mytopic 2").anyTimes();
		EasyMock.replay(tweet);
		Vote vote = converter.convert(tweet);
		Assert.assertEquals(tweet.getSource(), vote.getFromUser());
		Assert.assertEquals(now, vote.getVoteDateTime());
		Assert.assertEquals(2, vote.getVote());
		Assert.assertEquals("mytopic", vote.getTopic());
		EasyMock.verify(tweet);
	}
}
