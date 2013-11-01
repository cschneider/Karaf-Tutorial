h1. Overview

Tutorial how to use all integration features together to implement a Voting service that can be used through
rest and twitter and a web based UI. 

It shows how to:

- Create bundles using maven and the maven bundle plugin
- Wire bundles using blueprint and OSGi services
- Use the whiteboard pattern and the pax-web whiteboard extender to publish Servlets

h1. Structure

model           - Voting model and service interface
service         - Rest Service implementation of voting service
twitter-adapter - Adapter to allow voting throw twitter messages
ui              - User interface based on jquery and google charts 
features        - Feature file for simple installation on Apache Karaf

h1. Build

mvn clean install

h1. Twitter setup

You can skip this step if you want to only use the rest voting api.

Create a new Application at https://dev.twitter.com/apps .
Authorize the application to read your twitter account.

Create a config file in karaf etc/net.lr.tutorial.karaf.vote.adapter.cfg containing the properties below and paste the 
the values from the twitter setup you did above.

consumerKey=
consumerSecret=
accessToken=
accessTokenSecret=

h1. Installation

features:addurl mvn:net.lr.karaf.tutorial.voting/voting-features/1.0.0-SNAPSHOT/xml
features:install example-voting

h2. Test

Now in one browser window open the UI. Here you see how many votes were sent for the topic "tutorial".
The chart will automatically update
http://localhost:8181/vote-ui?topic=tutorial

You can also view the votes by accessing
http://localhost:8181/cxf/vote/tutorial

The simplest way to vote is by calling the URI. The last part is the vote from 1..6 where 1 is best.
http://localhost:8181/cxf/vote/tutorial/vote/1

You can also vote using twitter by sending the following from your account. #esbvote is the general filter so we do not get too
many tweets. The next param is the topic and the last is the vote again.
#esbvote tutorial 1
