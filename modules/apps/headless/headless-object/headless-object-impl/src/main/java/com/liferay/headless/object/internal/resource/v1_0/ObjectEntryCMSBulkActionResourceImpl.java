/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.internal.resource.v1_0;

import com.liferay.headless.object.dto.v1_0.ObjectEntryCMSBulkAction;
import com.liferay.headless.object.resource.v1_0.ObjectEntryCMSBulkActionResource;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alicia García
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

			if (ListUtil.isEmpty(idList)) {
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
				_removeEntries(batchIds, entriesIdByObjectDefinition);
		}

		// Return batch IDs to caller

		ObjectEntryCMSBulkAction objectEntryCMSBulkAction =
			new ObjectEntryCMSBulkAction();

		objectEntryCMSBulkAction.setIds(batchIds.toArray(new Long[0]));

		return objectEntryCMSBulkAction;
	}

	private String _getAPIURL(ObjectDefinition objectDefinition)
		throws Exception {

		Company company = _companyLocalService.getCompany(
			objectDefinition.getCompanyId());

		boolean secure = _isHttpsEnabled();

		String apiURL = _portal.getPortalURL(
			company.getVirtualHostname(), _portal.getPortalServerPort(secure),
			secure);

		if (objectDefinition == null) {
			return apiURL;
		}

		return apiURL + _portal.getPathContext() +
			objectDefinition.getRESTContextPath();
	}

	private boolean _isHttpsEnabled() {
		if (Objects.equals(
				Http.HTTPS,
				PropsUtil.get(PropsKeys.PORTAL_INSTANCE_PROTOCOL)) ||
			Objects.equals(
				Http.HTTPS, PropsUtil.get(PropsKeys.WEB_SERVER_PROTOCOL))) {

			return true;
		}

		return false;
	}

	private void _removeEntries(
			List<Long> batchIds,
			Map<Long, List<Long>> entriesIdByObjectDefinition)
		throws Exception {

		for (Long objectDefinitionId : entriesIdByObjectDefinition.keySet()) {
			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.getObjectDefinition(
					objectDefinitionId);

			// Get URL to objectDefinition specific endpoint

			String apiURL = _getAPIURL(objectDefinition);

			// Call batch endpoint and send back the job ID

			String results = HttpUtil.URLtoString(apiURL);
		}
	}

	@Resource
	private CompanyLocalService _companyLocalService;

	@Resource
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Resource
	private ObjectEntryLocalService _objectEntryLocalService;

	@Resource
	private Portal _portal;

}