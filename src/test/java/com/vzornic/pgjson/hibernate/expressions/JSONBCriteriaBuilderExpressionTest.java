package com.vzornic.pgjson.hibernate.expressions;

import com.vzornic.pgjson.TestUtils;
import com.vzornic.pgjson.User;
import com.vzornic.pgjson.hibernate.criteria.attribute.JSONRootImpl;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.internal.path.RootImpl;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class JSONBCriteriaBuilderExpressionTest {

	@Test
	public void testFirstLevelEqual() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");

		cr.where(cb.equal(jsonRoot.get( "email"), "johnwoodard@xleen.com"));
		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(5, result.get(0).getId().intValue());
	}

	@Test
	public void testExpressionsEquals() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");

		cr.where(cb.equal(jsonRoot.get( "email"), jsonRoot.get( "email")));
		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(17, result.size());
	}

	@Test
	public void testNestedEquals() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");

		cr.where(cb.equal(jsonRoot.get( "parents.father.name"), "Brooke Holt"));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testMatchInArray() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");

		cr.where(cb.equal(jsonRoot.get( "related[0].name"), "Carpenter Erickson"));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testMultiMatch() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		
		cr.where(cb.equal(jsonRoot.get( "age", Integer.class), 30));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(4, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertEquals(1, result.get(0).getId().intValue());
		assertEquals(4, result.get(1).getId().intValue());
		assertEquals(7, result.get(2).getId().intValue());
		assertEquals(15, result.get(3).getId().intValue());
	}

	@Test
	public void testIEquals() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.equal(cb.lower(jsonRoot.get( "parents.father.name")), "brOOke hOlT".toLowerCase()));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testNotEquals() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.notEqual(jsonRoot.get( "parents.father.name"), "Brooke Holt"));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(16, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertFalse(ids.contains(3));
	}

	@Test
	public void testINotEquals() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.notEqual((cb.lower(jsonRoot.get( "parents.father.name"))), "BrooKE holt".toLowerCase()));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(16, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertFalse(ids.contains(3));
	}

	@Test
	public void testLikeAnywhere() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(jsonRoot.get( "parents.father.name"), MatchMode.ANYWHERE.toMatchString("ooke ")));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testLikeEnd() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(jsonRoot.get( "parents.father.name"), MatchMode.END.toMatchString("Holt")));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testLikeStart() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(jsonRoot.get( "parents.father.name"), MatchMode.START.toMatchString("Brooke")));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testLikeDoesntMatch() {
		Session session = TestUtils.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(jsonRoot.get( "parents.father.name"), MatchMode.END.toMatchString("Brooke")));
		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(0, result.size());

		query = session.createQuery(cr);
		cr.where(cb.like(jsonRoot.get( "parents.father.name"), MatchMode.START.toMatchString("Holt")));
		result = query.list();
		assertEquals(0, result.size());

		query = session.createQuery(cr);
		cr.where(cb.like(jsonRoot.get( "parents.father.name"), MatchMode.ANYWHERE.toMatchString("XxX")));
		result = query.list();
		assertEquals(0, result.size());
	}

	@Test
	public void testILikeAnywhere() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(cb.lower(jsonRoot.get( "parents.father.name")), MatchMode.ANYWHERE.toMatchString("oOKe ").toLowerCase()));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testILikeEnd() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(cb.lower(jsonRoot.get( "parents.father.name")), MatchMode.END.toMatchString("HOLT").toLowerCase()));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testILikeStart() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(cb.lower(jsonRoot.get( "parents.father.name")), MatchMode.START.toMatchString("BroOKe").toLowerCase()));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testILikeDoesntMatch() {
		Session session = TestUtils.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.like(cb.lower(jsonRoot.get( "parents.father.name")), MatchMode.END.toMatchString("BroOKe")));
		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(0, result.size());

		query = session.createQuery(cr);
		cr.where(cb.like(cb.lower(jsonRoot.get( "parents.father.name")), MatchMode.START.toMatchString("HoLT")));
		result = query.list();
		assertEquals(0, result.size());

		query = session.createQuery(cr);
		cr.where(cb.like(cb.lower(jsonRoot.get( "parents.father.name")), MatchMode.ANYWHERE.toMatchString("xxx")));
		result = query.list();
		assertEquals(0, result.size());
	}

	@Test
	public void testGreaterThan() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.gt(jsonRoot.get( "age", Integer.class), 30));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(4, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(2));
		assertTrue(ids.contains(3));
		assertTrue(ids.contains(13));
		assertTrue(ids.contains(14));
	}

	@Test
	public void testGreaterOrEqualsThan() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.ge(jsonRoot.get( "age", Integer.class), 30));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(8, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(2));
		assertTrue(ids.contains(3));
		assertTrue(ids.contains(13));
		assertTrue(ids.contains(14));
		assertTrue(ids.contains(1));
		assertTrue(ids.contains(4));
		assertTrue(ids.contains(15));
		assertTrue(ids.contains(7));
	}

	@Test
	public void testLessThan() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.lt(jsonRoot.get( "age", Integer.class), 26));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(4, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(5));
		assertTrue(ids.contains(12));
		assertTrue(ids.contains(16));
		assertTrue(ids.contains(17));
	}

	@Test
	public void testLessOrEqualsThan() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.le(jsonRoot.get( "age", Integer.class), 26));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(5, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(5));
		assertTrue(ids.contains(12));
		assertTrue(ids.contains(16));
		assertTrue(ids.contains(17));
		assertTrue(ids.contains(9));
	}

	@Test
	public void testBetween() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.between(jsonRoot.get( "age", Integer.class), 25, 29));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(5, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(6));
		assertTrue(ids.contains(8));
		assertTrue(ids.contains(9));
		assertTrue(ids.contains(10));
		assertTrue(ids.contains(11));
	}

	@Test
	public void testNotBetween() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.not(cb.between(jsonRoot.get( "age", Integer.class), 18, 31)));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(4, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(3));
		assertTrue(ids.contains(13));
		assertTrue(ids.contains(2));
		assertTrue(ids.contains(14));
	}

	@Test
	public void testIn() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(jsonRoot.get( "age", Integer.class).in( Arrays.asList(30, 22, 21)));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(7, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(1));
		assertTrue(ids.contains(4));
		assertTrue(ids.contains(5));
		assertTrue(ids.contains(7));
		assertTrue(ids.contains(15));
		assertTrue(ids.contains(16));
		assertTrue(ids.contains(12));
	}

	@Test
	public void testNotIn() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.not(jsonRoot.get( "age", Integer.class).in(Arrays.asList(30, 22, 21, 32, 29, 28))));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(4, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertTrue(ids.contains(3));
		assertTrue(ids.contains(9));
		assertTrue(ids.contains(13));
		assertTrue(ids.contains(17));
	}

	@Test
	public void testNull() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.isNull(jsonRoot.get( "nonExistingField")));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(17, result.size());
	}

	@Test
	public void testNotNull() {
		Session session = TestUtils.getSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);
		Root<User> root = cr.from(User.class);
		JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root, "data");
		cr.where(cb.isNotNull(jsonRoot.get( "age", Integer.class)));

		Query<User> query = session.createQuery(cr);

		List<User> result = query.list();
		assertEquals(17, result.size());
	}
}
