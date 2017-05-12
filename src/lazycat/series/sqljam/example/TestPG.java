package lazycat.series.sqljam.example;

import java.math.BigInteger;

import lazycat.series.collection.RandomStringUtils;
import lazycat.series.lang.RandomUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ConfigurationInitializer;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.SessionEngine;
import lazycat.series.sqljam.SessionOptions;
import lazycat.series.sqljam.example.model.Article;
import lazycat.series.sqljam.update.Batch;

public class TestPG {

	static {
		System.setProperty("lazycat.logger.level.lazycat.series.sqljam", "DEBUG");
		System.setProperty("lazycat.logger.level.lazycat.series.jdbc", "DEBUG");
	}

	private static Session openSession() {
		String driverClassName = "org.postgresql.Driver";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", new ConfigurationInitializer() {
			public void configure(Configuration configuration, SessionOptions sessionOptions) {
				configuration.scanPackages("lazycat.series.sqljam.example.model");
			}
		});
		Session session = sessionEngine.openSession();
		System.out.println(session);
		return session;
	}

	private static void batchInsert2() {
		Session session = openSession();
		Batch batch = session.insert(Article.class).batch();
		batch.setFlushSize(100);
		int start = 6000;
		int N = 1000;
		for (int i = start; i < start + N; i++) {
			Article article = new Article();
			article.setAuthor(RandomStringUtils.randomString(10, true, true, true));
			article.setContent("<div>Content_" + i + "</div>");
			article.setTitle("Title_" + i);
			article.setText("Text_" + i);
			article.setUrl("http://www." + RandomStringUtils.randomString(5, true, false,false) + ".com/a/b/c.html");
			article.setScore(RandomUtils.randomFloat(1, 100));
			article.setFollowCount(BigInteger.valueOf(RandomUtils.randomLong(10, 10000)));
			batch.push(article);
		}
		batch.execute();
		session.commit();
		session.close();
		System.out.println("TestMySql.batchInsert2()");
	}

	private static void batchInsert() {
		Session session = openSession();
		int N = 1000;
		for (int i = 0; i < N; i++) {
			Article article = new Article();
			article.setAuthor(RandomStringUtils.randomString(6, true, true, true));
			article.setContent("<div>Content_" + i + "</div>");
			article.setTitle("Title_" + i);
			article.setText("Text_" + i);
			article.setUrl("http://www." + RandomStringUtils.randomString(5, true, true, true) + ".com/a/b/c.html");
			article.setScore(RandomUtils.randomFloat(1, 100));
			article.setFollowCount(BigInteger.valueOf(RandomUtils.randomLong(10, 10000)));
			session.save(article);
		}
		session.commit();
		session.close();
		System.out.println("TestMySql.batchInsert()");
	}

	public static void main(String[] args) {
		batchInsert2();
	}

}
