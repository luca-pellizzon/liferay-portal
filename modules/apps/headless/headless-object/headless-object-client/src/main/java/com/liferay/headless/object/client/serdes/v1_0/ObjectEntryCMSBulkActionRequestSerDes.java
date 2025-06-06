/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.client.serdes.v1_0;

import com.liferay.headless.object.client.dto.v1_0.ObjectEntryCMSBulkActionRequest;
import com.liferay.headless.object.client.json.BaseJSONParser;

import jakarta.annotation.Generated;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Alicia García
 * @generated
 */
@Generated("")
public class ObjectEntryCMSBulkActionRequestSerDes {

	public static ObjectEntryCMSBulkActionRequest toDTO(String json) {
		ObjectEntryCMSBulkActionRequestJSONParser
			objectEntryCMSBulkActionRequestJSONParser =
				new ObjectEntryCMSBulkActionRequestJSONParser();

		return objectEntryCMSBulkActionRequestJSONParser.parseToDTO(json);
	}

	public static ObjectEntryCMSBulkActionRequest[] toDTOs(String json) {
		ObjectEntryCMSBulkActionRequestJSONParser
			objectEntryCMSBulkActionRequestJSONParser =
				new ObjectEntryCMSBulkActionRequestJSONParser();

		return objectEntryCMSBulkActionRequestJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		ObjectEntryCMSBulkActionRequest objectEntryCMSBulkActionRequest) {

		if (objectEntryCMSBulkActionRequest == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (objectEntryCMSBulkActionRequest.getIds() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"ids\": ");

			sb.append("[");

			for (int i = 0; i < objectEntryCMSBulkActionRequest.getIds().length;
				 i++) {

				sb.append(objectEntryCMSBulkActionRequest.getIds()[i]);

				if ((i + 1) < objectEntryCMSBulkActionRequest.getIds().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (objectEntryCMSBulkActionRequest.getPermissions() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"permissions\": ");

			sb.append("[");

			for (int i = 0;
				 i < objectEntryCMSBulkActionRequest.getPermissions().length;
				 i++) {

				sb.append(objectEntryCMSBulkActionRequest.getPermissions()[i]);

				if ((i + 1) <
						objectEntryCMSBulkActionRequest.
							getPermissions().length) {

					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ObjectEntryCMSBulkActionRequestJSONParser
			objectEntryCMSBulkActionRequestJSONParser =
				new ObjectEntryCMSBulkActionRequestJSONParser();

		return objectEntryCMSBulkActionRequestJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ObjectEntryCMSBulkActionRequest objectEntryCMSBulkActionRequest) {

		if (objectEntryCMSBulkActionRequest == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (objectEntryCMSBulkActionRequest.getIds() == null) {
			map.put("ids", null);
		}
		else {
			map.put(
				"ids",
				String.valueOf(objectEntryCMSBulkActionRequest.getIds()));
		}

		if (objectEntryCMSBulkActionRequest.getPermissions() == null) {
			map.put("permissions", null);
		}
		else {
			map.put(
				"permissions",
				String.valueOf(
					objectEntryCMSBulkActionRequest.getPermissions()));
		}

		return map;
	}

	public static class ObjectEntryCMSBulkActionRequestJSONParser
		extends BaseJSONParser<ObjectEntryCMSBulkActionRequest> {

		@Override
		protected ObjectEntryCMSBulkActionRequest createDTO() {
			return new ObjectEntryCMSBulkActionRequest();
		}

		@Override
		protected ObjectEntryCMSBulkActionRequest[] createDTOArray(int size) {
			return new ObjectEntryCMSBulkActionRequest[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "ids")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "permissions")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			ObjectEntryCMSBulkActionRequest objectEntryCMSBulkActionRequest,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "ids")) {
				if (jsonParserFieldValue != null) {
					objectEntryCMSBulkActionRequest.setIds(
						toLongs((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "permissions")) {
				if (jsonParserFieldValue != null) {
					Object[] jsonParserFieldValues =
						(Object[])jsonParserFieldValue;

					com.liferay.headless.object.client.permission.Permission[]
						permissionsArray = new
						com.liferay.headless.object.client.permission.Permission
							[jsonParserFieldValues.length];

					for (int i = 0; i < permissionsArray.length; i++) {
						permissionsArray[i] =
							com.liferay.headless.object.client.permission.
								Permission.toDTO(
									(String)jsonParserFieldValues[i]);
					}

					objectEntryCMSBulkActionRequest.setPermissions(
						permissionsArray);
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			sb.append(_toJSON(value));

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value == null) {
			return "null";
		}

		if (value instanceof Map) {
			return _toJSON((Map)value);
		}

		Class<?> clazz = value.getClass();

		if (clazz.isArray()) {
			StringBuilder sb = new StringBuilder("[");

			Object[] values = (Object[])value;

			for (int i = 0; i < values.length; i++) {
				sb.append(_toJSON(values[i]));

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");

			return sb.toString();
		}

		if (value instanceof String) {
			return "\"" + _escape(value) + "\"";
		}

		return String.valueOf(value);
	}

}