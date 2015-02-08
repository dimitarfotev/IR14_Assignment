package WebCrawl;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.queryparser.classic.ParseException;
import org.jsoup.nodes.Document;

public class MainClassCrawler {

	static Document doc;
	public static Crawler crawler;

	public static void main(String[] args) throws InterruptedException,
			IOException {
		
		System.out.println("Enter the url that you want to crawl:");
		Scanner in = new Scanner(System.in);
		
		String url= in.next();
		LuceneIndexer index = new LuceneIndexer("indexDir");

		int depth = 0;
		

		System.out.println("Enter the search max depth:");
		int maxDepth = in.nextInt();
		
		ArrayList<String> visited = new ArrayList<String>();
		ArrayList<String> toVisit = new ArrayList<String>();
		ArrayList<String> visiting = new ArrayList<String>();
		

		int numberOfIndex = index.checkForExistingIndex("indexDir");
		if (numberOfIndex > 0)
		{
			System.out.println("There is an existing index. Type y to create a new index. Type anything else if you don't want");
		String toIndex= in.next();
			if(toIndex.equals("y"))
				
			{		index.deleteIndexes("indexDir");
				visiting.add(url);
				 crawler.Crawl (visited, visiting, toVisit,  depth, maxDepth, index);
			}
			
			
		}
		
		if(numberOfIndex==0)
		{
			visiting.add(url);
			 crawler.Crawl (visited, visiting, toVisit,  depth, maxDepth, index);
		}

		index.closeWriter("indexDir");

		String s = "."; // random value, string must not be null here
		while (!s.equals("*stop")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			LuceneSearch currentSearch;
			try {
				currentSearch = new LuceneSearch("indexDir");

				System.out.println("Enter the search query:");
				System.out.println("Type *stop to stop.");
				
				s = br.readLine();
				try {
					if (!s.equals("*stop") && !s.isEmpty()) {
						currentSearch.searchByQuery(s);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
