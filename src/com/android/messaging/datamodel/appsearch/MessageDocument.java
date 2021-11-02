package com.android.messaging.datamodel.appsearch;

import androidx.annotation.NonNull;
import androidx.appsearch.annotation.Document;
import androidx.appsearch.app.AppSearchSchema;

import java.util.Objects;

@Document
public class MessageDocument {

    // Required field for a document class. All documents MUST have a namespace.
    @Document.Namespace
    private final String namespace;

    // Required field for a document class. All documents MUST have an Id.
    @Document.Id
    private final String id;

    // Optional field for a document class, used to set the score of the
    // document. If this is not included in a document class, the score is set
    // to a default of 0.
    @Document.Score
    private final int score;

    // Optional field for a document class, used to index a message's text for this
    // document class.
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    private final String text;

    // Optional field for a document class, used to index a message's sender for this
    // document class.
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    private final String sender;

    MessageDocument(@NonNull String id, @NonNull String namespace, int score, @NonNull String text, @NonNull String sender) {
        this.id = Objects.requireNonNull(id);
        this.namespace = Objects.requireNonNull(namespace);
        this.score = score;
        this.text = Objects.requireNonNull(text);
        this.sender = Objects.requireNonNull(sender);
    }

    @NonNull
    public String getNamespace() {
        return namespace;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    public String getSender() {
        return sender;
    }
}

