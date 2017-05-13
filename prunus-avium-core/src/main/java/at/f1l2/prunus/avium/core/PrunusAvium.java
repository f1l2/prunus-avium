package at.f1l2.prunus.avium.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.f1l2.prunus.avium.core.model.Program;
import at.f1l2.prunus.avium.core.model.ProgramBuilder;
import at.f1l2.prunus.avium.core.player.RemotePlayer;
import at.f1l2.prunus.avium.core.player.RemotePlayerAccess;
import at.f1l2.prunus.avium.core.player.configuration.Oe1RemotePlayerConfig;

public class PrunusAvium {

	public static final String APPLICATION_NAME = "Prunus Avium";

	private static Logger logger = LoggerFactory.getLogger(PrunusAvium.class);

	public static void main(String[] args) throws IOException {

		// Bootstrap.main(args);

		// try {
		// PrunusAvium.createIndex();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		////
		//
		logger.info("Prunus Avium started");

		RemotePlayerAccess rpa = new RemotePlayer(new Oe1RemotePlayerConfig(), new ProgramBuilder());

		List<Program> programs = rpa.requestCurrentPlaylist();

		logger.info("# programs: {}", programs.size());

		List<Program> dimensionen = programs.stream().sorted(Comparator.comparing(Program::getBegin))
				.filter(item -> item.getTitle().startsWith("Dimensionen")).collect(Collectors.toList());

		logger.info("File storage: {}", System.getProperty("java.io.tmpdir"));

		rpa.downloadPrograms(dimensionen, new File(System.getProperty("java.io.tmpdir")));

		logger.info("Program has finished");
	}

	private static void createIndex() throws IOException, ParseException {
		StandardAnalyzer analyzer = new StandardAnalyzer();

		Directory index = new NIOFSDirectory(
				Paths.get(FilenameUtils.concat(System.getProperty("java.io.tmpdir"), "index")));

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(index, config);
		addDoc(w, "Lucene in Action", "193398817");
		addDoc(w, "Lucene for Dummies", "55320055Z");
		addDoc(w, "Managing Gigabytes", "55063554A");
		addDoc(w, "The Art of Computer Science", "9900333X");
		w.close();

		String querystr = "lucene";
		Query q = new QueryParser("title", analyzer).parse(querystr);

		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
		}
	}

	private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new StringField("isbn", isbn, Field.Store.YES));

		w.addDocument(doc);
	}

}
