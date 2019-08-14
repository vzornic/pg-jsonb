package com.vzornic.pgjson.hibernate.expressions;

import com.vzornic.pgjson.User;
import com.vzornic.pgjson.hibernate.restrictions.JSONBRestrictions;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.vzornic.pgjson.TestUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class JSONBExpressionTest {

	@Test
	public void testFirstLevelEqual() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.eq("data", "email", "johnwoodard@xleen.com"));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(5, result.get(0).getId().intValue());
	}

	@Test
	public void testNestedEquals() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.eq("data", "parents.father.name", "Brooke Holt"));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testMatchInArray() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.eq("data", "related[0].name", "Carpenter Erickson"));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testMultiMatch() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.eq("data", "age", "30"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iEq("data", "parents.father.name", "brOOke hOlT"));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testNotEquals() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.ne("data", "parents.father.name", "Brooke Holt"));

		List<User> result = criteria.list();
		assertEquals(16, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertFalse(ids.contains(3));
	}

	@Test
	public void testINotEquals() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iNe("data", "parents.father.name", "BrooKE holt"));

		List<User> result = criteria.list();
		assertEquals(16, result.size());

		List<Integer> ids = result.stream().map(User::getId).collect(Collectors.toList());

		assertFalse(ids.contains(3));
	}

	@Test
	public void testLikeAnywhere() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.like("data", "parents.father.name", "ooke ", MatchMode.ANYWHERE));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testLikeEnd() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.like("data", "parents.father.name", "Holt", MatchMode.END));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testLikeStart() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.like("data", "parents.father.name", "Brooke", MatchMode.START));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testLikeDoesntMatch() {
		Session session = TestUtils.getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.like("data", "parents.father.name", "Brooke", MatchMode.END));
		List<User> result = criteria.list();
		assertEquals(0, result.size());

		criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.like("data", "parents.father.name", "Holt", MatchMode.START));
		result = criteria.list();
		assertEquals(0, result.size());

		criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.like("data", "parents.father.name", "XxX", MatchMode.ANYWHERE));
		result = criteria.list();
		assertEquals(0, result.size());
	}

	@Test
	public void testILikeAnywhere() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iLike("data", "parents.father.name", "oOKe ", MatchMode.ANYWHERE));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testILikeEnd() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iLike("data", "parents.father.name", "HOLT", MatchMode.END));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testILikeStart() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iLike("data", "parents.father.name", "BroOKe", MatchMode.START));

		List<User> result = criteria.list();
		assertEquals(1, result.size());
		assertEquals(3, result.get(0).getId().intValue());
	}

	@Test
	public void testILikeDoesntMatch() {
		Session session = TestUtils.getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iLike("data", "parents.father.name", "BroOKe", MatchMode.END));
		List<User> result = criteria.list();
		assertEquals(0, result.size());

		criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iLike("data", "parents.father.name", "HoLT", MatchMode.START));
		result = criteria.list();
		assertEquals(0, result.size());

		criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.iLike("data", "parents.father.name", "xxx", MatchMode.ANYWHERE));
		result = criteria.list();
		assertEquals(0, result.size());
	}

	@Test
	public void testGreaterThan() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.gt("data", "age", "30"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.ge("data", "age", "30"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.lt("data", "age", "26"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.le("data", "age", "26"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.between("data", "age", "25", "29"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.notBetween("data", "age", "18", "31"));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.in("data", "age", Arrays.asList(30, 22, 21), CastType.BIGINT));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.notIn("data", "age", Arrays.asList(30, 22, 21, 32, 29, 28), CastType.BIGINT));

		List<User> result = criteria.list();
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

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.isNull("data", "nonExistingField"));

		List<User> result = criteria.list();
		assertEquals(17, result.size());
	}

	@Test
	public void testNotNull() {
		Session session = TestUtils.getSession();

		Criteria criteria = session.createCriteria(User.class);
		criteria.add(JSONBRestrictions.isNotNull("data", "age"));

		List<User> result = criteria.list();
		assertEquals(17, result.size());
	}
}
