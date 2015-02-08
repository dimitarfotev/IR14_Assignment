package WebCrawl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndexer {
	private IndexWriter writer;

	public LuceneIndexer(String indexDirectoryPath) throws IOException {
		
		//to create the indexer we need to create first an indexWriterConfig with an Analyzer. 
		//Than we use the indexing directory and the indexWriterconfig to create the indexWriter
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory directory = FSDirectory.open(new File("indexDir"));
		IndexWriterConfig indexWrtiterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
		indexWrtiterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND); 

		writer = new IndexWriter(directory, indexWrtiterConfig);
	}


	public void deleteIndexes(String indexDirectoryPath) throws IOException {
		
		//deletes all created index
		writer.deleteAll();
	}

	public void closeWriter(String indexDirectoryPath) throws IOException {
		
		//used to stop the indexing after it is done
		writer.close();
	}
	public int checkForExistingIndex(String indexDirectoryPath)
			throws IOException {
		int numberOfDocs = writer.numDocs();
		return numberOfDocs;
	}

	public void indexWebPage( String Title, String Body, String URL)
			throws IOException {

		{

			Document doc = new Document();

			try {
				
					doc.add(new TextField("title", Title, Field.Store.YES));
					doc.add(new TextField("body", Body, Field.Store.YES));
					doc.add(new TextField("url", URL, Field.Store.YES));
					


				writer.addDocument(doc);
				writer.commit(); // Commits all changes to the index
		
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
