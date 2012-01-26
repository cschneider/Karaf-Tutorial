package net.lr.karaf.jms.service;

import javax.jms.Connection;

public interface ExtJmsServiceFactory {
    ExtJmsService create(final Connection connection);
    boolean canHandle(final Connection connection);
}
