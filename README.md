# PG - jsonb

Java library easy querying [jsonb](https://www.postgresql.org/docs/9.4/datatype-json.html)  PostgreSQL type of data.

## Getting Started

This lib can be used either along with Hibernate Criterion API ([Restrictions](https://docs.jboss.org/hibernate/core/3.3/api/org/hibernate/criterion/Restrictions.html)) or with native JDBC queries.

#### Hibernate example

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

//TODO


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

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

