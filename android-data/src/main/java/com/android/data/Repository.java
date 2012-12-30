package com.android.data;

import org.ektorp.*;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.HttpClient;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.util.Documents;

import java.io.InputStream;
import java.util.*;

public class Repository<T extends Document> extends CouchDbRepositorySupport<T> {

    private static CouchDbConnector connector = new InMemoryConnector();

    protected Repository(Class<T> type) {
        super(type, connector);
    }

    static class InMemoryConnector implements CouchDbConnector {
        Map<String, Object> data = new HashMap<String, Object>();

        private String getNewId() {
            return UUID.randomUUID().toString();
        }

        @Override
        public void create(String id, Object o) {
            Documents.setId(o, id);
            data.put(id, o);
        }

        @Override
        public void create(Object o) {
            create(getNewId(), o);
        }

        @Override
        public void update(Object o) {

        }

        @Override
        public String delete(Object o) {
            return null;
        }

        @Override
        public String delete(String id, String revision) {
            return null;
        }

        @Override
        public PurgeResult purge(Map<String, List<String>> revisionsToPurge) {
            return null;
        }

        @Override
        public <T> T get(Class<T> c, String id) {
            return (T) data.get(id);
        }

        @Override
        public <T> T get(Class<T> c, String id, Options options) {
            return null;
        }

        @Override
        public <T> T find(Class<T> c, String id) {
            return null;
        }

        @Override
        public <T> T find(Class<T> c, String id, Options options) {
            return null;
        }

        @Override
        public <T> T get(Class<T> c, String id, String rev) {
            return null;
        }

        @Override
        public <T> T getWithConflicts(Class<T> c, String id) {
            return null;
        }

        @Override
        public boolean contains(String id) {
            return false;
        }

        @Override
        public InputStream getAsStream(String id) {
            return null;
        }

        @Override
        public InputStream getAsStream(String id, String rev) {
            return null;
        }

        @Override
        public InputStream getAsStream(String id, Options options) {
            return null;
        }

        @Override
        public List<Revision> getRevisions(String id) {
            return null;
        }

        @Override
        public AttachmentInputStream getAttachment(String id, String attachmentId) {
            return null;
        }

        @Override
        public AttachmentInputStream getAttachment(String id, String attachmentId, String revision) {
            return null;
        }

        @Override
        public String createAttachment(String docId, AttachmentInputStream data) {
            return null;
        }

        @Override
        public String createAttachment(String docId, String revision, AttachmentInputStream data) {
            return null;
        }

        @Override
        public String deleteAttachment(String docId, String revision, String attachmentId) {
            return null;
        }

        @Override
        public List<String> getAllDocIds() {
            return null;
        }

        @Override
        public <T> List<T> queryView(ViewQuery query, Class<T> type) {
            return null;
        }

        @Override
        public <T> Page<T> queryForPage(ViewQuery query, PageRequest pr, Class<T> type) {
            return null;
        }

        @Override
        public ViewResult queryView(ViewQuery query) {
            return null;
        }

        @Override
        public StreamingViewResult queryForStreamingView(ViewQuery query) {
            return null;
        }

        @Override
        public InputStream queryForStream(ViewQuery query) {
            return null;
        }

        @Override
        public void createDatabaseIfNotExists() {

        }

        @Override
        public String getDatabaseName() {
            return null;
        }

        @Override
        public String path() {
            return null;
        }

        @Override
        public HttpClient getConnection() {
            return null;
        }

        @Override
        public DbInfo getDbInfo() {
            return null;
        }

        @Override
        public DesignDocInfo getDesignDocInfo(String designDocId) {
            return null;
        }

        @Override
        public void compact() {

        }

        @Override
        public void compactViews(String designDocumentId) {

        }

        @Override
        public void cleanupViews() {

        }

        @Override
        public int getRevisionLimit() {
            return 0;
        }

        @Override
        public void setRevisionLimit(int limit) {

        }

        @Override
        public ReplicationStatus replicateFrom(String source) {
            return null;
        }

        @Override
        public ReplicationStatus replicateFrom(String source, Collection<String> docIds) {
            return null;
        }

        @Override
        public ReplicationStatus replicateTo(String target) {
            return null;
        }

        @Override
        public ReplicationStatus replicateTo(String target, Collection<String> docIds) {
            return null;
        }

        @Override
        public void addToBulkBuffer(Object o) {

        }

        @Override
        public List<DocumentOperationResult> flushBulkBuffer() {
            return null;
        }

        @Override
        public void clearBulkBuffer() {

        }

        @Override
        public List<DocumentOperationResult> executeBulk(InputStream inputStream) {
            return null;
        }

        @Override
        public List<DocumentOperationResult> executeAllOrNothing(InputStream inputStream) {
            return null;
        }

        @Override
        public List<DocumentOperationResult> executeBulk(Collection<?> objects) {
            return null;
        }

        @Override
        public List<DocumentOperationResult> executeAllOrNothing(Collection<?> objects) {
            return null;
        }

        @Override
        public List<DocumentChange> changes(ChangesCommand cmd) {
            return null;
        }

        @Override
        public StreamingChangesResult changesAsStream(ChangesCommand cmd) {
            return null;
        }

        @Override
        public ChangesFeed changesFeed(ChangesCommand cmd) {
            return null;
        }

        @Override
        public String callUpdateHandler(String designDocID, String function, String docId) {
            return null;
        }

        @Override
        public String callUpdateHandler(String designDocID, String function, String docId, Map<String, String> params) {
            return null;
        }

        @Override
        public <T> T callUpdateHandler(UpdateHandlerRequest req, Class<T> c) {
            return null;
        }

        @Override
        public String callUpdateHandler(UpdateHandlerRequest req) {
            return null;
        }

        @Override
        public void ensureFullCommit() {

        }
    }
}