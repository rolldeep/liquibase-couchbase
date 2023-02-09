package integration.statement;

import com.couchbase.client.java.manager.query.CreateQueryIndexOptions;
import com.wdt.couchbase.Keyspace;
import common.BucketTestCase;
import liquibase.ext.couchbase.statement.DropIndexStatement;
import org.junit.jupiter.api.Test;

import static com.couchbase.client.java.manager.query.CreateQueryIndexOptions.createQueryIndexOptions;
import static com.wdt.couchbase.Keyspace.keyspace;
import static common.constants.TestConstants.DEFAULT_COLLECTION;
import static common.constants.TestConstants.DEFAULT_SCOPE;
import static common.constants.TestConstants.FIELD_1;
import static common.constants.TestConstants.INDEX;
import static common.constants.TestConstants.TEST_BUCKET;
import static common.constants.TestConstants.TEST_COLLECTION;
import static common.constants.TestConstants.TEST_DOCUMENT;
import static common.constants.TestConstants.TEST_ID;
import static common.constants.TestConstants.TEST_SCOPE;
import static common.matchers.CouchBaseClusterAssert.assertThat;
import static java.util.Collections.singletonList;


class DropIndexStatementTest extends BucketTestCase {

    @Test
    void Should_drop_existing_index_in_default_scope() {
        insertDocInDefaultScope(DEFAULT_COLLECTION, TEST_ID, TEST_DOCUMENT);
        createIndexInDefaultScope(TEST_BUCKET, INDEX);

        DropIndexStatement statement = new DropIndexStatement(INDEX, TEST_BUCKET, DEFAULT_COLLECTION, DEFAULT_SCOPE);
        statement.execute(database.getConnection());

        assertThat(cluster).queryIndexes(TEST_BUCKET).doesNotHave(INDEX);
        removeDocFromDefaultScope(DEFAULT_COLLECTION, TEST_ID);
    }

    @Test
    void Should_drop_index_for_specific_keyspace() {
        insertDocInTestCollection(TEST_ID, TEST_DOCUMENT);
        Keyspace keyspace = keyspace(TEST_BUCKET, TEST_SCOPE, TEST_COLLECTION);
        createIndex(keyspace, INDEX);

        DropIndexStatement statement = new DropIndexStatement(INDEX, TEST_BUCKET, TEST_COLLECTION, TEST_SCOPE);
        statement.execute(database.getConnection());

        assertThat(cluster).queryIndexes(TEST_BUCKET).doesNotHave(INDEX);
        removeDocFromTestCollection(TEST_ID);
    }

    private void createIndexInDefaultScope(String bucket, String indexName) {
        cluster.queryIndexes().createIndex(bucket, indexName, singletonList(FIELD_1));
    }

    private void createIndex(Keyspace keyspace, String indexName) {
        CreateQueryIndexOptions options = createQueryIndexOptions()
                .collectionName(keyspace.getCollection())
                .scopeName(keyspace.getScope());
        cluster.queryIndexes()
                .createIndex(keyspace.getBucket(), indexName, singletonList(FIELD_1), options);
    }

}