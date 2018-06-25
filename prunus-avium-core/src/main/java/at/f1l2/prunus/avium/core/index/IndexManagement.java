package at.f1l2.prunus.avium.core.index;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.f1l2.prunus.avium.core.exception.AviumCoreException;
import at.f1l2.prunus.avium.core.index.configuration.IndexConfiguration;
import at.f1l2.prunus.avium.core.model.Program;

public class IndexManagement {

	private Logger logger = LoggerFactory.getLogger(IndexManagement.class);

	private StandardAnalyzer analyzer = new StandardAnalyzer();

	private IndexConfiguration configuration;

	private NIOFSDirectory index;

	private static final String EXCEPTION_MESSAGE_INDEX = "Unexpected error occurred during initiating index.";

	private static final String EXCEPTION_MESSAGE_INDEX_CLEAN = "Unexpected error occurred during cleaning index.";

	private static final String EXCEPTION_MESSAGE_INDEX_BUILDING = "Unexpected error occurred during building index.";

	private static final String EXCEPTION_MESSAGE_QUERY_PARSE = "Unexpected error occurred while parsing query.";

	private static final String EXCEPTION_MESSAGE_INDEX_SEARCH = "Unexpected error occurred while searching index.";

	public IndexManagement(IndexConfiguration configuration) {
		this.configuration = configuration;
		this.analyzer = new StandardAnalyzer();
		try {
			this.index = new NIOFSDirectory(configuration.getIndexPath());
		} catch (IOException e) {
			logger.error(EXCEPTION_MESSAGE_INDEX);
			throw new AviumCoreException(EXCEPTION_MESSAGE_INDEX, e);
		}
	}

	public void cleanIndex() {
		try {
			FileUtils.cleanDirectory(configuration.getIndexPath().toFile());
		} catch (IOException e) {
			logger.error(EXCEPTION_MESSAGE_INDEX_CLEAN);
			throw new AviumCoreException(EXCEPTION_MESSAGE_INDEX_CLEAN, e);
		}
	}

	public void buildIndex(List<Program> programs) {
		try {
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter w = new IndexWriter(index, config);

			for (Program program : programs) {
				addProgram(w, program);
			}
			w.close();
		} catch (Exception e) {
			logger.error(EXCEPTION_MESSAGE_INDEX_BUILDING);
			throw new AviumCoreException(EXCEPTION_MESSAGE_INDEX_BUILDING, e);
		}
	}

	public List<Program> search(String queryStr) {
		try {
			final Query query1 = new QueryParser(IndexField.PROGRAM_TITLE.getField(), analyzer).parse(queryStr + "*");
			final Query query2 = new QueryParser(IndexField.TITLE.getField(), analyzer).parse(queryStr + "*");
			final Query query3 = new QueryParser(IndexField.ID.getField(), analyzer).parse(queryStr + "*");
			final Query query4 = new QueryParser(IndexField.BEGIN.getField(), analyzer).parse(queryStr);

			BooleanQuery booleanQuery = new BooleanQuery.Builder()
					//
					.add(query1, BooleanClause.Occur.SHOULD)
					//
					.add(query2, BooleanClause.Occur.SHOULD)
					//
					.add(query3, BooleanClause.Occur.SHOULD)
					//
					.add(query4, BooleanClause.Occur.SHOULD)
					//
					.build();

			return executeQuery(booleanQuery);
		} catch (ParseException e) {
			logger.error(EXCEPTION_MESSAGE_QUERY_PARSE, e);
		}
		return Collections.emptyList();
	}

	private void addProgram(IndexWriter indexWriter, Program program) throws IOException {
		Document document = new Document();
		addStringFieldNullSafe(document, IndexField.ID, program.getUuid().toString());
		addTextFieldNullSafe(document, IndexField.TITLE, program.getTitle());
		addTextFieldNullSafe(document, IndexField.PROGRAM_TITLE, program.getProgramTitle());
		addStringFieldNullSafe(document, IndexField.SUB_TITLE, program.getSubtitle());
		addStringFieldNullSafe(document, IndexField.HREF, program.getHref());
		addStringFieldNullSafeDate(document, IndexField.BEGIN, program.getBegin());
		addStringFieldNullSafeDate(document, IndexField.END, program.getEnd());

		indexWriter.addDocument(document);
	}

	private void addStringFieldNullSafeDate(final Document document, IndexField name, LocalDateTime value)
			throws UnsupportedEncodingException {
		if (Objects.nonNull(value)) {
			String valueToString = DateTools.dateToString(
					Date.from(ZonedDateTime.of(value, ZoneId.systemDefault()).toInstant()), Resolution.MINUTE);
			addTextFieldNullSafe(document, name, valueToString);
		}
	}

	private void addStringFieldNullSafe(final Document document, IndexField name, String value)
			throws UnsupportedEncodingException {
		if (Objects.nonNull(value)) {
			document.add(new StringField(name.getField(), value, Field.Store.YES));
		}
	}

	private void addTextFieldNullSafe(final Document document, IndexField name, String value)
			throws UnsupportedEncodingException {
		if (Objects.nonNull(value)) {
			document.add(new TextField(name.getField(), value, Field.Store.YES));
		}
	}

	private List<Program> executeQuery(Query query) {

		final List<Program> result = new ArrayList<>();

		try {
			int hitsPerPage = 100;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(query, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				result.add(convertToProgram(d));
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_MESSAGE_INDEX_SEARCH, e);
		}

		return result;
	}

	private Program convertToProgram(Document d) throws UnsupportedEncodingException {
		final Program program = new Program();
		program.setHref(d.get(IndexField.HREF.getField()));
		program.setTitle(new String(d.get(IndexField.TITLE.getField()).getBytes(), "UTF-8"));
		program.setSubtitle(d.get(IndexField.SUB_TITLE.getField()));
		program.setBegin(toLocalDateTime(d.get(IndexField.BEGIN.getField())));
		program.setEnd(toLocalDateTime(d.get(IndexField.END.getField())));
		program.setUuid(UUID.fromString(d.get(IndexField.ID.getField())));
		program.setProgramTitle(d.get(IndexField.PROGRAM_TITLE.getField()));
		return program;
	}

	private LocalDateTime toLocalDateTime(String ms) {
		if (ms == null) {
			return null;
		}
		Long ms1 = 0l;
		try {
			ms1 = DateTools.stringToTime(ms) / 1000;
		} catch (Exception e) {

		}
		return Instant.ofEpochSecond(ms1).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
