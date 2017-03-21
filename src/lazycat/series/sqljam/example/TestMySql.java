package lazycat.series.sqljam.example;

import java.math.BigDecimal;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ConfigurationInitializer;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.SessionEngine;
import lazycat.series.sqljam.example.model.Article;
import lazycat.series.sqljam.example.model.ArticleCopy;
import lazycat.series.sqljam.example.model.Order;
import lazycat.series.sqljam.example.model.User;
import lazycat.series.sqljam.expression.Column;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.expression.Fields;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.update.Batch;
import lazycat.series.sqljam.update.Delete;

public class TestMySql {

	static {
		System.setProperty("lazycat.logger.level.lazycat.series.sqljam", "DEBUG");
		System.setProperty("lazycat.logger.level.lazycat.series.jdbc", "DEBUG");
	}

	public static void main1(String[] args) {
		String driverClassName = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "root", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
						// configuration.mapClass(Article.class);
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);
	}
	
	public static void test12() {
		Session session = openSession();
		Article article = new Article();
		article.setText("这是一篇军事文章");
		article.setTitle("论军事");
		article.setContent("<div>这是一篇军事文章</div>");
		article.setUrl("http://www.jerry.com");
		article.setAuthor("Jerry");
		article.setScore(58.0f);
		session.save(article);
		session.commit();
		System.out.println(article.getId() + "\t" + article.getAuthor());
	}

	public static void test3() {
		Session session = openSession();
		Article article = new Article();
		article.setText("这是一篇本地化文章");
		article.setTitle("论本地化");
		article.setContent("<div>这是一篇本地化文章</div>");
		article.setUrl("http://www.meisheng.com");
		article.setAuthor("Scaner");
		article.setScore(50.0f);
		session.save(article);
		session.commit();
		System.out.println(article);
	}

	public static void main2(String[] args) {

		String driverClassName = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "root", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
						// configuration.mapClass(Article.class);
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Batch batch = session.insert(Article.class).batch(10);

		Article article = new Article();
		article.setText("这是一篇体育文章");
		article.setTitle("论体育");
		article.setContent("<div>这是一篇体育文章</div>");
		article.setUrl("http://www.meisheng.com");
		article.setAuthor("Merry");
		article.setScore(50.0f);
		batch.push(article);

		article = new Article();
		article.setText("这是一篇古典文章");
		article.setTitle("论古典");
		article.setContent("<div>这是一篇古典文章</div>");
		article.setUrl("http://www.meishi.com");
		article.setAuthor("Tiger");
		article.setScore(180.0f);
		batch.push(article);

		article = new Article();
		article.setText("这是一篇计算机文章");
		article.setTitle("论计算机");
		article.setContent("<div>这是一篇计算机文章</div>");
		article.setUrl("http://www.music.com");
		article.setAuthor("Scott");
		article.setScore(10.0f);
		batch.push(article);

		article = new Article();
		article.setText("这是一篇舞蹈文章");
		article.setTitle("论舞蹈");
		article.setContent("<div>这是一篇舞蹈文章</div>");
		article.setUrl("http://www.smith.com");
		article.setAuthor("Jay");
		article.setScore(68.0f);
		batch.push(article);

		int rows = batch.execute();
		session.commit();
		System.out.println("Rows: " + rows);
		System.out.println("Over");
	}

	public static void main3(String[] args) {
		Session session = openSession();
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

	public static void main4(String[] args) {

		String driverClassName = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "root", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
						// configuration.mapClass(Article.class);
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Query query = session.query(Article.class);
		int rows = query.into(ArticleCopy.class);
		System.out.println(">>> " + rows);
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

	public static void main5(String[] args) {

		String driverClassName = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "root", "12345678", null, true,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
						// configuration.mapClass(Article.class);
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);

		Query query = session.query(Article.class);
		int rows = query.createAs("tb_new_article");
		System.out.println(">>> " + rows);
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

	public static void main6(String[] args) {
		Session session = openSession();
		// Query query = session.query(Article.class);
		// int i = query.all().filter(Expressions.ne("author",
		// "Scan")).desc("score").rows();
		// System.out.println(i);
		// List<Article> list =
		// query.cleanColumns().column("text").filter(Expressions.ne("author",
		// "Scan")).desc("score").limit(3).list();
		// for (Article a : list) {
		// System.out.println(a);
		// }
		List<Article> list = session.query(Article.class).group("title").max("score", "score")
				.column(Fields.function("left", new Column("title"), 2), "title").list();
		for (Article a : list) {
			System.out.println(a);
		}
		System.out.println("Over");
	}
  
	public static void main7(String[] args) {
		Session session = openSession();
		Delete delete = session.delete(Article.class);
		delete.innerJoin(ArticleCopy.class, "a", Expressions.eqProperty("author", "a.author"));
		int effected = delete.execute();
		System.out.println("Eff:" + effected);
		session.close();
		System.out.println("Over");
	}

	public static void main8(String[] args) {
		Session session = openSession();
		Delete delete = session.delete(ArticleCopy.class);
		delete.filter(Expressions.eq("author", "Jim"));
		int effected = delete.execute();
		System.out.println("Eff:" + effected);
		session.close();
		System.out.println("Over");
	}

	public static void main9() {
		Session session = openSession();
		int effected = session.update(Article.class).filter(Expressions.eq("author", "Jay")).set("title", "论公园").execute();
		session.commit();
		System.out.println("Eff:" + effected);
	}

	public static void test10() {
		Session session = openSession();
		User user = new User();
		user.setUsername("resin");
		user.setPassword("111111");
		session.save(user);
		session.commit();
		System.out.println(user.getId());
	}

	public static void test2() {
		Session session = openSession();
		User user = session.get(1, User.class);
		int effected = session.delete(user, true);
		session.commit();
		System.out.println("Effected: " + effected);
		session.close();
	}

	public static void test11() {
		Session session = openSession();
		Order order = new Order();
		order.setPrice(new BigDecimal("10.801"));
		order.setUid(6);
		session.save(order);

		order = new Order();
		order.setPrice(new BigDecimal("100.209"));
		order.setUid(6);
		session.save(order);

		order = new Order();
		order.setPrice(new BigDecimal("201.678"));
		order.setUid(6);
		session.save(order);
		
		 
		session.commit();
	}

	private static Session openSession() {
		String driverClassName = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/demo";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "root", "12345678", null, false,
				new ConfigurationInitializer() {
					public void configure(Configuration configuration) {
						configuration.scanPackage("lazycat.series.sqljam.example.model");
						// configuration.mapClass(Article.class);
					}
				});
		Session session = sessionEngine.openSession();
		System.out.println(session);
		return session;
	}

	public static void main(String[] args) {
		test11();
	}

}
