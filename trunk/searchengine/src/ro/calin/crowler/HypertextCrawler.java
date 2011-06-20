package ro.calin.crowler;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import ro.calin.LogHelper;

/**
 * Crawler. Primeste o lista de url-uri. Proceseaza raspunsul primit de la
 * conexiune si trece la urmatorul-url. Foloseste o coada cu prioritate si mai
 * multe thread-uri.
 * 
 * Numarul de thread-uri si depth-ul pana la care continua sa extaga url-uri din
 * documente sunt configurabile.
 * 
 * @author Calin 
 * 
 * TODO: ordonarea cozii cu prioritate
 */
public class HypertextCrawler implements Runnable {
	static LogHelper log = LogHelper.getInstance(HypertextCrawler.class);
	
	private static class CrawlerURL implements Comparable<CrawlerURL> {
		private String url;
		private int depth;

		private CrawlerURL(String url, int depth) {
			this.url = url;
			this.depth = depth;
		}

		@Override
		public int compareTo(CrawlerURL o) {
			if (depth < o.depth)
				return -1;
			else if (depth == o.depth) {
				// order by host name
				return url.compareTo(o.url);
			}

			return 1;
		}

		@Override
		public String toString() {
			return "CURL[url=" + url + ";depth=" + depth + "]";
		}
	}

	private class CrawlerWorkerThread implements Runnable {
		private boolean working = false;
		private Thread myThread = null;

		private void crawl(CrawlerURL curl) {

			try {

				ParsedHypertextDocument document = parser.parse(curl.url, curl.depth < maxDepth - 1 ? true : false);

				if (curl.depth < maxDepth - 1) {
					// put the links in the url queue
					// the parser must take care of extracting the valid
					// links
					List<String> links = document.getValidParsedLinks();

					if (links != null) {

						// put each link in the queue
						synchronized (urlQueue) {
							for (String link : links) {

								CrawlerURL url = new CrawlerURL(link, curl.depth + 1);
								urlQueue.offer(url);
							}

							// notify the other threads
							try {
								urlQueue.notifyAll();
							} catch (IllegalMonitorStateException e) {
								// no way of getting here
							}
						}
					}
				}

				processor.handleParsedDocument(document);

			} catch (HypertextDocumentParserException e) {
				if(log.isDebugEnabled())
					log.debug("Parse exception on " + curl + ": ", e);
			}
		}

		private boolean checkJobStatusNotifyAndReport() {
			// last worker closes the factory gates
			// queue is empty and nobody is working means job is done
			if (urlQueue.isEmpty()) {
				boolean done = true;
				for (CrawlerWorkerThread worker : workers) {
					synchronized (worker) {
						if (worker.working == true) {
							done = false;
							break;
						}
					}
				}

				if (done) {
					if(log.isDebugEnabled())
						log.debug("Job is done. Notify all threads.");
					
					// the job is done; announce everyone
					synchronized (jobDone) {
						jobDone = done;
					}

					for (CrawlerWorkerThread worker : workers) {
						if (worker.myThread != null && worker != this)
							worker.myThread.interrupt();
					}
					
					
					//notify the processor that job is finished
					processor.handleFinishedJob();
				}
			}

			return jobDone;
		}

		@Override
		public void run() {
			try {
				myThread = Thread.currentThread();

				if(log.isDebugEnabled())
					log.debug("Worker is starting...");

				outer: while (true) {
					synchronized (jobDone) {
						if (jobDone)
							break;
					}

					CrawlerURL someResource;
					synchronized (urlQueue) {
						while (urlQueue.isEmpty()) {
							try {
								urlQueue.wait(4000);
							} catch (InterruptedException e) {
								// should get here if interupted by the last
								// working thread
							}

							synchronized (jobDone) {
								if (jobDone)
									break outer;
							}
						}

						someResource = urlQueue.poll();
					}

					synchronized (this) {
						working = true;
					}

					crawl(someResource);

					synchronized (this) {
						working = false;
					}

					if (checkJobStatusNotifyAndReport())
						break;

					// give piss a chance
					Thread.yield();
				}
			} catch (Throwable e) {
				// this little worker had to go home early
				synchronized (workers) {
					workers.remove(this);
				}

				// notify the threads
				checkJobStatusNotifyAndReport();
				
				if(log.isDebugEnabled())
					log.debug("Worker is had to go home early: ", e);
			} finally {

				if(log.isDebugEnabled())
					log.debug("Worker, over and out.");
			}
		}
	}

	public static int MAX_NUMBER_OF_THREADS = 100;
	private static int DEFAULT_NUMBER_OF_THREADS = 50;
	public static int MIN_NUMBER_OF_THREADS = 1;

	public static int MAX_MAX_DEPTH = 20;
	private static int DEFAULT_MAX_DEPTH = 3;
	public static int MIN_MAX_DEPTH = 1;

	private int numberOfThreads;
	private int maxDepth;
	private HypertextCrawlerProcessor processor;

	private PriorityQueue<CrawlerURL> urlQueue;
	private Boolean jobDone;

	private HashSet<CrawlerWorkerThread> workers;

	/**
	 * Mapeaza pattern-uri de content-type la parser-e de documente
	 */
	private HypertextDocumentParser parser;

	{
		urlQueue = new PriorityQueue<CrawlerURL>();
		workers = new HashSet<CrawlerWorkerThread>();
		jobDone = false;
	}

	public HypertextCrawler(HypertextCrawlerProcessor processor, HypertextDocumentParser parser, String... startUrls)
			throws IllegalArgumentException, MalformedURLException {
		this(processor, parser, DEFAULT_NUMBER_OF_THREADS, DEFAULT_MAX_DEPTH, startUrls);
	}

	public HypertextCrawler(HypertextCrawlerProcessor processor, HypertextDocumentParser parser, int numberOfThreads, int maxDepth,
			String... startUrls) throws IllegalArgumentException {

		if (startUrls == null || startUrls.length == 0 || processor == null || parser == null)
			throw new IllegalArgumentException("No null or empty arguments!");

		if (numberOfThreads > MAX_NUMBER_OF_THREADS || numberOfThreads < MIN_NUMBER_OF_THREADS)
			throw new IllegalArgumentException("Illegal number of threads!");

		if (maxDepth > MAX_MAX_DEPTH || maxDepth < MIN_MAX_DEPTH)
			throw new IllegalArgumentException("Illegal depth!");

		for (String url : startUrls) {
			if (url == null)
				throw new IllegalArgumentException("No null urls!");

			// start with depth 0
			urlQueue.offer(new CrawlerURL(url, 0));
		}

		this.processor = processor;
		this.parser = parser;
		this.numberOfThreads = numberOfThreads;
		this.maxDepth = maxDepth;
	}

	private void crawl() {
		
		String crawler = "Crawler " + this.hashCode();
		Thread.currentThread().setName(crawler);
		
		if(log.isDebugEnabled())
			log.debug("Starting " + crawler + "...");
			
		for (int i = 0; i < numberOfThreads; i++) {
			synchronized (jobDone) {
				if (jobDone)
					break;
			}

			CrawlerWorkerThread worker = new CrawlerWorkerThread();

			synchronized (worker) {
				workers.add(worker);
			}

			if (i < numberOfThreads - 1) {
				// start the thread
				Thread t = new Thread(worker);
				t.setName(crawler + " worker " + i);
				t.start();
			} else {
				if(log.isDebugEnabled())
					log.debug(crawler + " started.");
				
				worker.run();
			}
		}
	}

	public void stop() {
		
		if(log.isDebugEnabled())
			log.debug("Force crawler stop...");
		
		synchronized (jobDone) {
			jobDone = true;
		}

		for (CrawlerWorkerThread worker : workers) {
			if (worker.myThread != null)
				worker.myThread.interrupt();
		}		
	}

	@Override
	public void run() {
		crawl();
	}
}
