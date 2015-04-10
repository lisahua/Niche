package seal.niche.empirical.statistic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public class ConfigUtility {

	public static final double PURITY_VERB_RATIO_THRESHOLD = 0.3;
	private static Set<String> PURITY_VERBS = new HashSet<String>();
	private static String[] purityVerbs = { "total", "postings", "filtered",
			"calculate", "float", "missing", "matches", "time", "threads",
			"search", "thread", "fs", "boolean", "flyweight", "decrement",
			"clone", "translog", "parent", "number", "suggest", "content",
			"aggregation", "hits", "explicit", "prefix", "bottom", "array",
			"param", "before", "next", "bucket", "use", "jvm", "collection",
			"date", "estimate", "meta", "timestamp", "document", "warmer",
			"token", "sync", "mappings", "current", "map", "tokenizer",
			"customs", "ignore", "direct", "previous", "needs", "allocate",
			"factor", "blocks", "ttl", "bulk", "backing", "tx", "num", "new",
			"doc", "exists", "slice", "successful", "keep", "x", "to",
			"gateway", "docs", "terms", "settings", "round", "long", "same",
			"must", "has", "after", "shards", "scorer", "updated", "any",
			"replica", "key", "fields", "index", "text", "geo", "get",
			"explanation", "double", "rescore", "size", "node", "names",
			"similarity", "active", "make", "ifconfig", "valid", "sub",
			"ignored", "can", "annotated", "routing", "frequency", "consume",
			"child", "throw", "repository", "fuzzy", "name", "types", "top",
			"track", "recycler", "assigned", "as", "comparator", "recovery",
			"source", "ram", "actual", "handler", "accessed", "assign",
			"numeric", "reason", "filtering", "accept", "excludes", "indexing",
			"score", "payload", "info", "open", "spawn", "file", "initial",
			"pending", "rejected", "warmers", "combine", "only", "refresh",
			"fetch", "os", "op", "maybe", "equals", "or", "delta", "compare",
			"random", "prepare", "health", "create", "non", "include",
			"obtain", "realtime", "now", "http", "indices", "addresses",
			"regexp", "rebalance", "position", "evaluate", "stream",
			"repositories", "bytes", "segments", "range", "query", "failed",
			"facets", "mapper", "mapping", "build", "aliases", "version",
			"formatter", "distance", "judge", "transport", "compile", "end",
			"completed", "failures", "resolve", "snapshot", "should", "idle",
			"phase1", "persistent", "nodes", "uid", "apply", "cause", "unwrap",
			"count", "last", "char", "contains", "type", "heap", "sent",
			"more", "hit", "request", "primary", "action", "value", "required",
			"custom", "wrap", "iterator", "hash", "scroll", "status", "int",
			"aggregations", "arrive", "default", "parsed", "rest", "match",
			"uncompress", "parser", "scope", "allow", "stats", "state",
			"convert", "calc", "object", "from", "includes", "stage",
			"network", "id", "shard", "full", "bits", "listener", "path",
			"peek", "find", "less", "is", "error", "added", "reuse", "check",
			"alias", "ack", "executor", "origin", "concat", "in", "searcher",
			"term", "address", "context", "target", "removed", "local",
			"modules", "group", "first", "smart", "timeout", "river" };

	public static final double UP_CLUSTER_INCONSISTENT_THRESHOLD = 0.3;
	public static final int UP_SIMILAR_THRESHOLD = 2;// unable to change based
														// on implementation now.
	public static final int UP_INCONSISTENT_CLUSTER = 3;

	public static Set<String> getPURITY_VERBS() {
		if (PURITY_VERBS.size() == 0)
			PURITY_VERBS.addAll(Arrays.asList(purityVerbs));
		return PURITY_VERBS;
	}

	public static void setPURITY_VERBS(Set<String> pURITY_VERBS) {
		PURITY_VERBS = pURITY_VERBS;
	}

}
