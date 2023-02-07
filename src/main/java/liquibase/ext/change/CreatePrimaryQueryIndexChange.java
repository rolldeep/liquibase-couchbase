package liquibase.ext.change;

import com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.statement.CreatePrimaryQueryIndexStatement;
import liquibase.servicelocator.PrioritizedService;
import liquibase.statement.SqlStatement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseChange(
        name = "createPrimaryQueryIndex",
        description = "Create primary query index with validation "
                + "https://docs.couchbase.com/server/current/n1ql/n1ql-language-reference/createprimaryindex.html",
        priority = PrioritizedService.PRIORITY_DATABASE,
        appliesTo = {"collection", "database"}
)
public class CreatePrimaryQueryIndexChange extends CouchbaseChange {
    private String bucketName;
    private String collectionName;
    private Boolean deferred;
    private String indexName;
    private Integer numReplicas;
    private String scopeName;

    @Override
    public String getConfirmationMessage() {
        return String.format("Primary query index \"%s\" has been created", getIndexName());
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        if (isNotBlank(getBucketName())) {
            return new SqlStatement[]{new CreatePrimaryQueryIndexStatement(getBucketName(), createPrimaryQueryIndexOptions())};
        }
        return SqlStatement.EMPTY_SQL_STATEMENT;
    }
    private CreatePrimaryQueryIndexOptions createPrimaryQueryIndexOptions() {
        return CreatePrimaryQueryIndexOptions
                .createPrimaryQueryIndexOptions()
                .indexName(getIndexName())
                .collectionName(getCollectionName())
                .scopeName(getScopeName())
                .deferred(getDeferred())
                .numReplicas(getNumReplicas());
    }
}