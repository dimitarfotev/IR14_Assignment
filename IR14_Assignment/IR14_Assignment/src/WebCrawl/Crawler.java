package WebCrawl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	static Document doc;

	public static void Crawl(ArrayList<String> visited,
			ArrayList<String> visiting, ArrayList<String> toVisit,
			 int depth, int maxDepth,
			LuceneIndexer index) throws IOException {

		System.out.println("Depth=" +depth);

		for (String link : visiting) {

			if (visited.contains(link)) // if the link as visited before
			{
				// visiting.remove(link);
			} else {

				try {
					doc = Jsoup.connect(link).timeout(12340).get();

					String textCode = doc.toString();

					
						

							Elements newLinks = doc.select("a[href]");

							if (textCode.contains("<code>"))// if the web page
															// has a <code> tag,
															// this means the
															// page has a code
							// in stack overflow the html tag, used where a code
							// is written, is <code> ....</code>
							{
								URI uri;
								try {
									uri = new URI(link);  // we normalize the URI and than add it like visited
								
								
								String normalizedUrl= uri.toString();
								
								// index the page if she has a code
								visited.add(normalizedUrl);
								

								String Title = doc.title();
								String Body = doc.body().text();
								String URL = link;
								System.out.println("Indexing: " + link);
								index.indexWebPage(Title, Body, URL);
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
							}
							 else {
								System.out
										.println("There is no programming code on the webpage "
												+ link);
							}
								for (Element x : newLinks) {
									toVisit.add(x.attr("abs:href"));
								}
								

						
					}
				 catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					System.out.println("Illegal link. Please form your url like z.B. : http://www.cs.uni-magdeburg.de" );
					
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					

				} 
				catch (HttpStatusException e) {
					// TODO Auto-generated catch block
					System.out.println("Unable to connect. Http Status Exception " + link);

				}catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
					System.out.println("Timout Exception: Cannot connect to:" + link );
				}

				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		if (depth < maxDepth) {
			depth++;
			visiting.clear();
			visiting.addAll(toVisit);
			toVisit.clear();

			Crawl(visited, visiting, toVisit,  depth, maxDepth, index);

		}

	}
}
