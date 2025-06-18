/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.internal.resource.v1_0;

import com.liferay.headless.batch.engine.dto.v1_0.ImportTask;
import com.liferay.headless.batch.engine.resource.v1_0.ImportTaskResource;
import com.liferay.headless.object.dto.v1_0.ObjectEntryCMSBulkActionRequest;
import com.liferay.headless.object.dto.v1_0.ObjectEntryCMSBulkActionResponse;
import com.liferay.headless.object.resource.v1_0.ObjectEntryCMSBulkActionResource;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.vulcan.permission.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Luca Pellizzon
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/object-entry-cms-bulk-action.properties",
	scope = ServiceScope.PROTOTYPE,
	service = ObjectEntryCMSBulkActionResource.class
)
public class ObjectEntryCMSBulkActionResourceImpl
	extends BaseObjectEntryCMSBulkActionResourceImpl {

	@Override
	public ObjectEntryCMSBulkActionResponse postObjectEntryCMSBulkAction(
			String bulkActionName, String search, Filter filter,
			ObjectEntryCMSBulkActionRequest objectEntryCMSBulkActionRequest)
		throws Exception {

		Long[] ids = objectEntryCMSBulkActionRequest.getIds();

		if (ArrayUtil.isEmpty(ids)) {

			// Retrieve results from original query + filters + search

		}

		// Organize IDs by their ObjectDefinition

		Map<String, Map<Long, List<Long>>> map = new HashMap<>();

		for (long id : ids) {
			ObjectEntry objectEntry = _objectEntryLocalService.fetchObjectEntry(
				id);

			if (objectEntry != null) {
				map.computeIfAbsent(
					"ObjectEntry", key -> new HashMap<>()
				).computeIfAbsent(
					objectEntry.getObjectDefinitionId(),
					objectEntryId -> new ArrayList<>()
				).add(
					id
				);
			}
			else {
				ObjectEntryFolder objectEntryFolder =
					_objectEntryFolderLocalService.fetchObjectEntryFolder(id);

				if (objectEntryFolder != null) {
					map.computeIfAbsent(
						"ObjectEntryFolder", key -> new HashMap<>()
					).computeIfAbsent(
						objectEntryFolder.getObjectEntryFolderId(),
						key -> new ArrayList<>()
					).add(
						id
					);
				}
			}
		}

		// Call the batch endpoint of each ObjectDefinition

		List<Long> batchIds = new ArrayList<>();

		if (bulkActionName.equals("remove")) {
			_removeEntries(map);
		}
		else if (bulkActionName.equals("permission")) {
			_setPermissions(
				map, objectEntryCMSBulkActionRequest.getPermissions());
		}

		// Return batch IDs to caller

		return new ObjectEntryCMSBulkActionResponse() {
			{
				setIds(() -> batchIds.toArray(new Long[0]));
			}
		};
	}

	private Map<String, Object> _deleteImportTaskObject(
			String className, List<Long> ids,
			ImportTaskResource importTaskResource, String taskItemDelegateName)
		throws Exception {

		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyMap();
		}

		ImportTask importTask = importTaskResource.deleteImportTaskObject(
			className, null, null,
			ImportTask.ImportStrategy.ON_ERROR_CONTINUE.getValue(),
			taskItemDelegateName,
			transform(
				ids,
				id -> HashMapBuilder.put(
					"id", id
				).build()));

		return HashMapBuilder.<String, Object>put(
			"id", importTask.getId()
		).put(
			"processedItems", ids
		).build();
	}

	private ImportTaskResource _getImportTaskResource() {
		return _factory.create(
		).httpServletRequest(
			contextHttpServletRequest
		).httpServletResponse(
			contextHttpServletResponse
		).uriInfo(
			contextUriInfo
		).user(
			contextUser
		).build();
	}

	private Map<String, Object> _putImportTaskObject(
			String className, List<Long> ids,
			ImportTaskResource importTaskResource, Permission[] permissions,
			String taskItemDelegateName)
		throws Exception {

		if (ListUtil.isEmpty(ids) || ArrayUtil.isEmpty(permissions)) {
			return Collections.emptyMap();
		}

		ImportTask importTask = importTaskResource.putImportTaskObject(
			className, null, null, null, taskItemDelegateName, "PARTIAL_UPDATE",
			transform(
				ids,
				id -> HashMapBuilder.<String, Object>put(
					"id", id
				).put(
					"permissions",
					transformToList(
						permissions,
						permission -> HashMapBuilder.<String, Object>put(
							"actionIds",
							ListUtil.fromArray(permission.getActionIds())
						).put(
							"roleExternalReferenceCode",
							permission.getRoleExternalReferenceCode()
						).put(
							"roleName", permission.getRoleName()
						).put(
							"roleType", permission.getRoleType()
						).build())
				).build()));

		return HashMapBuilder.<String, Object>put(
			"id", importTask.getId()
		).put(
			"processedItems", ids
		).build();
	}

	private List<Map<String, Object>> _removeEntries(
			Map<String, Map<Long, List<Long>>> map)
		throws Exception {

		List<Map<String, Object>> lmap = new ArrayList<>();
		ImportTaskResource importTaskResource = _getImportTaskResource();

		if (map.containsKey("ObjectEntry")) {
			Map<Long, List<Long>> objectEntries = map.get("ObjectEntry");

			for (Map.Entry<Long, List<Long>> entry : objectEntries.entrySet()) {
				ObjectDefinition objectDefinition =
					_objectDefinitionLocalService.getObjectDefinition(
						entry.getKey());

				lmap.add(
					_deleteImportTaskObject(
						"com.liferay.object.rest.dto.v1_0.ObjectEntry",
						entry.getValue(), importTaskResource,
						objectDefinition.getName()));
			}
		}

		if (map.containsKey("ObjectEntryFolder")) {
			Map<Long, List<Long>> objectEntryFolders = map.get(
				"ObjectEntryFolder");

			lmap.add(
				_deleteImportTaskObject(
					"com.liferay.headless.object.dto.v1_0.ObjectEntryFolder",
					ListUtil.fromMapKeys(objectEntryFolders),
					importTaskResource, null));
		}

		return lmap;
	}

	private List<Map<String, Object>> _setPermissions(
			Map<String, Map<Long, List<Long>>> entriesIdByObjectDefinition,
			Permission[] permissions)
		throws Exception {

		if (ArrayUtil.isEmpty(permissions)) {
			return Collections.emptyList();
		}

		ImportTaskResource importTaskResource = _getImportTaskResource();
		List<Map<String, Object>> batchIds = new ArrayList<>();

		if (entriesIdByObjectDefinition.containsKey("ObjectEntry")) {
			Map<Long, List<Long>> objectEntries =
				entriesIdByObjectDefinition.get("ObjectEntry");

			for (Map.Entry<Long, List<Long>> entries :
					objectEntries.entrySet()) {

				ObjectDefinition objectDefinition =
					_objectDefinitionLocalService.getObjectDefinition(
						entries.getKey());

				batchIds.add(
					_putImportTaskObject(
						"com.liferay.object.rest.dto.v1_0.ObjectEntry",
						entries.getValue(), importTaskResource, permissions,
						objectDefinition.getName()));
			}
		}

		if (entriesIdByObjectDefinition.containsKey("ObjectEntryFolder")) {
			Map<Long, List<Long>> objectEntryFolders =
				entriesIdByObjectDefinition.get("ObjectEntryFolder");

			batchIds.add(
				_putImportTaskObject(
					"com.liferay.headless.object.dto.v1_0.ObjectEntryFolder",
					ListUtil.fromMapKeys(objectEntryFolders),
					importTaskResource, permissions, null));
		}

		return batchIds;
	}

	@Reference
	private ImportTaskResource.Factory _factory;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

}