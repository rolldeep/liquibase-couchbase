package liquibase.ext.couchbase.change;


import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.ext.couchbase.statement.RemoveDocumentsStatement;
import liquibase.ext.couchbase.types.Id;
import liquibase.ext.couchbase.types.Keyspace;
import liquibase.statement.SqlStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static liquibase.ext.couchbase.types.Keyspace.keyspace;

/**
 * Removes document(-s) by id(-s) or id range
 * @link <a href="https://docs.couchbase.com/java-sdk/3.3/howtos/kv-operations.html#insert">Reference documentation</a>
 * @see RemoveDocumentsStatement
 * @see Keyspace
 */

@Getter
@Setter
@DatabaseChange(
        name = "removeDocuments",
        description = "Remove multiple documents from keyspace https://docs.couchbase.com/java-sdk/3.3/howtos/kv-operations.html",
        priority = ChangeMetaData.PRIORITY_DEFAULT,
        appliesTo = {"collection", "database"}
)
@NoArgsConstructor
@AllArgsConstructor
public class RemoveDocumentsChange extends CouchbaseChange {

    private String bucketName;
    private String scopeName;
    private String collectionName;
    private List<Id> ids = new ArrayList<>();

    @Override
    public String getConfirmationMessage() {
        return String.format("Documents removed from collection %s", collectionName);
    }

    @Override
    public SqlStatement[] generateStatements() {
        Keyspace keyspace = keyspace(bucketName, scopeName, collectionName);
        return new SqlStatement[] {
                new RemoveDocumentsStatement(keyspace, ids)
        };
    }

}
