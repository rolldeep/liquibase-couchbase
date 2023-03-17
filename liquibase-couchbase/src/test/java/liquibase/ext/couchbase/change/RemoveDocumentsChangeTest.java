package liquibase.ext.couchbase.change;

import common.TestChangeLogProvider;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.ext.couchbase.changelog.ChangeLogProvider;
import liquibase.ext.couchbase.database.CouchbaseLiquibaseDatabase;
import liquibase.ext.couchbase.types.Id;
import org.junit.jupiter.api.Test;

import java.util.List;

import static common.constants.ChangeLogSampleFilePaths.REMOVE_MANY_TEST_XML;
import static common.constants.ChangeLogSampleFilePaths.REMOVE_ONE_TEST_XML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.collections.Iterables.firstOf;

class RemoveDocumentsChangeTest {
    private final Id ID_1 = new Id("id1");
    private final Id ID_2 = new Id("id2");

    private final CouchbaseLiquibaseDatabase database = mock(CouchbaseLiquibaseDatabase.class);
    private final ChangeLogProvider changeLogProvider = new TestChangeLogProvider(database);

    @Test
    void Should_have_correct_change_type() {
        DatabaseChangeLog changeLog = changeLogProvider.load(REMOVE_MANY_TEST_XML);
        assertThat(changeLog.getChangeSets())
                .flatMap(ChangeSet::getChanges)
                .withFailMessage("Changelog contains wrong types")
                .hasOnlyElementsOfType(RemoveDocumentsChange.class);
    }

    @Test
    void Should_contains_correct_bucket() {
        DatabaseChangeLog changeLog = changeLogProvider.load(REMOVE_MANY_TEST_XML);
        ChangeSet changeSet = firstOf(changeLog.getChangeSets());
        RemoveDocumentsChange change = (RemoveDocumentsChange) firstOf(changeSet.getChanges());
        assertThat(change.getBucketName()).isEqualTo("testBucket");
    }

    @Test
    void Should_contains_specific_documents() {
        DatabaseChangeLog changeLog = changeLogProvider.load(REMOVE_MANY_TEST_XML);
        ChangeSet changeSet = firstOf(changeLog.getChangeSets());
        RemoveDocumentsChange change = (RemoveDocumentsChange) firstOf(changeSet.getChanges());
        assertThat(change.getIds()).containsExactly(ID_1, ID_2);
    }

    @Test
    void Should_contains_specific_document() {
        DatabaseChangeLog changeLog = changeLogProvider.load(REMOVE_ONE_TEST_XML);
        ChangeSet changeSet = firstOf(changeLog.getChangeSets());
        RemoveDocumentsChange change = (RemoveDocumentsChange) firstOf(changeSet.getChanges());
        assertThat(change.getIds()).containsExactly(ID_1);
    }

    @Test
    void Should_contains_exactly_one_document_id() {
        DatabaseChangeLog changeLog = changeLogProvider.load(REMOVE_ONE_TEST_XML);
        ChangeSet changeSet = firstOf(changeLog.getChangeSets());
        List<Change> changes = changeSet.getChanges();
        assertThat(changes).hasSize(1);
        assertThat(((RemoveDocumentsChange) changes.get(0)).getIds()).hasSize(1);
    }
}

