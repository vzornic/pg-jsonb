package com.vzornic.pgjson.postgresql.domain.jsonquery.implementation;

/**
 * Enum defines mode that will JsonProperty use for path walk inside json.
 * Currently postgres allows tree ways iterating through json.
 *
 * @author vedadzornic
 */
public enum JsonIteratorMode {


	/**
	 * If this notation is used paths will be separated and query will apply
	 * hashtag json notation #>.
	 * Example: root.child.final { json column } #> '{root,child,final}'
	 * <p>
	 */
	DEFAUTL,

	/**
	 * If this notation is used, jsonPath will be separated with arrow ( -> )
	 * ex: root.child.final = 'root'->'child'->>'final'
	 */
	ARROW_ITERATOR_MODE
}
