package ru.ksu.niimm.cll.github.search.query;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ksu.niimm.cll.github.search.util.LuceneUtil$;

import java.io.IOException;

/**
 * @author Nikita Zhiltsov
 */
public class IssueQuery extends CustomScoreQuery {
    private final Logger logger = LoggerFactory.getLogger("search.IssueQuery");

    public IssueQuery(Query subQuery) {
        super(subQuery);
    }

    @Override
    protected CustomScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException {
        return new IssueScorerProvider(context);
    }

    class IssueScorerProvider extends CustomScoreProvider {
        public IssueScorerProvider(AtomicReaderContext context) {
            super(context);
        }

        @Override
        public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
            Document document = context.reader().document(doc);
            float languageScore = 0;
            if (document.getField("languages") != null)
                languageScore = LuceneUtil$.MODULE$.jaccardSimilarity(document, "languages", getSubQuery());

            float frameworkScore = 0;
            if (document.getField("frameworks") != null)
                frameworkScore = LuceneUtil$.MODULE$.jaccardSimilarity(document, "frameworks", getSubQuery());

            float tagScore = 0;
            if (document.getField("tags") != null)
                tagScore = LuceneUtil$.MODULE$.tagScore(document, "tags");
            float starScore = 0;
            if (document.getField("stars") != null) {
                float starsValue = document.getField("stars").numericValue().floatValue();
                if (starsValue > 0) {
                    starScore = new Float(Math.log10(starsValue));
                }
            }
            float watchScore = 0;
            if (document.getField("watches") != null) {
                float watchesValue = document.getField("watches").numericValue().floatValue();
                if (watchesValue > 0) {
                    watchScore = new Float(Math.log10(watchesValue));
                }
            }

            return 5 * languageScore + 10 * frameworkScore + 3 * tagScore + 0.2f * starScore + 0.2f * watchScore;
        }

    }
}
