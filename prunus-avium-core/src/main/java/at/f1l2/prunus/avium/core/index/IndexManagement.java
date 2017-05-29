package at.f1l2.prunus.avium.core.index;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

	public IndexManagement(IndexConfiguration configuration) {
		this.configuration = configuration;
		this.analyzer = new StandardAnalyzer();
		try {
			this.index = new NIOFSDirectory(configuration.getIndexPath());
		} catch (IOException e) {
			logger.error("Unexpected error occurred during initiating index.");
			throw new AviumCoreException("Unexpected error occurred during initiating index.", e);
		}
	}

	public void cleanIndex() {
		try {
			FileUtils.cleanDirectory(configuration.getIndexPath().toFile());
		} catch (IOException e) {
			logger.error("Unexpected error occurred during cleaning index.");
			throw new AviumCoreException("Unexpected error occurred during cleaning index.", e);
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
			logger.error("Unexpected error occurred during building index.");
			throw new AviumCoreException("Unexpected error occurred during building index.", e);
		}
	}

	private void addProgram(IndexWriter indexWriter, Program program) throws IOException {
		Document document = new Document();
		if (Objects.nonNull(program.getUuid())) {
			document.add(new TextField("id", program.getUuid().toString(), Field.Store.YES));
		}
		if (Objects.nonNull(program.getTitle())) {
			document.add(new TextField("title", program.getTitle(), Field.Store.YES));
		}

		if (Objects.nonNull(program.getSubtitle())) {
			document.add(new StringField("subtitle", program.getSubtitle(), Field.Store.YES));
		}
		if (Objects.nonNull(program.getHref())) {
			document.add(new StringField("href", program.getHref(), Field.Store.YES));
		}

		if (Objects.nonNull(program.getBegin())) {
			document.add(new StringField("begin", toString(program.getBegin()), Field.Store.YES));
		}

		if (Objects.nonNull(program.getEnd())) {
			document.add(new StringField("end", toString(program.getEnd()), Field.Store.YES));
		}

		indexWriter.addDocument(document);
	}

	public List<Program> searchByID(String queryStr) {

		try {
			final Query query = new QueryParser("id", analyzer).parse(queryStr);
			return executeQuery(query);
		} catch (ParseException e) {
			logger.error("Unexpected error occurred while parsing query.");
		}

		return new ArrayList<>();

	}

	public List<Program> searchByTitle(String queryStr) {
		try {
			final Query query = new QueryParser("title", analyzer).parse(queryStr);
			return executeQuery(query);
		} catch (ParseException e) {
			logger.error("Unexpected error occurred while parsing query.");
		}
		return new ArrayList<>();
	}

	private List<Program> executeQuery(Query query) {

		final List<Program> result = new ArrayList<>();

		try {
			int hitsPerPage = 10;
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
			logger.error("Unexpected error occurred while searching the index.", e);
			System.out.println(e);
		}

		return result;
	}

	private Program convertToProgram(Document d) {

		Program program = new Program();
		program.setHref(d.get("href"));
		program.setTitle(d.get("title"));
		program.setSubtitle(d.get("subtitle"));
		program.setBegin(toLocalDateTime(d.get("begin")));
		program.setEnd(toLocalDateTime(d.get("end")));
		program.setUuid(UUID.fromString(d.get("id")));

		return program;
	}

	private LocalDateTime toLocalDateTime(String ms) {

		System.out.println(ms);

		if (ms == null) {
			return null;
		}
		LocalDateTime localDateTime = Instant.ofEpochSecond(Long.valueOf(ms)).atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		System.out.println(localDateTime.toString());
		return localDateTime;
	}

	private String toString(LocalDateTime localDateTime) {

		if (Objects.isNull(localDateTime)) {
			return "";
		}

		return Long.toString(localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond());
	}
}
