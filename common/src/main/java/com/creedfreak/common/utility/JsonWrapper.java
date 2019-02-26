package com.creedfreak.common.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * The json wrapper class, wraps a Gson object to retrieve and parse
 * json files for configuration.
 */
public class JsonWrapper {

	private Gson mGson;
	private ClassLoader mClassLoader;

	public JsonWrapper () {
		mGson = new GsonBuilder ().setPrettyPrinting ().create ();
		mClassLoader = this.getClass ().getClassLoader ();
	}

	/**
	 * readTable is used to read in Json objects from a file into a HashMap, where the Json (Key, Value) pair
	 * corresponds to the HashMap (Key, Value) pair. Likewise depending on the passed in type token, the HashMap
	 * may have more nested HashMaps or other type of objects.
	 *
	 * @param token    - The Type to pass to the Json Reader in order to parse the Json objects correctly.
	 * @param resource - The path to the resource we want to parse.
	 * @return - A HashMap which contains the json information from the resource parameter.
	 * @throws IOException - If the file cannot be found or read from then throw an IO exception
	 */
	public HashMap readJson (Type token, String resource) throws IOException {
		JsonReader reader;
		BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (mClassLoader.getResourceAsStream (resource)));
		HashMap parsedTable = null;

		reader = mGson.newJsonReader (bufferedReader);
		parsedTable = mGson.fromJson (reader, token);
		reader.close ();

		return parsedTable;
	}

	/**
	 * writeJson is used to write any HashMap out to a Json file. Each (Key, Value) pair in the HashMap gets
	 * written out to the file as a Json (Key, Value) pair.
	 *
	 * @param map      - The HashMap to write out to file.
	 * @param token    - The Type of the HashMap in which to write to the file.
	 * @param resource - The path to the file to write out to.
	 * @throws IOException -
	 */
	public void writeJson (HashMap map, Type token, String resource) throws IOException {
		JsonWriter writer;
		BufferedWriter bufferedWriter;

		bufferedWriter = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (resource)));
		writer = mGson.newJsonWriter (bufferedWriter);
		mGson.toJson (map, token, writer);

		writer.close ();
	}
}
