package WebCrawl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class LuceneSearch {
	private IndexReader reader;

	public LuceneSearch(String indexDirectoryPath) throws IOException {

		
		//opens the directory with the indexes, where we are going to search
		
		reader = DirectoryReader.open(FSDirectory.open(new File("indexDir")));

	}

	public void searchByQuery(String s) throws IOException, ParseException {

		IndexSearcher searcher = new IndexSearcher(reader);

		Analyzer analyzer = new StandardAnalyzer();

		Query queryParser = new MultiFieldQueryParser(new String[] { "body",
				"title", "url" }, analyzer).parse(s);

		TopDocs topDocs = searcher.search(queryParser, 10);

		ScoreDoc[] hits = topDocs.scoreDocs;
		
		int docsToPrint=10;
		if(topDocs.totalHits<=10)
		{
			docsToPrint=topDocs.totalHits;
		}
		
		if (topDocs.totalHits == 0) {
			System.out.println("There are no found webpages, relevant to this search.");
		}
		else 
		{
			for (int i = 0; i < docsToPrint; i++) {

				ScoreDoc dr = hits[i];
				int docId = dr.doc;
				Document d = searcher.doc(docId);
				System.out.println("rank:" + (i + 1) + ". " +  "  "+ "title: " + d.get("title") + "url:" + d.get("url")
						+ "  " + "relevance score=" + hits[i].score);
			}

		}
	}
}
