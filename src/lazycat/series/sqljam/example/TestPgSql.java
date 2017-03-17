package lazycat.series.sqljam.example;

import java.util.List;

import lazycat.series.logger.LazyLogger;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ConfigurationInitializer;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.SessionEngine;
import lazycat.series.sqljam.example.model.Article;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.update.Batch;

/**
 * TestPgSql
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TestPgSql {

	static {
		System.setProperty("lazycat.logger.level.lazycat.series.sqljam", "DEBUG");
		System.setProperty("lazycat.logger.level.lazycat.series.jdbc", "DEBUG");
	}

	private static final LazyLogger logger = LoggerFactory.getLogger(TestPgSql.class);

	public static void main1(String[] args) {
		String driverClassName = "org.postgresql.Driver";
		// String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
						// configuration.mapClass(Article.class);
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);
	}

	public static void main2(String[] args) {

		String driverClassName = "org.postgresql.Driver";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Batch batch = session.insert(Article.class).batch(10);

		Article article = new Article();
		article.setText("这是一篇美声文章");
		article.setTitle("论美声");
		article.setContent("<div>这是一篇美声文章</div>");
		article.setUrl("http://www.meisheng.com");
		article.setAuthor("Peter");
		article.setScore(50.0f);
		batch.push(article);

		article = new Article();
		article.setText("这是一篇美食文章");
		article.setTitle("论美食");
		article.setContent("<div>这是一篇美食文章</div>");
		article.setUrl("http://www.meishi.com");
		article.setAuthor("Jim");
		article.setScore(180.0f);
		batch.push(article);

		article = new Article();
		article.setText("这是一篇音乐文章");
		article.setTitle("论音乐");
		article.setContent("<div>这是一篇音乐文章</div>");
		article.setUrl("http://www.music.com");
		article.setAuthor("Jack");
		article.setScore(10.0f);
		batch.push(article);

		article = new Article();
		article.setText("这是一篇舞蹈文章");
		article.setTitle("论舞蹈");
		article.setContent("<div>这是一篇舞蹈文章</div>");
		article.setUrl("http://www.smith.com");
		article.setAuthor("Smith");
		article.setScore(68.0f);
		batch.push(article);

		int rows = batch.execute();
		session.commit();
		System.out.println("Rows: " + rows);
		System.out.println("Over");
	}

	public static void main3(String[] args) {

		String driverClassName = "org.postgresql.Driver";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Article bean = session.get(19, Article.class);
		System.out.println(bean);
		System.out.println("Over");
	}

	public static void main5(String[] args) {
		String driverClassName = "org.postgresql.Driver";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", null, false,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Article article = session.get(14, Article.class);
		article.setTitle("论变声");
		session.update(article);
		session.commit();

		article = session.get(14, Article.class);
		System.out.println(article);
		session.close();
		System.out.println("TestMain.main5()");
	}

	public static void main4(String[] args) {

		String driverClassName = "org.postgresql.Driver";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Query query = session.query(Article.class);
		query.filter(Expressions.gte("score", 100));
		query.desc("author");
		System.out.println(query.rows());
		List<Article> list = query.list();
		for (Article a : list) {
			System.out.println(a);
		}
		// int rows = query.insert(ArticleCopy.class);
		// System.out.println(">>> "+rows);
		// System.out.println("--------------------------------");
		// int rows = query.createAs("tb_new_article");
		// System.out.println(">>> "+rows);
		// list = query.list();
		// for (Article a : list) {
		// System.out.println(a);
		// }
		// List<String> dlist = query.scalar("lastModified", String.class);
		// System.out.println(dlist);
		System.out.println("Over");
	}

	public static void main(String[] args) {
		main3(args);
	}

}
