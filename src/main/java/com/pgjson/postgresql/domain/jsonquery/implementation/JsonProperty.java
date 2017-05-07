package com.pgjson.postgresql.domain.jsonquery.implementation;

import com.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.pgjson.postgresql.domain.jsonquery.model.JsonPath;
import com.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * Class used to create column part of sql query in json fragments.
 * Useful for selecting single json field inside json column.
 * This class does not take care of invalid properties.
 *
 * @author vedadzornic
 */
public class JsonProperty implements JsonQueryFragment {
	private static String ARRAY_REMOVE_REGEX = "[\\.|\\[|\\]]";
	private static final Pattern ARRAY_PATTERN = Pattern.compile("\\[(\\d+)\\]");

	private JsonIteratorMode iteratorMode = JsonIteratorMode.DEFAUTL;
	private final String column;
	private List<JsonPath> paths = new ArrayList<>();
	private CastType castType;
	private boolean asJson = false;
	private boolean ignoreValues = false;
	private boolean randomKeyValues = false;
	private List<ParametrizedValue> parametrizedValues = new ArrayList<>();

	/**
	 * Creates new JsonProperty.
	 *
	 * @param column
	 * @param path
	 */
	public JsonProperty(String column, String path) {
		this.column = column;
		initializePaths(path);
	}

	/**
	 * Creates new JsonProperty
	 *
	 * @param column
	 * @param paths
	 */
	public JsonProperty(String column, List<JsonPath> paths) {
		this.column = column;
		this.paths = paths;
	}

	/**
	 * Creates new JsonProperty
	 *
	 * @param column
	 */
	public JsonProperty(String column) {
		this.column = column;
	}

	/**
	 * Creates sql fragment for given property.
	 *
	 * @return ex: column->'json'->>'path'
	 * ex: cast(column->'json'->>'path' as bigint)
	 * ex: column->?->>?
	 */
	@Override
	public String toSqlString() {
		StringBuilder result = new StringBuilder();
		if (castType != null) {
			result.append("cast(");
		}
		result.append(column);
		Iterator<JsonPath> it = paths.iterator();

		if (!paths.isEmpty()) {
			result.append("->");
		}

		while (it.hasNext()) {
			JsonPath path = it.next();
			boolean appendSingleQuotes = JsonPath.PathType.STRING == path.getType() && !ignoreValues;

			if (!asJson && !it.hasNext()) {
				result.append(">");
			}

			if (appendSingleQuotes) {
				result.append("'");
			}

			if (ignoreValues) {
				ParametrizedValue value = null;
				if (randomKeyValues) {
					String randomKey = UUID.randomUUID().toString();
					value = new ParametrizedValue(randomKey, path.getKey());
				} else {
					value = new ParametrizedValue(path.getKey());
				}
				this.parametrizedValues.add(value);
				result.append(":").append(value.getKey());
			} else {
				result.append(path.getKey());
			}

			if (appendSingleQuotes) {
				result.append("'");
			}

			if (it.hasNext()) {
				result.append("->");
			}
		}

		if (castType != null) {
			result.append(" as ").append(castType.getCastType()).append(")");
		}

		return result.toString();
	}

	/**
	 * Returns parametrized values for property.
	 *
	 * @return
	 */
	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		return parametrizedValues;
	}

	/**
	 * Creates List<{@link JsonPath}> from given path as string.
	 * Example of path as string: <code>root.someArray[1].someValue</code>
	 *
	 * @param path
	 */
	private void initializePaths(String path) {
		if (path != null) {
			String[] paths = path.split("\\.");
			for (String sequence : paths) {
				if (!sequence.trim().isEmpty()) {
					if (ARRAY_PATTERN.matcher(sequence).find()) {
						String[] sequenceParts = sequence.replaceAll(ARRAY_REMOVE_REGEX, " ").split(" ");
						if (sequenceParts.length == 2) {
							this.paths.add(new JsonPath(sequenceParts[0], JsonPath.PathType.STRING));
							this.paths.add(new JsonPath(sequenceParts[1], JsonPath.PathType.NUMBER));
						}
					} else {
						this.paths.add(new JsonPath(sequence, JsonPath.PathType.STRING));
					}
				}
			}
		}
	}


	/**
	 * Property will return jsonb/json value
	 *
	 * @return
	 */
	public JsonProperty asJson() {
		this.asJson = true;
		return this;
	}

	/**
	 * Propery will return string value
	 *
	 * @return
	 */
	public JsonProperty asString() {
		this.asJson = false;
		return this;
	}

	/**
	 * Turns on funcion to replace values (path sequences inside property) with '?' or random uuid key.
	 *
	 * @return
	 */
	public JsonProperty ignoreValues() {
		this.ignoreValues = true;
		return this;
	}

	/**
	 * Turns on funcionality to generate random keys instead of values/paths inside query.
	 * <p>
	 * Ex: for given params column: 'column_name', json_path: 'some.json.path' to sqlStringMethod will generate:
	 * <code>column_name->:RANDOM_UUID_1->:RANDOM_UUID_2->:RANDOM_UUID_3</code>
	 * </p>
	 *
	 * @return
	 */
	public JsonProperty randomKeyValues() {
		this.ignoreValues = true;
		this.randomKeyValues = true;
		return this;
	}

	/**
	 * Turns on cast function.
	 *
	 * @param castType
	 * @return
	 */
	public JsonProperty cast(CastType castType) {
		this.castType = castType;
		return this;
	}

	/**
	 * Adds new path in path list.
	 * Path will be string.
	 *
	 * @param path
	 * @return
	 */
	public JsonProperty addPath(String path) {
		this.paths.add(new JsonPath(path, JsonPath.PathType.STRING));
		return this;
	}

	/**
	 * Adds new path to path list.
	 * Path will be number ( index of array ).
	 *
	 * @param path
	 * @return
	 */
	public JsonProperty addPath(Long path) {
		this.paths.add(new JsonPath(path.toString(), JsonPath.PathType.NUMBER));
		return this;
	}
}
