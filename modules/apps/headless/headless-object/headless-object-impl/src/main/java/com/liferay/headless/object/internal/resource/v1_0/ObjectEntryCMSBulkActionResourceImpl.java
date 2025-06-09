/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.internal.resource.v1_0;

import com.liferay.headless.batch.engine.dto.v1_0.ImportTask;
import com.liferay.headless.batch.engine.resource.v1_0.ImportTaskResource;
import com.liferay.headless.object.dto.v1_0.ObjectEntryCMSBulkAction;
import com.liferay.headless.object.resource.v1_0.ObjectEntryCMSBulkActionResource;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
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
	public ObjectEntryCMSBulkAction postObjectEntryCMSBulkAction(
			String bulkActionName, String[] entryIds, String search,
			Filter filter)
		throws Exception {

		if (ArrayUtil.isEmpty(entryIds)) {

			// Retrieve results from original query + filters + search

		}

		// Organize IDs by their ObjectDefinition

		Map<Long, List<Long>> entriesIdByObjectDefinition = new HashMap<>();

		for (String entryId : entryIds) {
			long objectEntryId = GetterUtil.getLong(entryId);

			ObjectEntry objectEntry = _objectEntryLocalService.getObjectEntry(
				objectEntryId);

			List<Long> idList = entriesIdByObjectDefinition.get(
				objectEntry.getObjectDefinitionId());

			if ((idList == null) || ListUtil.isEmpty(idList)) {
				idList = new ArrayList<>();

				entriesIdByObjectDefinition.put(
					objectEntry.getObjectDefinitionId(), idList);
			}

			idList.add(objectEntryId);
		}

		// Call the batch endpoint of each ObjectDefinition

		List<Long> batchIds = new ArrayList<>();

		switch (bulkActionName) {
			case "remove":
				batchIds.addAll(_removeEntries(entriesIdByObjectDefinition));
		}

		// Return batch IDs to caller

		ObjectEntryCMSBulkAction objectEntryCMSBulkAction =
			new ObjectEntryCMSBulkAction();

		objectEntryCMSBulkAction.setIds(batchIds.toArray(new Long[0]));

		return objectEntryCMSBulkAction;
	}

	private List<Long> _removeEntries(
			Map<Long, List<Long>> entriesIdByObjectDefinition)
		throws Exception {

		List<Long> batchIds = new ArrayList<>();
		ImportTaskResource importTaskResource = _factory.create(
		).httpServletRequest(
			contextHttpServletRequest
		).httpServletResponse(
			contextHttpServletResponse
		).uriInfo(
			contextUriInfo
		).user(
			contextUser
		).build();

		for (Map.Entry<Long, List<Long>> entries :
				entriesIdByObjectDefinition.entrySet()) {

			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.getObjectDefinition(
					entries.getKey());

			List<Map<String, Long>> list = new ArrayList<>();

			for (Long id : entries.getValue()) {
				list.add(
					HashMapBuilder.put(
						"id", id
					).build());
			}

			ImportTask importTask = importTaskResource.deleteImportTaskObject(
				"com.liferay.object.rest.dto.v1_0.ObjectEntry", null, null,
				ImportTask.ImportStrategy.ON_ERROR_CONTINUE.getValue(),
				objectDefinition.getName(), list);

			batchIds.add(importTask.getId());
		}

		return batchIds;
	}

	@Reference
	private ImportTaskResource.Factory _factory;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

}