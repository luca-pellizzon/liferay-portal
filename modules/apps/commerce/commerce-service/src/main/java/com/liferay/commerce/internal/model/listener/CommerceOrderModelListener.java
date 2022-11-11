/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.internal.model.listener;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
// import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ModelListener;
// import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.commerce.constants.CommerceOrderConstants;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Danny Situ
 */
@Component(immediate = true, service = ModelListener.class)
public class CommerceOrderModelListener
	extends BaseModelListener<CommerceOrder> {

	@Override
	public void onAfterUpdate(CommerceOrder originalCommerceOrder, CommerceOrder commerceOrder) {
		try { 
			if ((commerceOrder.getOrderStatus() == CommerceOrderConstants.ORDER_STATUS_COMPLETED) && (originalCommerceOrder.getOrderStatus() != CommerceOrderConstants.ORDER_STATUS_COMPLETED)) {
				ObjectDefinition objectDefinition = _objectDefinitionLocalService.fetchSystemObjectDefinition("AccountEntry");

				if (objectDefinition == null) {
					throw new PortalException("No AccountEntry System Object Definition");
				}

				ObjectField objectField = _objectFieldLocalService.getObjectField(objectDefinition.getObjectDefinitionId(), "points");

				Map<String, Serializable> values = HashMapBuilder.<String, Serializable>put(
					objectField.getName(), 0
				).build();

				try {
					Map<String, Serializable> existingValues = _objectEntryLocalService.getExtensionDynamicObjectDefinitionTableValues(objectDefinition, commerceOrder.getCommerceAccountId());

					if (!existingValues.isEmpty()) {
						values = existingValues;
					}
				}
				catch (PortalException portalException) {
					if (_log.isWarnEnabled()) {
						_log.warn(portalException);
					}
				}

				long quantity = 0;

				List<CommerceOrderItem> commerceOrderItems =
					commerceOrder.getCommerceOrderItems();

				for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
					quantity += commerceOrderItem.getQuantity();
				}

				values.put(objectField.getName(), (int)values.get(objectField.getName()) + quantity);


				_objectEntryLocalService.addOrUpdateExtensionDynamicObjectDefinitionTableValues(
					commerceOrder.getUserId(), objectDefinition, commerceOrder.getCommerceAccountId(),
					values, new ServiceContext());
			}
		}
		catch (PortalException portalException) {
			if (_log.isWarnEnabled()) {
				_log.warn(portalException);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderModelListener.class);

	@Reference
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;
		
	// @Reference
	// private CompanyLocalService _companyLocalService;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;


}