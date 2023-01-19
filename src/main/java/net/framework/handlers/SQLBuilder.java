package net.framework.handlers;

public interface SQLBuilder {
    SearchQuery buildSearchQuery(Object... argsForQuery);
}
