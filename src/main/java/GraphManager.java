package main.java;

import main.java.models.Tag;
import org.neo4j.driver.v1.*;
import main.java.models.Book;
import java.util.*;

public class GraphManager implements AutoCloseable
{
	private final Driver driver;
	public GraphManager(String uri, String user, String password ) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}
	
	@Override
	public void close() {
		driver.close();
	}
	
	private static int countBooks(Transaction tx, int userId, boolean read) {
		StatementResult res;
		if(!read) res = tx.run("MATCH (:Book) RETURN COUNT(*)");
		else res = tx.run("MATCH (u:User {user_id:\""+userId+"\"}) -[:RATED]->(:Book) RETURN COUNT(*)");
		
		int val = -1;
		
		if(res.hasNext()) {
			Record rec = res.next();
			val = rec.get(0).asInt();
		}
		return val;
	}
	
	public int getNumBooks(int userId, boolean read) {
		try (Session session = driver.session()) {
			return session.readTransaction( new TransactionWork<Integer>() {
				@Override
				public Integer execute(Transaction tx) {
					return countBooks(tx, userId, read);
				}
			} );
		}
	}
	
	private static int countWish(Transaction tx, int userId) {
		StatementResult res = tx.run("MATCH (u:User {user_id:\""+userId+"\"})-[:TO_READ]->(:Book) RETURN COUNT(*)");
		int val = -1;
		
		if(res.hasNext()) {
			Record rec = res.next();
			val = rec.get(0).asInt();
		}
		return val;
	}
	
	public int getNumWish(int userId) {
		try (Session session = driver.session()) {
			return session.readTransaction( new TransactionWork<Integer>() {
				@Override
				public Integer execute(Transaction tx) {
					return countWish(tx, userId);
				}
			} );
		}
	}

	private static List<Book> browseBooks(Transaction tx, int userId, boolean rated, int offset)
	{
		List<Book> tmpBooks = new ArrayList<>();
		String query;
		if(rated) query = "MATCH (p:User {user_id:\""+userId+"\"})-[r:RATED]->(b:Book) RETURN b.book_id, b.original_title, b.authors, r.rating  SKIP "+offset+" LIMIT 15";
		else query = "MATCH (p:User {user_id:\""+userId+"\"}),(b:Book) WHERE NOT (p)-[:RATED]->(b) RETURN b.book_id, b.original_title, b.authors, b.average_rating ORDER BY b.average_rating DESC SKIP "+offset+" LIMIT 15;";
		
		System.out.println("Query: "+query);
		StatementResult result = tx.run(query);
		while ( result.hasNext() )
		{
			Record tmpRes = result.next();
			Book b = new Book();
			b.setBookId(Integer.parseInt(tmpRes.get(0).asString()));
			b.setTitle(tmpRes.get(1).asString());
			b.setAuthor(tmpRes.get(2).asString());
			Double avg;
			try { //dataset is corrupted, some avgs are double, others are strings -- not our fault
				avg = tmpRes.get(3).asDouble();
			} catch(Exception e) {
				avg = Double.parseDouble(tmpRes.get(3).asString());
			}
			b.setAvgRating(avg);
			tmpBooks.add(b);
		}
		return tmpBooks;
	}
	
	public List<Book> getBooks(int userId, boolean rated, int offset) //rated indica se voglio la lista di libri letti&votati oppure no
	{
		try (Session session = driver.session()) {
			return session.readTransaction( new TransactionWork<List<Book>>() {
				@Override
				public List<Book> execute(Transaction tx) {
					return browseBooks(tx, userId, rated, offset);
				}
			} );
		}
	}
	
	private static List<Book> findBooks(Transaction tx, int criteria, String text)
	{
		List<Book> tmpBooks = new ArrayList<>();
		String query = "";
		
		System.out.println("Query: " + query);
		StatementResult result = tx.run(query);
		while (result.hasNext())
		{
			Record tmpRes = result.next();
			Book b = new Book();
			b.setBookId(Integer.parseInt(tmpRes.get(0).asString()));
			b.setTitle(tmpRes.get(1).asString());
			b.setAuthor(tmpRes.get(2).asString());
			Double avg;
			try { //dataset is corrupted, some avgs are double, others are strings -- not our fault
				avg = tmpRes.get(3).asDouble();
			} catch(Exception e) {
				avg = Double.parseDouble(tmpRes.get(3).asString());
			}
			b.setAvgRating(avg);
			tmpBooks.add(b);
		}
		return tmpBooks;
	}
	
	public List<Book> searchBooks(int criteria, String text) //cirteria=0 for title, 1 for author
	{
		try (Session session = driver.session()) {
			return session.readTransaction( new TransactionWork<List<Book>>() {
				@Override
				public List<Book> execute(Transaction tx) {
					return findBooks(tx, criteria, text);
				}
			} );
		}
	}
	
	private static List<Book> browseWishList(Transaction tx, int userId, int offset)
	{
		List<Book> tmpBooks = new ArrayList<>();
		String query = "MATCH (p:User)-[r:TO_READ]->(b:Book) WHERE p.user_id=\""+userId+"\" RETURN DISTINCT b.book_id, b.original_title, b.authors, b.average_rating ORDER BY b.average_rating DESC SKIP "+offset+" LIMIT 10";
		
		StatementResult result = tx.run(query);
		while ( result.hasNext() ) {
			Record tmpRes = result.next();
			Book b = new Book();
			b.setBookId(Integer.parseInt(tmpRes.get(0).asString()));
			b.setTitle(tmpRes.get(1).asString());
			b.setAuthor(tmpRes.get(2).asString());
			b.setAvgRating(Double.parseDouble(tmpRes.get(3).asString()));
			tmpBooks.add(b);
		}
		return tmpBooks;
	}
	
	public List<Book> getWishList(int userId, int offset)
	{
		try (Session session = driver.session()) {
			return session.readTransaction( new TransactionWork<List<Book>>() {
				@Override
				public List<Book> execute(Transaction tx) {
					return browseWishList(tx, userId, offset);
				}
			} );
		}
	}
	
	private static void insertWish(Transaction tx, int userId, int bookId) {
		String query = "MATCH (u:User) WHERE u.user_id=\""+userId+"\" MATCH (b:Book) WHERE b.book_id=\""+bookId+"\" CREATE (u)-[:TO_READ]->(b)";
		System.out.println("Query: "+query);
		tx.run(query);
	}
	
	public void addWish(int userId, int bookId) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute(Transaction tx)
				{
					insertWish(tx, userId, bookId);
					return true;
				}
			});
		}
	}
	
	private static void deleteWish(Transaction tx, int userId, int bookId) {
		String query = "MATCH (u:User {user_id:\""+userId+"\"})-[r:TO_READ]->(b:Book {book_id:\""+bookId+"\"}) DELETE r";
		System.out.println("Query: "+query);
		tx.run(query);
	}
	
	public void removeWish(int userId, int bookId) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute(Transaction tx)
				{
					deleteWish(tx, userId, bookId);
					return true;
				}
			});
		}
	}

	private static void insertTag(Transaction tx, int bookId, String tag) {
		String query = "MATCH (b:Book {book_id:\""+bookId+"\"}) MERGE (t:Tag {tag_name:\""+tag+"\"}) MERGE (b)-[r:TAGGED_AS]->(t) ON CREATE SET r.count=1 ON MATCH SET r.count=r.count+1";
		System.out.println("Query: "+query);
		tx.run(query);
	}
	
	public void addTag(int bookId, String tag) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute(Transaction tx)
				{
					insertTag(tx, bookId, tag);
					return true;
				}
			});
		}
	}

	private static void insertRating(Transaction tx, int bookId, int userId, int rating) {
		String query = "MATCH (u:User {user_id:\""+userId+"\"}),(b:Book {book_id:\""+bookId+"\"}) MERGE (u)-[r:RATED]->(b) SET r.rating="+rating;
		System.out.println("Query: "+query);
		tx.run(query);
		
		//computes the new avg rating
		String query2 = "MATCH ()-[r:RATED]->(b:Book {book_id:\""+bookId+"\"}) WITH b, AVG(r.rating) AS new_rating SET b.average_rating=toString(ROUND(new_rating*100)/100)";
		tx.run(query2);
		
		//removes book from WishList, in case it was there
		tx.run("MATCH (u:User {user_id:\""+userId+"\"})-[r:TO_READ]->(b:Book {book_id:\""+bookId+"\"}) DELETE r");
	}
	
	public void addRating(int bookId, int userId, int rating) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute(Transaction tx)
				{
					insertRating(tx, bookId, userId, rating);
					return true;
				}
			});
		}
	}
	
	private static void deleteBook(Transaction tx, int bookId) {
		String query = "MATCH (b:Book {book_id:\""+bookId+"\"}) DETACH DELETE b";
		System.out.println("Query: "+query);
		tx.run(query);
	}
	
	public void removeBook(int bookId) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute(Transaction tx)
				{
					deleteBook(tx, bookId);
					return true;
				}
			});
		}
	}
	
	private static void createBook(Transaction tx, String title, String authors) {
		
		//computes new ID as MAX_ID +1
		String query = "MATCH (b:Book) RETURN COUNT(*)+2"; //"MATCH (b:Book) RETURN toInteger(MAX(b.book_id))+2";
		StatementResult res1 = tx.run(query);
		
		if(res1.hasNext())
		{
			Record r = res1.next();
			String bookId = Integer.toString(r.get(0).asInt());
			String query2 = "CREATE (n:Book {title:\""+title+"\", original_title:\""+title+"\", authors:\""+authors+"\", average_rating:0.0, book_id:\""+bookId+"\"})";
			System.out.println("Query: "+query2);
			tx.run(query2);
		}
	}
	
	public void addBook(String title, String authors) {
		try (Session session = driver.session()) {
			session.writeTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute(Transaction tx)
				{
					createBook(tx, title, authors);
					return true;
				}
			});
		}
	}

	private static Integer countTotWish(Transaction tx, int bookId) {
		String query = "MATCH (:User)-[r:TO_READ]->(b:Book) WHERE b.book_id = '" + bookId + "' RETURN count(r) as totWish";
		System.out.println("Query: "+query);
		StatementResult result = tx.run(query);
		Record tmpRes = result.next();
		Integer totWish = Integer.parseInt(tmpRes.get(0).toString());

		return totWish;
	}

	public Integer getTotWish(int bookId) {
		try (Session session = driver.session()) {
			return session.writeTransaction( new TransactionWork<Integer>() {
				@Override
				public Integer execute(Transaction tx) {
					return countTotWish(tx, bookId);
				}
			} );
		}
	}

	private static List<Tag> matchTags(Transaction tx, int bookId) {
		List<Tag> tag_list = new ArrayList<>();
		String query = "MATCH (b:Book)-[r:TAGGED_AS]->(t:Tag) WHERE b.book_id = '" + bookId + "' RETURN t.tag_id, t.tag_name";
		System.out.println("Query: "+query);

		StatementResult result = tx.run(query);
		while(result.hasNext()) {
			Record tmpRes = result.next();
			Tag t = new Tag();
			t.setTag_id(Integer.parseInt(tmpRes.get(0).asString()));
			t.setTag_name(tmpRes.get(1).asString());
			tag_list.add(t);
		}

		return tag_list;
	}

	public List<Tag> getTags(int bookId) {
		try (Session session = driver.session()) {
			return session.writeTransaction( new TransactionWork<List<Tag>>() {
				@Override
				public List<Tag> execute(Transaction tx) {
					return matchTags(tx, bookId);
				}
			} );
		}
	}

	private static Integer countTotTag(Transaction tx, int bookId) {
		String query = "MATCH (b:Book)-[r:TAGGED_AS]->(t:Tag) WHERE b.book_id = '" + bookId + "' RETURN count(r) as totTags";
		System.out.println("Query: "+query);
		StatementResult result2 = tx.run(query);
		Record tmpRes = result2.next();
		Integer totTags = Integer.parseInt(tmpRes.get(0).toString());

		return totTags;
	}

	public Integer getTotTag(int bookId) {
		try (Session session = driver.session()) {
			return session.writeTransaction( new TransactionWork<Integer>() {
				@Override
				public Integer execute(Transaction tx) {
					return countTotTag(tx, bookId);
				}
			} );
		}
	}

	private static List<Book> browseSuggestion(Transaction tx, int userId)
	{
		List<Book> tmpBooks = new ArrayList<>();
		String query = "MATCH (u:User {user_id:\""+userId+"\"})-[r:RATED]->(b:Book)-[ta:TAGGED_AS]->(t:Tag)<-[ta2:TAGGED_AS]-(b2:Book) WHERE r.rating >= 4 AND b <> b2 WITH u,b2,collect(DISTINCT t) as commonTag WHERE size(commonTag) >= 3 AND NOT EXISTS ((u)-[:TO_READ]->(b2)) RETURN DISTINCT b2.book_id, b2.original_title, b2.authors, b2.average_rating, size(commonTag) ORDER BY size(commonTag) DESC, b2.average_rating DESC LIMIT 5";

		String query2 = "MATCH (u:User {user_id:\""+userId+"\"})-[r:RATED ]->(b:Book)<-[r2:RATED]-(u2:User)-[r3:RATED]->(b2:Book) WHERE r.rating>=4 AND r2.rating>=4 AND r3.rating>=4 AND b <> b2 AND u <> u2 AND NOT EXISTS ((u)-[:RATED]->(b2)) AND NOT EXISTS ((u)-[:TO_READ]->(b2)) RETURN DISTINCT b2.book_id, b2.original_title, b2.authors, b2.average_rating ORDER BY b2.average_rating DESC LIMIT 5";
		
		System.out.println("Query: "+query+"\n");
		System.out.println("Query: "+query2+"\n");
		
		StatementResult result = tx.run(query);
		StatementResult result2 = tx.run(query2);
		
		while ( result.hasNext() )
		{
			Record tmpRes = result.next();
			Book b = new Book();
			b.setBookId(Integer.parseInt(tmpRes.get(0).asString()));
			b.setTitle(tmpRes.get(1).asString());
			b.setAuthor(tmpRes.get(2).asString());
			Double avg;
			try { //dataset is corrupted, some avgs are double, others are strings -- not our fault
				avg = tmpRes.get(3).asDouble();
			} catch(Exception e) {
				avg = Double.parseDouble(tmpRes.get(3).asString());
			}
			b.setAvgRating(avg);
			tmpBooks.add(b);
		}

		while ( result2.hasNext() )
		{
			Record tmpRes = result2.next();
			Book b = new Book();
			b.setBookId(Integer.parseInt(tmpRes.get(0).asString()));
			b.setTitle(tmpRes.get(1).asString());
			b.setAuthor(tmpRes.get(2).asString());
			Double avg;
			try { //dataset is corrupted, some avgs are double, others are strings -- not our fault
				avg = tmpRes.get(3).asDouble();
			} catch(Exception e) {
				avg = Double.parseDouble(tmpRes.get(3).asString());
			}
			b.setAvgRating(avg);
			if(!tmpBooks.contains(b)) tmpBooks.add(b);
		}
		return tmpBooks;
	}

	public List<Book> getSuggestion(int userId) //rated indica se voglio le suggestion per rating o per wishlist
	{
		try (Session session = driver.session()) {
			return session.readTransaction( new TransactionWork<List<Book>>() {
				@Override
				public List<Book> execute(Transaction tx) {
					return browseSuggestion(tx, userId);
				}
			} );
		}
	}
}