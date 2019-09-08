package com.vzornic.pgjson.hibernate.dialect;

import org.hibernate.dialect.PostgreSQL93Dialect;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StringType;

/**
 * Hibernate dialect to support json queries with HQL
 */
public class PostgresJsonSQL94Dialect extends PostgreSQL94Dialect {

	public PostgresJsonSQL94Dialect() {
		registerFunction("internal_json_text_1", new SQLFunctionTemplate(StringType.INSTANCE, "?1->>?2"));
		registerFunction("internal_json_text_2", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->>?3"));
		registerFunction("internal_json_text_3", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->>?4"));
		registerFunction("internal_json_text_4", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->?4->>?5"));
		registerFunction("internal_json_text_5", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->?4->?5->>?6"));
		registerFunction("internal_json_text_6", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->?4->?5->?6->>?7"));
		registerFunction("internal_json_text_7", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->?4->?5->?6->?7->>?8"));
		registerFunction("internal_json_text_8", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->?4->?5->?6->?7->?8->>?9"));
		registerFunction("internal_json_text_9", new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2->?3->?4->?5->?6->?7->?8->?9->>?10"));
	}
}
