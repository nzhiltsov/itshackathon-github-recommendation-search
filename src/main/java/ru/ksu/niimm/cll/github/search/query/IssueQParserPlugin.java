package ru.ksu.niimm.cll.github.search.query;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;

/**
 * @author Nikita Zhiltsov
 */
public class IssueQParserPlugin extends QParserPlugin {

    @Override
    public void init(NamedList args) {
        SolrParams.toSolrParams(args);
    }

    @Override
    public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
        return new IssueParser(qstr, localParams, params, req);
    }

    private static class IssueParser extends QParser {
        private Query innerQuery;

        public IssueParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
            super(qstr, localParams, params, req);
            try {
                QParser parser = getParser(qstr, "lucene", getReq());
                this.innerQuery = parser.parse();
            } catch (SyntaxError syntaxError) {
                throw new RuntimeException("An error has occurred while parsing the query:" + qstr, syntaxError);
            }
        }

        @Override
        public Query parse() throws SyntaxError {
            return new IssueQuery(innerQuery);
        }
    }
}
