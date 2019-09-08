# PG - jsonb

Java library easy querying [jsonb](https://www.postgresql.org/docs/9.4/datatype-json.html)  PostgreSQL type of data.

## Getting Started

This lib can be used either along with Hibernate Criterion API ([Restrictions](https://docs.jboss.org/hibernate/core/3.3/api/org/hibernate/criterion/Restrictions.html)) or with native JDBC queries.

#### Hibernate CriteriaBuilder API example

In order to use pg-jsonb lib with CriteriaBuilder API it is required to use one from the provided Dialects that could be found at `com.vzornic.pgjson.hibernate.dialect`.

For example:
`hibernate.dialect=com.vzornic.pgjson.hibernate.dialect.PostgresJsonSQL94Dialect`



```
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<User> cr = cb.createQuery(User.class);
    
    Root<User> root = cr.from(User.class); // Get root object
    // Instantiate JSONRootImpl with RootImpl. This is required in order to build json properties
    JSONRootImpl<User> jsonRoot = new JSONRootImpl<User>((RootImpl<User>) root);
		
    cr.where(cb.equal(jsonRoot.get("json_column", "jsondata.parent[0].name"), "Jane Doe"));

```

As you can see in example, json expressions are build using `JSONRootImpl<X>`. 

Methods `get(String attributeName, String jsonPath, Class<Y> type)` and `get(String attributeName, String jsonPath)` will build a json paths. If type is not specified, String is used by default.

API is using javascript notation to build paths, i.e:

```
jsondata.parent.nested.object.array[1]
```

Criteria Builder API supports nesting to the 10th level.

#### Hibernate Criterion API example

```
  Criteria criteria = ... // Get Criteria object
  criteria.add(Restrictions.eq("name", "John Doe"); // Regular hibernate query
  criteria.add(JSONBRestrictions.eq("json_column", "jsondata.parent[0].name", "Jane Doe"); // Json restriction  
  criteria.list(); // Obtain result
```

#### JDBC example

```
  Statement stmt = conn.createStatement();
  SimpleJsonCondition jsonCondition = new SimpleJsonCondition(new JsonProperty("json_column", "sondata.parent[0].name"), new ParametrizedValue("Jane Doe"), "=");
  String sql = new StringBuilder()
      .append("SELECT * FROM user WHERE ")
      .append(jsonCondition.toSqlString())
      .toString();
```

### Installing

Add to your pom.xml

```
<dependency>
    <groupId>com.vzornic.pgjsonb</groupId>
    <artifactId>pgjsonb</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Running the tests

In order to run tests, there are few prerequirements needed:

- postgress up on localhost:5432
- Database `pgjsonb`
- User test/test with full access to `pgjsonb` database
- Execute seed.sql from test/resources


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Feel free to enhance this libary and open PR. I'll review PRs as soon as possible.

## Authors

* **Vedad Zornic** - *Initial work* - [vzornic](https://github.com/vzornic)

See also the list of [contributors](https://github.com/vzornic/pg-jsonb/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/vzornic/pg-jsonb/blob/master/LICENSE) file for details

