package com.pgjson.postgresql.domain.jsonquery.implementation;

/**
 * Enum defines mode that will JsonProperty use for path walk inside json.
 * Currently postgres allows tree ways iterating through json.
 *
 * @author vedadzornic
 */
public enum JsonIteratorMode {

	/**
	 * This is default json walk operator.
	 * If this notation is turned on, query will use '@>' operator.
	 * This operator should always be used because it is only one
	 * which use index.
	 */
	DEFAUTL,

	/**
	 * If this notation is used, jsonPath will be separated with arrow ( -> )
	 * ex: root.child.final = 'root'->'child'->>'final'
	 */
	ARROW_ITERATOR_MODE,

	/**
	 * If this notation is used paths will be separated and query will apply
	 * hashtag json notation #>.
	 * Example: root.child.final { json column } #> '{root,child,final}'
	 * <p>
	 * NOTE: This mode is currently not supported !!!
	 */
	HASHTAG_ITERATOR_MODE
}
